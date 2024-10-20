package com.minsait.oum.mdc.mqtt.input.getinstvalues;

import java.util.UUID;

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

@Profile("!test")
@Configuration
public class GetInstantaneousValuesChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;

	@Value("${mqtt.topics.on-demand.instantaneous-values}")
	private String topic;

	@Bean
	public MessageChannel mqttInputGetInstValuesChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundGetInstValuesProfile() {

		String clientID = UUID.randomUUID().toString();

//		String topicOld = "meter/instregisters";
//		String topic = "$share/group01/meter/instregisters";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID,
				mqttPahoClientFactory, topic);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputGetInstValuesChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputGetInstValuesChannel")
	public MessageHandler handlerGetInstValuesProfile() {
		return new GetInstantaneousValuesHandler();
	}
}
