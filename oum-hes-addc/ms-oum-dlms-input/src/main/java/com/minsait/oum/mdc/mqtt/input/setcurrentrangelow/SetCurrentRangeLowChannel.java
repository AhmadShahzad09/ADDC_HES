package com.minsait.oum.mdc.mqtt.input.setcurrentrangelow;

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
public class SetCurrentRangeLowChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;

	@Value("${mqtt.topics.on-demand.set-current-range-low}")
	private String topic;

	@Bean
	public MessageChannel mqttInputSetCurrentRangeLowChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundSetCurrentRangeLow() {

		String clientID = "setCurrentRangeLow" + UUID.randomUUID().toString();

//		String topicOld = "meter/switchstatus";
//		String topic = "$share/group01/meter/switchstatus";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID,
				mqttPahoClientFactory, topic);
		// adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputSetCurrentRangeLowChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputSetCurrentRangeLowChannel")
	public MessageHandler handlerSetCurrentRangeLow() {
		return new SetCurrentRangeLowHandler();
	}
}
