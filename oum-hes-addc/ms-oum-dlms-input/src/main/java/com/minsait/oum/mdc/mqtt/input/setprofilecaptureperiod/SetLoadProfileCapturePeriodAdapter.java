package com.minsait.oum.mdc.mqtt.input.setprofilecaptureperiod;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import org.springframework.stereotype.Service;

@Service
public class SetLoadProfileCapturePeriodAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.SET_LOAD_PROFILE_CAPTURE_PERIOD;
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
            if (hasJsonObject(jsonObject,"LoadProfile1")) device.setLoadProfile1CapturePeriod(jsonObject.get("LoadProfile1").getAsString());
            if (hasJsonObject(jsonObject,"LoadProfile2")) device.setLoadProfile2CapturePeriod(jsonObject.get("LoadProfile2").getAsString());
            if (hasJsonObject(jsonObject,"PowerQualityProfile")) device.setPowerQualityProfileCapturePeriod(jsonObject.get("PowerQualityProfile").getAsString());
            if (hasJsonObject(jsonObject,"InstrumentationProfile")) device.setInstrumentationProfileCapturePeriod(jsonObject.get("InstrumentationProfile").getAsString());
            return device;
        });
        return request;
    }

    public boolean hasJsonObject(final JsonObject jsonObject, final String property) {
        return jsonObject.has(property) && !(jsonObject.get(property) instanceof JsonNull);
    }

}
