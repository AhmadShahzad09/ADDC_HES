package com.minsait.oum.mdc.mqtt.input.billingprofile;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BillingProfileAdapter extends AbstractMessageAdapter {


    @Autowired
    private Gson gson;

    @Override
    public ResponseType getResponseType() {
        return ResponseType.BILLING_PROFILE;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException {

        this.parseDevices(request, requestJson, "meterReadingList", deviceJsonElement -> {
            Device device = this.getDevice(deviceJsonElement);
            device.setMeasures(
                    this.getMeasures(deviceJsonElement.getAsJsonObject().get("intervalBlocks").getAsJsonArray()));

            return device;
        });

        return request;
    }

    private Device getDevice(JsonElement jsonDevice) throws MessageAdapterException {
        Device result = new Device();

        JsonObject jsonObject = gson.fromJson(jsonDevice.toString(), JsonObject.class);

        result.setModel(jsonObject.get("modelname").getAsString());
        try {
            result.setOwner(jsonObject.get("owner").getAsString());
        } catch (Exception e) {

        }
        if (jsonObject.get("devicename").getAsString() == null
                || jsonObject.get("devicename").getAsString().length() == 0) {
            result.setName(jsonObject.get("serialNumber").getAsString());
            result.setSerialNumber(jsonObject.get("serialNumber").getAsString());
        } else {
            result.setName(jsonObject.get("devicename").getAsString());
            result.setSerialNumber(jsonObject.get("devicename").getAsString());
        }
        result.setVersion(jsonObject.get("version").getAsString());
        result.setIp(jsonObject.get("ipaddr").getAsString());
        JsonElement deviceError = jsonObject.get("deviceErrorMessage");
        if (deviceError != null) {
            result.setErrorMessage(deviceError.getAsString());
        }
        JsonElement maintenanceCode = jsonObject.get("maintenanceMode");
        if (maintenanceCode != null) {
            result.setInMaintenanceMode(maintenanceCode.getAsBoolean());
        }
        MediumType medium = MediumTypeConverter.convert(jsonObject.get("medium").getAsString());
        result.setMedium(medium);

//		try {
//			IntervalType interval = IntervalTypeConverter.convert(jsonObject.get("interval").getAsString());
//			result.setInterval(interval);
//		} catch (Exception e) {
//			DeviceException dex = new DeviceException("Time interval NULL", result, e);
//			throw dex;
//		}

        return result;
    }

    private List<Measure> getMeasures(JsonArray jsonMeasures) {

        List<Measure> result = new ArrayList<>();

        for (JsonElement jsonMeasure : jsonMeasures) {

            Measure measure = this.getMeasure(jsonMeasure);
            result.add(measure);
        }

        return result;
    }

    private Measure getMeasure(JsonElement jsonMeasure) {

        Measure result = new Measure();
        JsonObject jsonObject = gson.fromJson(jsonMeasure.toString(), JsonObject.class);
        List<Measure.Block> blocks = new ArrayList<Measure.Block>();
        if (jsonObject.get("profileStatus") != null && !jsonObject.get("profileStatus").getAsString().isEmpty()) {
            result.setProfileStatus(jsonObject.get("profileStatus").getAsString());
        }
        if (jsonObject.get("captureTime") != null)
            result.setCaptureTime(jsonObject.get("captureTime").getAsString());

        JsonArray jsonBlocks = jsonObject.get("blocks").getAsJsonArray();
        for (JsonElement jsonBlock : jsonBlocks) {
            Measure.Block block = new Measure.Block();
            JsonObject jsonBlockObject = gson.fromJson(jsonBlock.toString(), JsonObject.class);

            block.setTimestamp(jsonBlockObject.get("time") != null ? jsonBlockObject.get("time").getAsLong() : 0);
            block.setValue(jsonBlockObject.get("value") != null ? jsonBlockObject.get("value").getAsDouble() : 0.0);
            String channel = jsonBlockObject.get("channel") != null ? jsonBlockObject.get("channel").getAsString() : "";
            if ("IR".equals(channel)) {
                block.setChannel("Q1");
            } else if ("ER".equals(channel)) {
                block.setChannel("Q4");
            } else {
                block.setChannel(channel);
            }
            block.setMagnitude(jsonBlockObject.get("unitM") != null ? jsonBlockObject.get("unitM").getAsString() : "");
            block.setCode(jsonBlockObject.get("code") != null ? jsonBlockObject.get("code").getAsString() : "");
            if (jsonBlockObject.get("maxDemandTime") != null && !jsonBlockObject.get("maxDemandTime").getAsString().isEmpty()) {
                block.setMaxDemandTime(jsonBlockObject.get("maxDemandTime").getAsLong());
            }
            block.setTariff(jsonBlockObject.get("tariff") != null ? jsonBlockObject.get("tariff").getAsString() : "");
            block.setType(jsonBlockObject.get("type") != null ? jsonBlockObject.get("type").getAsString() : "");
            blocks.add(block);
        }
        result.getBlocks().addAll(blocks);

        return result;
    }

}
