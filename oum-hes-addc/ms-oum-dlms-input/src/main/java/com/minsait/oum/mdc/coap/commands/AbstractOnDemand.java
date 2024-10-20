package com.minsait.oum.mdc.coap.commands;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.mbed.coap.client.CoapClient;
import com.mbed.coap.client.CoapClientBuilder;
import com.mbed.coap.packet.BlockSize;
import com.mbed.coap.packet.CoapPacket;
import com.mbed.coap.packet.MediaTypes;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import com.minsait.oum.mdc.kafka.publisher.TaskStatusMessageService;
import com.minsait.oum.mdc.listener.task.PlatformTaskVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractOnDemand {

	@Autowired
	private TaskStatusMessageService taskStatusMessageService;

	@Autowired
	private Gson gson;

	protected abstract String getCoapPath();

	protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					executeCommand(buildDefaultCommand(id, orders, ipPort), getCoapPath(), ipPort);
				} catch (Exception ex) {
					log.error(String.format("Cannot execute command '%s', error building command:  %s", command,
							ex.getMessage()), ex);
					// capture build default command exception to finish call on call monitor
					sendError(id, null, ex.getMessage());
				}
			}
		}).start();
	}

	private OnDemandCommandVO buildDefaultCommand(String id, OnDemandOrder orders, String ipPort) {
		OnDemandCommandVO commandVO = new OnDemandCommandVO();
		commandVO.setDevices(orders.getDevices());
		commandVO.setIdRequest(id);
		commandVO.setGWsn(orders.getGatewaySerialNumber());
		commandVO.setGWip(StringUtils.EMPTY);
		commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
		customizeCommand(commandVO, orders);
		return commandVO;
	}

	protected abstract void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders);

	protected void executeCommand(OnDemandCommandVO commandVO, String coapPath, String ipPort) {

		String requestId = commandVO.getIdRequest();
		String payload = gson.toJson(commandVO);
		log.debug("ON DEMAND Payload " + payload);
		log.debug("ON DEMAND Request Id " + requestId);

		CoapClient client = null;
		CoapPacket coapResp = null;
		try {
			client = CoapClientBuilder
					.newBuilder(new InetSocketAddress(ipPort.split(":")[0], Integer.parseInt(ipPort.split(":")[1])))
					.maxIncomingBlockTransferSize(65536).blockSize(BlockSize.S_64)
					.build();
			log.info("[COAP client] created:" + ipPort.split(":")[0]);
			log.info("[COAP client] sending POST to " + coapPath + " with payload: " + payload);
			coapResp = client.resource(coapPath).blockSize(BlockSize.S_64).payload(payload, MediaTypes.CT_APPLICATION_JSON).sync().post();
			log.info("[COAP client] already sent data");
			int coapResponseCode = coapResp.getCode().getHttpCode();
			log.info("[COAP client] response:" + coapResponseCode);
			log.info("The COAP result is: " + coapResp + " ->CoapCode:" + coapResp.getCode().getCoapCode()
					+ " httpcode->" + coapResp.getCode().getHttpCode() + "-> payload->" + coapResp.getPayloadString());
			int httpCode = coapResponseCode;

			if (httpCode < 200 || httpCode > 299) {
				throw new Exception(String.format(
						"coap message not processed: fail response code returned is %d and response payload is %s",
						httpCode, coapResp.getPayloadString()));
			}
			log.debug("ON DEMAND Request id" + requestId + " has been sent");
		} catch (Exception e) {
			sendError(requestId, coapResp, e.getMessage());
			log.info("[COAP client] Error in COAP client:" + e);
		} finally {
			log.info("[COAP client] closing socket");
			client.close();
			log.info("[COAP client] socket closed");
		}
		log.debug("ON DEMAND Request id" + requestId + " has been sent");
	}

	protected void sendError(String id, CoapPacket coapResp, String message) {
//		MqttPublisherErrorMessage message = new MqttPublisherErrorMessage();
//		message.setErrorCode("Device could not be reached or communication was rejected: error code->" + coapResp);
//		message.setTime(new Date().getTime());
//		message.setIdRequest(id);
//
//		String errorPayLoad = gson.toJson(message);
//		log.error("Error payload to be sent: " + errorPayLoad);
//		mqttErrorClient.sendToMqtt(errorPayLoad);

		Map<Integer, String> errorMap = new LinkedTreeMap<Integer, String>();
		errorMap.put(1, message);

		taskStatusMessageService.send(PlatformTaskVO.builder().pid(id).error(errorMap).build());

//    	{
//    		"pid": "asdfasdf",
//    		"taskId": 34523,
//    		"orderId": 245234,
//    		"requestId": 453453,
//    		"meterId": "asdfasdf",
//    		"error":[],
//    		"debug":[],
//    		"info":[]
//    		}

	}
}