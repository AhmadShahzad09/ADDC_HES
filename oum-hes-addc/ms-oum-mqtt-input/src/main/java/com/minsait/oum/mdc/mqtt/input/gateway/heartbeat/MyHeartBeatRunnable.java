package com.minsait.oum.mdc.mqtt.input.gateway.heartbeat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import com.mbed.coap.client.CoapClient;
import com.mbed.coap.client.CoapClientBuilder;
import com.mbed.coap.exception.CoapException;
import com.mbed.coap.packet.BlockSize;
import com.mbed.coap.packet.CoapPacket;
import com.mbed.coap.packet.MediaTypes;
import com.minsait.mdc.util.DeviceStatus;
import com.minsait.mdc.util.MdmRestClient;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.domain.EquipmentTask;
import com.minsait.oum.mdc.domain.ResultVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyHeartBeatRunnable implements Callable<HeartBeatProcessResult> {

	private static final int HEART_BEAT_PORT = 5683;

//	private Request request;

	private EquipmentTask equipment;

	private String ip;

	private String deviceName;

	private MdmRestClient _restClient;

	public MyHeartBeatRunnable(MdmRestClient restClient, String ip, String deviceName) {

		this.ip = ip;
		this.deviceName = deviceName;
		_restClient = restClient;

	}

	public EquipmentTask getEquipment() {
		return equipment;
	}

	public void setEquipment(EquipmentTask equipment) {
		this.equipment = equipment;
	}

	@Override
	public HeartBeatProcessResult call() throws Exception {

		log.info("processing new HeartBeat message : {}", this);

		try {
			// try to find status for device name received
			Optional<DeviceStatus> statusOpt = _restClient.getDeviceStatus(deviceName);

			/*
			 * if status is not present, the gateway is not managed by this microservice and
			 * probably another microservice instance can identify and manage this gateway.
			 * Return a gateway not found message to omit further call process
			 */
			if (!statusOpt.isPresent())
				return HeartBeatProcessResult.from(false, deviceName, new ResultVO().setStatus(Status.ERROR));

			DeviceStatus status = statusOpt.get();
			// if status is not PRELOADED, finish with OK
			if (!status.equals(DeviceStatus.PRELOADED))
				return HeartBeatProcessResult.from(deviceName, new ResultVO().setStatus(Status.FINISH_OK));

			// status must be PRELOADED, ask MDM for gateway params
			ResponseEntity<String> gatewayParamResponse = _restClient.getGatewayParams(deviceName);

			if (!gatewayParamResponse.getStatusCode().equals(HttpStatus.OK))
				return HeartBeatProcessResult.from(deviceName,
						new ResultVO().setStatus(Status.FINISH_WITH_ERROR)
								.setMessage(String.format("Error getting gateway params for device %s : %s", deviceName,
										gatewayParamResponse.getBody())));

			// check that gateway has meters assigned, it not return
			JSONObject gatewayParamsJson = new JSONObject(gatewayParamResponse.getBody());
			if (gatewayParamsJson.getJSONArray("meterList").isEmpty())
				return HeartBeatProcessResult.from(deviceName, new ResultVO().setStatus(Status.FINISH_WARNING)
						.setMessage("Gateway without meters: " + deviceName));

			if (_restClient.isChangeDeviceStatusWebServiceEnabled())
				_restClient.setDeviceStatus(deviceName, DeviceStatus.DISCOVERED);

			if (_restClient.isChangeDeviceIpWebServiceEnabled())
				_restClient.setDeviceIp(deviceName, ip, HEART_BEAT_PORT);

			try {
				// create coap message
				gatewayParamsJson.put("idRequest", UUID.randomUUID().toString());
				gatewayParamsJson.put("gWsn", deviceName);
				gatewayParamsJson.put("gWip", ip);

				sendCoapMessage(gatewayParamsJson);
			} catch (CoapMessageException cmex) {
				return HeartBeatProcessResult.from(deviceName,
						new ResultVO().setStatus(Status.FINISH_WITH_ERROR).setMessage(String
								.format("Error sending coap message to device %s: %s", deviceName, cmex.getMessage())));
			}

			return HeartBeatProcessResult.from(deviceName, new ResultVO().setStatus(Status.FINISH_OK));

		} catch (RestClientException rcex) {
			log.error(String.format("error during HeartBeat device %s process: %s", deviceName, rcex.getMessage()));
			return HeartBeatProcessResult.from(deviceName,
					new ResultVO().setStatus(Status.FINISH_WITH_ERROR).setMessage(rcex.getMessage()));
		}
	}

	private void sendCoapMessage(JSONObject jsonObject) throws CoapMessageException {
		CoapClient client = null;
		try {
			log.info("new COAP gatewayConfig request to device {}[{}:{}]: {}", deviceName, ip, HEART_BEAT_PORT,
					jsonObject);
			client = CoapClientBuilder.newBuilder(new InetSocketAddress(ip, HEART_BEAT_PORT))
					.maxIncomingBlockTransferSize(65536).blockSize(BlockSize.S_64).build();

			CoapPacket coapResponse = client.resource("/gatewayconfig").blockSize(BlockSize.S_64)
					.payload(jsonObject.toString(), MediaTypes.CT_APPLICATION_JSON).sync().post();

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

	@Override
	public String toString() {
		return "MyHeartBeatRunnable [deviceName=" + deviceName + ", ip=" + ip + ", equipment=" + equipment
				+ ", _restClient=" + _restClient + "]";
	}

	@SuppressWarnings("serial")
	private static final class CoapMessageException extends Exception {

		public CoapMessageException(String message, Throwable throwable) {
			super(message, throwable);

		}

	}

}
