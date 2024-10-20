package com.minsait.oum.mdc.tcp.server;

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

@Configuration
public class TcpServerConfig {

    @Value("${tcp.server.port.onAlarm}")
    private int onAlarmPort;

    @Value("${tcp.server.port.onConnectivity}")
    private int onConnectivityPort;

    @Value("${tcp.server.port.onInstallation}")
    private int onInstallationPort;

    @Value("${tcp.server.port.onPowerDown}")
    private int onPowerDownPort;
    
//    @Bean("onAlarmServerConfig")
//    public AbstractServerConnectionFactory onAlarmServerConnectionFactory() {
//        TcpNioServerConnectionFactory serverConnectionFactory = new TcpNioServerConnectionFactory(onAlarmPort);
//        serverConnectionFactory.setUsingDirectBuffers(true);
//        return serverConnectionFactory;
//    }    
//    
//    @Bean("onConnectivityServerConfig")
//    public AbstractServerConnectionFactory onConnectivityServerConnectionFactory() {
//        TcpNioServerConnectionFactory serverConnectionFactory = new TcpNioServerConnectionFactory(onConnectivityPort);
//        serverConnectionFactory.setUsingDirectBuffers(true);
//        return serverConnectionFactory;
//    }
//
//    @Bean("onInstallationServerConfig")
//    public AbstractServerConnectionFactory onInstallationServerConnectionFactory() {
//        TcpNioServerConnectionFactory serverConnectionFactory = new TcpNioServerConnectionFactory(onInstallationPort);
//        serverConnectionFactory.setUsingDirectBuffers(true);
//        return serverConnectionFactory;
//    } 
//
//    @Bean("onPowerDownServerConfig")
//    public AbstractServerConnectionFactory onPowerDownServerConnectionFactory() {
//        TcpNioServerConnectionFactory serverConnectionFactory = new TcpNioServerConnectionFactory(onPowerDownPort);
//        serverConnectionFactory.setUsingDirectBuffers(true);
//        return serverConnectionFactory;
//    }
//
//    @Bean("onAlarmInboundChannel")
//    public MessageChannel onAlarmInboundChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean("onConnectivityInboundChannel")
//    public MessageChannel onConnectivityInboundChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean("onInstallationInboundChannel")
//    public MessageChannel onInstallationInboundChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean("onPowerDownInboundChannel")
//    public MessageChannel onPowerDownInboundChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean("onAlarmInboundGateway")
//    public TcpInboundGateway onAlarmInboundGateway(
//    		@Qualifier("onAlarmServerConfig") AbstractServerConnectionFactory serverConnectionFactory,
//    		@Qualifier("onAlarmInboundChannel") MessageChannel inboundChannel) {
//        TcpInboundGateway tcpInboundGateway = new TcpInboundGateway();
//        tcpInboundGateway.setConnectionFactory(serverConnectionFactory);
//        tcpInboundGateway.setRequestChannel(inboundChannel);
//        return tcpInboundGateway;
//    }
//
//    @Bean("onConnectivityInboundGateway")
//    public TcpInboundGateway onConnectivityInboundGateway(
//    		@Qualifier("onConnectivityServerConfig") AbstractServerConnectionFactory serverConnectionFactory,
//    		@Qualifier("onConnectivityInboundChannel") MessageChannel inboundChannel) {
//        TcpInboundGateway tcpInboundGateway = new TcpInboundGateway();
//        tcpInboundGateway.setConnectionFactory(serverConnectionFactory);
//        tcpInboundGateway.setRequestChannel(inboundChannel);
//        return tcpInboundGateway;
//    }
//
//    @Bean("onInstallationInboundGateway")
//    public TcpInboundGateway onInstallationInboundGateway(
//    		@Qualifier("onInstallationServerConfig") AbstractServerConnectionFactory serverConnectionFactory,
//    		@Qualifier("onInstallationInboundChannel") MessageChannel inboundChannel) {
//        TcpInboundGateway tcpInboundGateway = new TcpInboundGateway();
//        tcpInboundGateway.setConnectionFactory(serverConnectionFactory);
//        tcpInboundGateway.setRequestChannel(inboundChannel);
//        return tcpInboundGateway;
//    }
//
//    @Bean("onPowerDownInboundGateway")
//    public TcpInboundGateway onPowerDownInboundGateway(
//    		@Qualifier("onPowerDownServerConfig") AbstractServerConnectionFactory serverConnectionFactory,
//    		@Qualifier("onPowerDownInboundChannel") MessageChannel inboundChannel) {
//        TcpInboundGateway tcpInboundGateway = new TcpInboundGateway();
//        tcpInboundGateway.setConnectionFactory(serverConnectionFactory);
//        tcpInboundGateway.setRequestChannel(inboundChannel);
//        return tcpInboundGateway;
//    }

}
