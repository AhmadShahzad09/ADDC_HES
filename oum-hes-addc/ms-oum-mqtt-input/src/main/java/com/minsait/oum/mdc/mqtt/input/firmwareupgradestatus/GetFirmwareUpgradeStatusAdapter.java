package com.minsait.oum.mdc.mqtt.input.firmwareupgradestatus;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GetFirmwareUpgradeStatusAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.GET_FIRMWARE_UPGRADE_STATUS;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {
        parseDevices(request, jsonRequest, "devices", jsonElement -> {
            Device device = new Device();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            device.setSerialNumber(jsonObject.get("serialNumber").getAsString());
            device.setImageTransferEnable(jsonObject.get("imageTransferEnable").getAsString());
            device.setImageTransferStatus(jsonObject.get("imageTransferStatus").getAsString());
            device.setModuleFirmwareVersion(jsonObject.get("moduleFirmwareVersion").getAsString());
            device.setCoreFirmwareVersion(jsonObject.get("coreFirmwareVersion").getAsString());
            device.setUpgradeCounter(jsonObject.get("upgradeCounter").getAsString());
            device.setUpgradeTime(jsonObject.get("upgradeTime").getAsString());
            
            return device;
        });

        return request;
    }
}
