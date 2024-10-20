package com.minsait.oum.mdc.mqtt.input.setpaymentmode;

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
public class SetPaymentModeChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${mqtt.topics.on-demand.set-payment-mode}")
	private String topic;

	@Bean
	public MessageChannel mqttInputSetPaymentModeChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundSetPaymentMode() {

		String clientID = "setPaymentMode" + UUID.randomUUID().toString();
		
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
		//adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputSetPaymentModeChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputSetPaymentModeChannel")
	public MessageHandler handlerSetPaymentMode() {
		return new SetPaymentModeHandler();
	}
}