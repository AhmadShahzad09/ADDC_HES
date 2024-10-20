package com.minsait.oum.mdc.mqtt.input.nameplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
public class ReadNamePlateAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.READ_NAME_PLATE_INFO_METER;
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
			device.setDeviceID(jsonObject.get("deviceID").getAsString());
			device.setManufacturerName(jsonObject.get("manufacturerName").getAsString());
			device.setCtNumerator(jsonObject.get("ctNumerator").getAsString());
			device.setCtDenominator(jsonObject.get("ctDenominator").getAsString());
			device.setVtNumerator(jsonObject.get("vtNumerator").getAsString());
			device.setVtDenominator(jsonObject.get("vtDenominator").getAsString());
			device.setCtRatio(jsonObject.get("ctRatio").getAsString());
			device.setVtTransferRatio(jsonObject.get("vtTransferRatio").getAsString());
			device.setCurrentRating(jsonObject.get("currentRating").getAsString());
			device.setYearOfManufacture(jsonObject.get("yearOfManufacture").getAsString());
			device.setFirmwareVersion(jsonObject.get("firmwareVersion").getAsString());
			device.setAddress(jsonObject.get("address").getAsString());
			device.setTotaliserSerialNumber(jsonObject.get("totaliserSerialNumber").getAsString());
			device.setTranducerSerialNumber(jsonObject.get("tranducerSerialNumber").getAsString());
			device.setManufacturerCode(jsonObject.get("manufacturerCode").getAsString());
			device.setType(jsonObject.get("type").getAsString());

			return device;
		});

        return request;
    }
}
