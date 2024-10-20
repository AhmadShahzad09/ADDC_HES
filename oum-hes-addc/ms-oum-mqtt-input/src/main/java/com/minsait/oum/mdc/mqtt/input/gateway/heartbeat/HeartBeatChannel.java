package com.minsait.oum.mdc.mqtt.input.gateway.heartbeat;

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

import com.minsait.mdc.util.AppConstants;

import lombok.extern.slf4j.Slf4j;

@Profile("!test")
@Slf4j
@Configuration
public class HeartBeatChannel {

	@Autowired
	private MqttPahoClientFactory mqttPahoClientFactory;
	
	@Value("${protocol.version}")
	private String protocolVersion;
	
	@Value("${mqtt.topics.gateway.heartbeat}")
	private String topic;

	@Bean
	public MessageChannel mqttInputHeartBeatChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inboundHeartBeat() {

		String clientID = this.getClass().getSimpleName() + "_" + UUID.randomUUID().toString();

//		String topicOld = "gateway/heartbeat";
		//String topic = "$share/group01/gateway/heartbeat";

		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
//		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(2);
		adapter.setOutputChannel(mqttInputHeartBeatChannel());

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputHeartBeatChannel")
	public MessageHandler handlerHeartBeat() {
		log.info("using " + protocolVersion + " version");
		if (AppConstants.PROTOCOL_V09.equalsIgnoreCase(protocolVersion)) {
			log.info("using HeartBeatHandlerV9");
			return (MessageHandler) new com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.v09.HeartBeatHandler();
		}

		log.info("using HeartBeatHandler");
		return (MessageHandler) new com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.HeartBeatHandler();		
	}
}
