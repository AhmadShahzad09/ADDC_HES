package com.minsait.oum.mdc.tcp.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.minsait.oum.mdc.tcp.server.handler.MessageService;

@MessageEndpoint
public class TcpServerEndpoint {

    private MessageService messageService;

    @Autowired
    public TcpServerEndpoint(MessageService messageService) {
        this.messageService = messageService;
    }

    @ServiceActivator(inputChannel = "onAlarmInboundChannel")
    public byte[] onAlarmProcess(byte[] message) {
        return messageService.processMessage(message);
    }

    @ServiceActivator(inputChannel = "onConnectivityInboundChannel")
    public byte[] onConnectivityProcess(byte[] message) {
        return messageService.processMessage(message);
    }

    @ServiceActivator(inputChannel = "onInstallationInboundChannel")
    public byte[] onInstallationProcess(byte[] message) {
        return messageService.processMessage(message);
    }

    @ServiceActivator(inputChannel = "onPowerDownInboundChannel")
    public byte[] onPowerDownProcess(byte[] message) {
        return messageService.processMessage(message);
    }

}
