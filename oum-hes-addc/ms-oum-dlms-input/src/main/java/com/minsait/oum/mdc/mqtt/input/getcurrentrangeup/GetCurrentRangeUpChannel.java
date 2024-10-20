package com.minsait.oum.mdc.mqtt.input.getcurrentrangeup;

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
public class GetCurrentRangeUpChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;

	@Value("${mqtt.topics.on-demand.get-current-range-up}")
	private String topic;

	@Bean
	public MessageChannel mqttInputGetCurrentRangeUpChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundGetCurrentRangeUp() {

		String clientID = "getCurrentRangeUp" + UUID.randomUUID().toString();

//		String topicOld = "meter/switchstatus";
//		String topic = "$share/group01/meter/switchstatus";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID,
				mqttPahoClientFactory, topic);
		// adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputGetCurrentRangeUpChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputGetCurrentRangeUpChannel")
	public MessageHandler handlerGetCurrentRangeUp() {
		return new GetCurrentRangeUpHandler();
	}
}