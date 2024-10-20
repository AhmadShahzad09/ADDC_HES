package com.minsait.oum.mdc.mqtt.input.alarms;

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
public class EventsChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;

	@Value("${mqtt.topics.meter.events}")
	private String topic;

	@Bean
	public MessageChannel mqttInputAlarmsProfileChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundAlarmsProfile() {

		String clientID = UUID.randomUUID().toString();

//		String topicOld = "meter/events";
//		String topic = "$share/group01/meter/events";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID,
				mqttPahoClientFactory, topic);
//		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputAlarmsProfileChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputAlarmsProfileChannel")
	public MessageHandler handlerAlarmsProfile() {
		return new EventsHandler();
	}
}
