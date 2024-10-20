package com.minsait.oum.mdc.mqtt.input.billingreset;

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
public class BillingResetChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;

	@Value("${mqtt.topics.on-demand.billing-reset}")
	private String topic;

	@Bean
	public MessageChannel mqttInputBillingResetChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundBillingReset() {

		String clientID = "billingResetSuscriber" + UUID.randomUUID().toString();

//		String topicOld = "meter/reconnection";
//		String topic = "$share/group01/meter/reconnection";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID,
				mqttPahoClientFactory, topic);
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputBillingResetChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputBillingResetChannel")
	public MessageHandler handlerBillingReset() {
		return new BillingResetHandler();
	}
}
