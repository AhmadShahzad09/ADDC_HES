package com.minsait.oum.mdc.mqtt.input.firmwareupgradestatus;

import com.google.gson.Gson;
import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class GetFirmwareUpgradeStatusHandler extends AbstractMessageHandlerProcessor {

    @Autowired
    private GetFirmwareUpgradeStatusAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {

        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> {
                	
                	Map<String, Object> map = new HashMap<>();
                	map.put("serialNumber", device.getSerialNumber());
                	map.put("imageTransferEnable", device.getImageTransferEnable());
                	map.put("imageTransferStatus", device.getImageTransferStatus());
                	map.put("moduleFirmwareVersion", device.getModuleFirmwareVersion());
                	map.put("coreFirmwareVersion", device.getCoreFirmwareVersion());
                	map.put("upgradeCounter", device.getUpgradeCounter());
                	map.put("upgradeTime", device.getUpgradeTime());
                	
                	
                	this.getMdmRestClient().setTelemetryRemoteConfig(
                            TelemetryRemoteConfigurationVO.from(device, "FirmwareUpgradeStatus", new Gson().toJson(map)));
                });

    }
}
