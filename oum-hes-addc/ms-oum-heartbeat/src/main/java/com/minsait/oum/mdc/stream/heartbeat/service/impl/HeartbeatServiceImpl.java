package com.minsait.oum.mdc.stream.heartbeat.service.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.mbed.coap.client.CoapClient;
import com.mbed.coap.client.CoapClientBuilder;
import com.mbed.coap.exception.CoapException;
import com.mbed.coap.packet.BlockSize;
import com.mbed.coap.packet.CoapPacket;
import com.mbed.coap.packet.MediaTypes;
import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;
import com.minsait.oum.mdc.domain.ResultVO;
import com.minsait.oum.mdc.stream.heartbeat.data.ProfileApplication;
import com.minsait.oum.mdc.stream.heartbeat.domain.DeviceStatus;
import com.minsait.oum.mdc.stream.heartbeat.rest.MdmRestClient;
import com.minsait.oum.mdc.stream.heartbeat.service.HeartbeatDataService;
import com.minsait.oum.mdc.stream.heartbeat.service.HeartbeatDataService.GatewayParamsResponse;
import com.minsait.oum.mdc.stream.heartbeat.service.HeartbeatService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HeartbeatServiceImpl implements HeartbeatService {

	private static final int HEART_BEAT_PORT = 5683;

	@Autowired
	private HeartbeatDataService dataService;

	@Autowired
	private ProfileApplication application;
	
	@Autowired
	private MdmRestClient _mdmRestClient;

	@Override
	public void handle(Request heartbeatRequest) {
		HeartBeatProcessResult heartBeatResult = process(heartbeatRequest);

		/*
		 * if gateway is not found in this microservice, do not create call, we assume
		 * that another mqtt-input microservice instance will identify this gateway and
		 * manage its heartbeat
		 */
		if (!heartBeatResult.isGatewayFound()) {
			log.warn("gateway {} not found: probably is managed by another Heartbeat microservice instance",
					heartBeatResult.getGatewaySN());
			return;
		}


		// create call and update HeartBeat status
		heartbeatRequest.setSerialnumber(heartBeatResult.getGatewaySN());
		for (Device device : heartbeatRequest.getDevices()) {
			device.setSerialNumber(heartBeatResult.getGatewaySN());
		}
		Call callMonitor = this.application.createCallMonitorEntry(heartbeatRequest);
		if (callMonitor != null)
			application.updateCallStatus(callMonitor, heartBeatResult.getResult().getStatus(),
					heartBeatResult.getResult().getMessage());

	}

	private HeartBeatProcessResult process(Request heartbeatRequest) {
		HeartBeatProcessResult result = HeartBeatProcessResult.from(null, new ResultVO().setStatus(Status.ERROR));

		for (Device device : heartbeatRequest.getDevices())
			try {
				result = this.processDevice(device.getSerialNumber(), device.getIp());
			} catch (Exception ex) {
				log.error(String.format("error processing heartbeat for device %s: %s", device.getSerialNumber(),
						ex.getMessage()), ex);
				return HeartBeatProcessResult.from(device.getSerialNumber(),
						new ResultVO().setStatus(Status.ERROR).setMessage(ex.getMessage()));
			}

		return result;
	}

	private HeartBeatProcessResult processDevice(String deviceName, String ip) {
		try {
			
			String eqCode = _mdmRestClient.getEqCodeBySerialNumber(deviceName);
			// try to find status for device name received
			Optional<DeviceStatus> statusOpt = dataService.getDeviceStatus(eqCode);

			/*
			 * if status is not present, the gateway is not managed by this microservice and
			 * probably another microservice instance can identify and manage this gateway.
			 * Return a gateway not found message to omit further call process
			 */
			if (!statusOpt.isPresent())
				return HeartBeatProcessResult.from(false, eqCode, new ResultVO().setStatus(Status.ERROR));

			DeviceStatus status = statusOpt.get();
			// if status is not PRELOADED, finish with OK
			if (!status.equals(DeviceStatus.PRELOADED))
				return HeartBeatProcessResult.from(eqCode, new ResultVO().setStatus(Status.FINISH_OK));

			// status must be PRELOADED, ask MDM for gateway params
			GatewayParamsResponse gatewayParamsResponse = dataService.getGatewayParams(eqCode);

			if (gatewayParamsResponse.isFailed())
				return HeartBeatProcessResult.from(eqCode,
						new ResultVO().setStatus(Status.FINISH_WITH_ERROR)
								.setMessage(String.format("Error getting gateway params for device %s : %s", eqCode,
										gatewayParamsResponse.getBody())));

			// check that gateway has meters assigned, it not return
			JSONObject gatewayParamsJson = new JSONObject(gatewayParamsResponse.getBody());
			if (gatewayParamsJson.getJSONArray("meterList").isEmpty())
				return HeartBeatProcessResult.from(eqCode, new ResultVO().setStatus(Status.FINISH_WARNING)
						.setMessage("Gateway without meters: " + eqCode));

			if (dataService.isChangeDeviceStatusWebServiceEnabled())
				dataService.setDeviceStatus(eqCode, DeviceStatus.DISCOVERED);

			if (dataService.isChangeDeviceIpWebServiceEnabled())
				dataService.setDeviceIp(eqCode, ip, HEART_BEAT_PORT);

			try {
				// create coap message
				gatewayParamsJson.put("idRequest", UUID.randomUUID().toString());
				gatewayParamsJson.put("gWsn", deviceName);
				gatewayParamsJson.put("gWip", ip);

				sendCoapMessage(deviceName, ip, gatewayParamsJson);
			} catch (CoapMessageException cmex) {
				return HeartBeatProcessResult.from(eqCode,
						new ResultVO().setStatus(Status.FINISH_WITH_ERROR).setMessage(String
								.format("Error sending coap message to device %s: %s", eqCode, cmex.getMessage())));
			}

			return HeartBeatProcessResult.from(eqCode, new ResultVO().setStatus(Status.FINISH_OK));

		} catch (RestClientException rcex) {
			log.error(String.format("error during HeartBeat device %s process: %s", deviceName, rcex.getMessage()));
			return HeartBeatProcessResult.from(deviceName,
					new ResultVO().setStatus(Status.FINISH_WITH_ERROR).setMessage(rcex.getMessage()));
		}
	}

	private void sendCoapMessage(String deviceName, String ip, JSONObject gatewayParamsJson)
			throws CoapMessageException {
		CoapClient client = null;
		try {
			log.info("new COAP gatewayConfig request to device {}[{}:{}]: {}", deviceName, ip, HEART_BEAT_PORT,
					gatewayParamsJson);
			client = CoapClientBuilder.newBuilder(new InetSocketAddress(ip, HEART_BEAT_PORT))
					.maxIncomingBlockTransferSize(65536).blockSize(BlockSize.S_64).build();

			CoapPacket coapResponse = client.resource("/gatewayconfig").blockSize(BlockSize.S_64)
					.payload(gatewayParamsJson.toString(), MediaTypes.CT_APPLICATION_JSON).sync().post();

			log.info("COAP gatewayConfig response received to device {}[{}:{}]: {} - {}", deviceName, ip,
					HEART_BEAT_PORT, coapResponse.getCode().codeToString(), coapResponse.getPayloadString());
		} catch (IOException | CoapException ex) {
			log.error(String.format("cannot send COAP gatewayConfig message to device %s[%s:%d]: %s", deviceName, ip,
					HEART_BEAT_PORT, ex.getMessage()), ex);
			throw new CoapMessageException(ex.getMessage(), ex);
		} finally {
			if (client != null)
				client.close();

		}
	}

	@SuppressWarnings("serial")
	private static final class CoapMessageException extends Exception {

		public CoapMessageException(String message, Throwable throwable) {
			super(message, throwable);

		}

	}
}
