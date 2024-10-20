package com.minsait.oum.mdc.mqtt.input.gateway.heartbeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.application.ProfileApplication;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.kafka.publisher.HeartbeatMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatHandler implements MessageHandler {

	@Autowired
	private ProfileApplication application;

	@Autowired
	private HeartBeatAdapter adapter;

	@Autowired
	private HeartbeatMessageService heartbeatMessageService;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		Request request = null;

		try {

			log.debug("==> Message: " + message.getHeaders() + "->" + message.getPayload());
			try {
				request = adapter.convert(message.getPayload().toString());
				log.info("[HeartBeatAdapter] request id: " + request.getIdRequest());
			} catch (HeartBeatException hbx) {
				request = hbx.getRequest();
				request.setErrorMessage(hbx.getMessage());
				request.setResponseType(ResponseType.HEART_BEAT);
			}

			// send request to kafka topic so that it'll be processed by oum-heartbeat
			// microservice
			heartbeatMessageService.send(request);

		} catch (Exception ex) {
			log.error(String.format("error sending heartbeat request %s to kafka: %s", request, ex.getMessage()), ex);
			// if we cannot send request to kafka, immediately close call with error
			Call callMonitor = this.application.createCallMonitorEntry(request);
			if (callMonitor != null)
				application.updateCallStatus(callMonitor, Status.ERROR, ex.getMessage());

		}
	}

}
