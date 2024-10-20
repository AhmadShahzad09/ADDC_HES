package com.minsait.oum.mdc.dlms.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNioServerConnectionFactory;
import org.springframework.messaging.MessageChannel;

import com.indracompany.energy.dlms.cosem.addc.server.GXDLMSPushListener;
import com.indracompany.energy.dlms.cosem.addc.server.INotificationCallback;

@Configuration
public class DlmsServerConfig {
	
	@Autowired
	@Qualifier("onAlarmNotificationCallback")
	private INotificationCallback onAlarmNotificationCallback;
	
	@Autowired
	@Qualifier("onConnectivityNotificationCallback")
	private INotificationCallback onConnectivityNotificationCallback;
	
	@Autowired
	@Qualifier("onInstallationNotificationCallback")
	private INotificationCallback onInstallationNotificationCallback;
	
	@Autowired
	@Qualifier("onPowerDownNotificationCallback")
	private INotificationCallback onPowerDownNotificationCallback;

    @Value("${tcp.server.port.onAlarm}")
    private int onAlarmPort;

    @Value("${tcp.server.port.onConnectivity}")
    private int onConnectivityPort;

    @Value("${tcp.server.port.onInstallation}")
    private int onInstallationPort;

    @Value("${tcp.server.port.onPowerDown}")
    private int onPowerDownPort;
    
	@Bean("onAlarmServerConfig")
	public GXDLMSPushListener onAlarmServerConnectionFactory() {
		try {
			return new GXDLMSPushListener(onAlarmPort, onAlarmNotificationCallback);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
	@Bean("onConnectivityServerConfig")
	public GXDLMSPushListener onConnectivityServerConnectionFactory() {
		try {
			return new GXDLMSPushListener(onConnectivityPort, onConnectivityNotificationCallback);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
	@Bean("onInstallationServerConfig")
	public GXDLMSPushListener onInstallationServerConnectionFactory() {
		try {
			return new GXDLMSPushListener(onInstallationPort, onInstallationNotificationCallback);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
	@Bean("onPowerDownServerConfig")
	public GXDLMSPushListener onPowerDownServerConnectionFactory() {
		try {
			return new GXDLMSPushListener(onPowerDownPort, onPowerDownNotificationCallback);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
