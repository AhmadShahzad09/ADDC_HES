package com.minsait.oum.mdc.mqtt.input.clearalarms;

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
public class ClearAlarmsChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${mqtt.topics.on-demand.clear-alarms}")
	private String topic;

	@Bean
	public MessageChannel mqttInputClearAlarmsChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundClearAlarmss() {

		String clientID = "clearalarmsSubscriber" + UUID.randomUUID().toString();
		
//		String topicOld = "meter/clearalarms";
//		String topic = "$share/group01/meter/clearalarms";
		
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputClearAlarmsChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputClearAlarmsChannel")
	public MessageHandler handlerClearAlarms() {
		return new ClearAlarmsHandler();
	}
}
