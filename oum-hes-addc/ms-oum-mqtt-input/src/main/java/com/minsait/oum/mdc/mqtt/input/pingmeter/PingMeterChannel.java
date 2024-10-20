package com.minsait.oum.mdc.mqtt.input.pingmeter;

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
public class PingMeterChannel {

    @Autowired
    private MqttPahoClientFactory mqttPahoClientFactory;

    @Value("${mqtt.topics.on-demand.meter-ping}")
    private String topic;

    @Bean
    public MessageChannel mqttInputPingMeterChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inboundPingMeter() {
        String clientID = UUID.randomUUID().toString();
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputPingMeterChannel());

        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputPingMeterChannel")
    public MessageHandler handlerPingMeter() {
        return new PingMeterHandler();
    }
}
