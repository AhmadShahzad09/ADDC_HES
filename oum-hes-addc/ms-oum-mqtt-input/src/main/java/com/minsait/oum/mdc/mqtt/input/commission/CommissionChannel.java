package com.minsait.oum.mdc.mqtt.input.commission;

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
public class CommissionChannel {
	
	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${mqtt.topics.gateway.meter-discovery}")
	private String topic;

	@Bean
	public MessageChannel mqttInputCommissionChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundCommission() {

		String clientID = UUID.randomUUID().toString();

//		String topicOld = "gateway/comission";
//		String topic = "$share/group01/gateway/comission";
//		String topic2Old = "gateway/commission";
//		String topic2 = "$share/group01/gateway/commission"; // right spelling: commission

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
//		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputCommissionChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputCommissionChannel")
	public MessageHandler handlerCommission() {
		return (MessageHandler) new CommissionHandler();
	}
}
