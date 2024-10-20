package com.minsait.oum.mdc.mqtt.input.setmeteringmode;

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
public class SetMeteringModeChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${mqtt.topics.on-demand.set-metering-mode}")
	private String topic;

	@Bean
	public MessageChannel mqttInputSetMeteringModeChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer producer() {

		String clientID =  UUID.randomUUID().toString();

		
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputSetMeteringModeChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputSetMeteringModeChannel")
	public MessageHandler handler() {
		return new SetMeteringModeHandler();
	}
}
