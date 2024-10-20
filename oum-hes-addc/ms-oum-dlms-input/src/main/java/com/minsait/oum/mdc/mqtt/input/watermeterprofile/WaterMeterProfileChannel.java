package com.minsait.oum.mdc.mqtt.input.watermeterprofile;

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
public class WaterMeterProfileChannel {
	
	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${mqtt.topics.meter.water-profile}")
	private String topic;

	@Bean
	public MessageChannel mqttInputWaterMeterProfileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundWaterProfile() {

		String clientID = UUID.randomUUID().toString();
		
//		String topicOld = "meter/waterprofile";
//		String topic = "$share/group01/meter/waterprofile";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
//		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputWaterMeterProfileChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputWaterMeterProfileChannel")
	public MessageHandler handlerWaterMeterProfile() {
		return new WaterMeterProfileHandler();
	}
}

