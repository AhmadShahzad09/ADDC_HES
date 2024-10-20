package com.minsait.oum.mdc.mqtt.input.gateway.setprofileinterval;

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
public class SetProfileIntervalChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${mqtt.topics.gateway.set-profile-intervalperiod}")
	private String topic;

	@Bean
	public MessageChannel mqttInputSetProfileIntervalChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundSetProfileInterval() {

		String clientID = "setProfileInterval" + UUID.randomUUID();
		
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
		//adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputSetProfileIntervalChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputSetProfileIntervalChannel")
	public MessageHandler handlerSetProfileInterval() {
		return new SetProfileIntervalHandler();
	}
}
