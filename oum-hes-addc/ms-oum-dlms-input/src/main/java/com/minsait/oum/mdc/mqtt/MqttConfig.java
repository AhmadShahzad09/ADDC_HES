package com.minsait.oum.mdc.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import com.google.gson.Gson;

@Configuration
public class MqttConfig {

	@Value("${mqtt.server.ip}")
	private String mqttIp;

	@Value("${mqtt.server.user}")
	private String mqttUser;

	@Value("${mqtt.server.password}")
	private String mqttPassword;

	@Value("${mqtt.server.inflight}")
	private String inflight;

	@Bean
	public MqttPahoClientFactory mqttPahoClientFactory() {
		DefaultMqttPahoClientFactory mqttPahoClientFactory = new DefaultMqttPahoClientFactory();

		MqttConnectOptions options = new MqttConnectOptions();
		String[] urls = new String[1];
		urls[0] = new String("tcp://" + mqttIp);
		options.setServerURIs(urls);
		options.setUserName(mqttUser);
		options.setMaxInflight(Integer.valueOf(inflight));
		options.setPassword(mqttPassword.toCharArray());
        options.setCleanSession(false);
        options.setAutomaticReconnect(true);
		mqttPahoClientFactory.setConnectionOptions(options);

		return mqttPahoClientFactory;
	}

//	@Bean
//	public ModelMapper modelMapper() {
//		ModelMapper modelMapper = new ModelMapper();
//		return modelMapper;
//	}

	@Bean
	public Gson gson() {
		Gson gson = new Gson();
		return gson;
	}

}
