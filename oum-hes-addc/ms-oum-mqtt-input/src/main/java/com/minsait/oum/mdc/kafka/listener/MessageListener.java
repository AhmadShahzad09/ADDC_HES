package com.minsait.oum.mdc.kafka.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.minsait.mdc.data.model.Call;
import com.minsait.oum.mdc.coap.commands.RequestProcessApplication;
import com.minsait.oum.mdc.driver.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageListener {
	
	@Autowired
	private Gson gson;
	
	@Autowired
	private RequestProcessApplication application;

	@StreamListener(MessageStreamEtisalat.ETISALAT)
	public void getEtisalatMsg(@Payload final Message message) throws Exception {

		log.info("RequestProcessApplication >> Getting message: {}", message.getMessage().toString());

		Call call = gson.fromJson(message.getMessage(), Call.class);

		log.info("MessageListener >> Processing call: {}...", call.toString());

		this.application.process(call);

		log.info("MessageListener >> Call processed");
	}
}
