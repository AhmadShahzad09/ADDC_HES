package com.minsait.oum.mdc.mqtt.input.loadprofile.powerqualityprofile;

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
public class LoadPowerQualityProfileChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;

	@Value("${mqtt.topics.meter.power-quality-profile}")
	private String topic;

	@Bean
	public MessageChannel mqttInputLoadPowerQualityProfileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundLoadPowerQualityProfile() {

		String clientID = UUID.randomUUID().toString();
		
//		String topicOld = "meter/powerqualityprofile";
//		String topic = "$share/group01/meter/powerqualityprofile";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
//		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputLoadPowerQualityProfileChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputLoadPowerQualityProfileChannel")
	public MessageHandler handlerLoadPowerQualityProfileChannel() {
		return new LoadPowerQualityProfileHandler();
	}
}
