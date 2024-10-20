package com.minsait.oum.mdc.mqtt.input.loadprofile.loadprofile2;

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
public class LoadProfile2Channel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;

	@Value("${mqtt.topics.meter.load-profile2}")
	private String topic;

	@Bean
	public MessageChannel mqttInputLoadProfileChannelTwo() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundLoadProfileTwo() {

		String clientID = UUID.randomUUID().toString();
		
//		String topicOld = "meter/loadprofile2";
//		String topic = "$share/group01/meter/loadprofile2";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
//		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputLoadProfileChannelTwo());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputLoadProfileChannelTwo")
	public MessageHandler handlerLoadProfileTwo() {
		return new LoadProfile2Handler();
	}
}
