package com.minsait.oum.mdc.mqtt.input.getmeteringmode;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.*;
import org.springframework.integration.core.*;
import org.springframework.integration.mqtt.core.*;
import org.springframework.integration.mqtt.inbound.*;
import org.springframework.integration.mqtt.support.*;
import org.springframework.messaging.*;

import java.util.*;

@Profile("!test")
@Configuration
public class GetMeteringModeChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${mqtt.topics.on-demand.get-metering-mode}")
	private String topic;

	@Bean
	public MessageChannel mqttInputGetMeteringModeChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundGetMeteringMode() {

		String clientID =  UUID.randomUUID().toString();

		
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputGetMeteringModeChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputGetMeteringModeChannel")
	public MessageHandler handlerGetMeteringMode() {
		return new GetMeteringModeHandler();
	}
}
