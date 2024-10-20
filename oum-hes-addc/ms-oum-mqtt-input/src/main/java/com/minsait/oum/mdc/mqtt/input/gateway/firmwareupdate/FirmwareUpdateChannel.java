package com.minsait.oum.mdc.mqtt.input.gateway.firmwareupdate;

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
public class FirmwareUpdateChannel {
    @Autowired
    private MqttPahoClientFactory mqttPahoClientFactory;

    @Value("${mqtt.topics.gateway.firmware-update}")
    private String topic;

    @Bean
    public MessageChannel mqttInputFirmwareUpdateChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inboundFirmwareUpdate() {

        String clientID = UUID.randomUUID().toString();

        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientID, mqttPahoClientFactory, topic);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputFirmwareUpdateChannel());

        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputFirmwareUpdateChannel")
    public MessageHandler handlerFirmwareUpdate() {
        return new FirmwareUpdateHandler();
    }
}