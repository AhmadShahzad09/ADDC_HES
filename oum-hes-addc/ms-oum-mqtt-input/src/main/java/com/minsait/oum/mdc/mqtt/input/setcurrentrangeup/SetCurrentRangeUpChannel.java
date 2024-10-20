package com.minsait.oum.mdc.mqtt.input.setcurrentrangeup;

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
public class SetCurrentRangeUpChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;

	@Value("${mqtt.topics.on-demand.set-current-range-up}")
	private String topic;

	@Bean
	public MessageChannel mqttInputSetCurrentRangeUpChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundSetCurrentRangeUp() {

		String clientID = "setCurrentRangeUp" + UUID.randomUUID().toString();

//		String topicOld = "meter/switchstatus";
//		String topic = "$share/group01/meter/switchstatus";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID,
				mqttPahoClientFactory, topic);
		// adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputSetCurrentRangeUpChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputSetCurrentRangeUpChannel")
	public MessageHandler handlerSetCurrentRangeUp() {
		return new SetCurrentRangeUpHandler();
	}
}
