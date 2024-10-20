package com.minsait.oum.mdc.mqtt.input.loadprofile.loadprofile1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.UUID;

@Profile("!test")
@Configuration
public class LoadProfile1Channel {
	
	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${mqtt.topics.meter.load-profile1}")
	private String topic;

	@Bean
	public MessageChannel mqttInputLoadProfileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundLoadProfile() {

		String clientID = UUID.randomUUID().toString();
		
		// v9
//		String topicV9Old = "meter/loadprofile";
//		String topicV9 = "$share/group01/meter/loadprofile";

		// v13
//		String topicV13Old = "meter/loadprofile1";
//		String topicV13 = "$share/group01/meter/loadprofile1";
		
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
//		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputLoadProfileChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputLoadProfileChannel")
	public MessageHandler handlerLoadProfile() {
		return new LoadProfile1Handler();
	}
}
