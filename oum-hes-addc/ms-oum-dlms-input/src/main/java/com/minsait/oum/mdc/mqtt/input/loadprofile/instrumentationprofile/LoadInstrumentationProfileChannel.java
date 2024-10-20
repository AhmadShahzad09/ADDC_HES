package com.minsait.oum.mdc.mqtt.input.loadprofile.instrumentationprofile;

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
public class LoadInstrumentationProfileChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${mqtt.topics.meter.instrumentation-profile}")
	private String topic;

	@Bean
	public MessageChannel mqttInputLoadInstrumentationProfileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundInstrumentationProfile() {

		String clientID = UUID.randomUUID().toString();
		
//		String topicOld = "meter/instrumentationprofile";
//		String topic = "$share/group01/meter/instrumentationprofile";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
//		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputLoadInstrumentationProfileChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputLoadInstrumentationProfileChannel")
	public MessageHandler handlerLoadInstrumentationProfile() {
		return new LoadInstrumentationProfileHandler();
	}
}
