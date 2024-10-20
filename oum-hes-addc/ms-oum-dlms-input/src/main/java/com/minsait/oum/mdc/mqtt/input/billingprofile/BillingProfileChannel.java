package com.minsait.oum.mdc.mqtt.input.billingprofile;

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
public class BillingProfileChannel {
	
	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
		
	@Value("${mqtt.topics.meter.billing-profile}")
	private String topic;

	@Bean
	public MessageChannel mqttInputBillingProfileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundBillingProfile() {

		String clientID = UUID.randomUUID().toString();
		
//		String topicOld = "meter/billingprofile";
//		String topic = "$share/group01/meter/billingprofile";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
//		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputBillingProfileChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputBillingProfileChannel")
	public MessageHandler handlerBillingProfile() {
		return new BillingProfileHandler();
	}
}


