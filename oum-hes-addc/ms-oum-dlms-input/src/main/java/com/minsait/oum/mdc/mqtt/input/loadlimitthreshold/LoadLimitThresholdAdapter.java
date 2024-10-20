package com.minsait.oum.mdc.mqtt.input.loadlimitthreshold;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.mdc.util.MessageParsingUtils;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.platform.billingprofile.BillingProfileMeter;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import com.minsait.oum.mdc.mqtt.input.exception.MessageParsingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LoadLimitThresholdAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.GET_LOAD_LIMIT_THRESHOLD;
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
            device.setThresholdAct(jsonObject.get("thresholdAct").getAsString());
            device.setThreshDuration(jsonObject.get("threshDuration").getAsString());
            return device;
        });

        return request;
    }

//    @Autowired
//    private ObjectMapper objectMapper;
//
//    public LoadLimitThresholdResponse convert(final String jsonResponse) throws IOException {
//        log.debug("BillingProfileHandler -> Going to convert json message into Request");
//        String normalizedJsonRequest = jsonResponse.replaceAll("\\^M", "").replaceAll("\\^", "");
//        log.info("Normalized Json Request: " + normalizedJsonRequest);
//        LoadLimitThresholdResponse loadLimitThresholdResponse = objectMapper.readValue(jsonResponse, LoadLimitThresholdResponse.class);
//
//        return loadLimitThresholdResponse;
//    }

    //TODO Delete this part when ProfileApplication will be refactored
    @Autowired
    private Gson gson;

    public Request convertToRequest(String jsonRequest) throws MessageParsingException {
        log.debug("LoadLimitThresholdHandler -> Going to convert json message into Request");
        String normalizedJsonRequest = jsonRequest.replaceAll("\\^M", "").replaceAll("\\^", "");
        log.info("Normalized Json Request: " + normalizedJsonRequest);

        JsonObject jsonObjectRequest = gson.fromJson(normalizedJsonRequest, JsonObject.class);
        Request result = this.convertToRequest(jsonObjectRequest);
        result.setResponseType(ResponseType.BILLING_PROFILE);
        result.setRequestType(RequestType.SCHEDULED);
        result.setPayload(jsonRequest);

        JsonArray jsonDevices = jsonObjectRequest.get("devices").getAsJsonArray();
        for (JsonElement jsonDevice : jsonDevices) {

            JsonObject jsonObject = gson.fromJson(jsonDevice.toString(), JsonObject.class);
            Device device = MessageParsingUtils.parseDeviceJson(jsonObject);
            result.getDevices().add(device);
        }

        return result;
    }

    private List<BillingProfileMeter> parseMeasureArrayToBillingList(JsonArray jsonMeasures) throws MessageParsingException {
        log.debug("LoadLimitThresholdHandler -> Going to parseMeasureArrayToLoadLimitThresholdList");
        List<BillingProfileMeter> result = new ArrayList<>();
        for (JsonElement jsonMeasure : jsonMeasures) {
            BillingProfileMeter measure = this.parseSingleBilling(jsonMeasure);
            result.add(measure);
        }
        return result;
    }

    private BillingProfileMeter parseSingleBilling(JsonElement jsonMeasure) throws MessageParsingException {
        log.debug("LoadLimitThresholdHandler -> Going to parseSingleLoadLimitThreshold" + jsonMeasure.toString());

//        Billing result = new Billing();
//
//        try {
//
//            List<Billing.Block> blocks = new ArrayList<>();
//            JsonObject jsonObject = gson.fromJson(jsonMeasure.toString(), JsonObject.class);
//
//            if (jsonObject.get("profileStatus") != null)
//                result.setProfileStatus(jsonObject.get("profileStatus").getAsDouble());
//
//            if (jsonObject.get("captureTime") != null)
//                result.setCaptureTime(jsonObject.get("captureTime").getAsString());
//
//            JsonArray jsonBlocks = jsonObject.get("blocks").getAsJsonArray();
//
//            for (JsonElement jsonBlock : jsonBlocks) {
//
//                Billing.Block block = new Billing.Block();
//                JsonObject jsonBlockObject = gson.fromJson(jsonBlock.toString(), JsonObject.class);
//
//                block.setTimestamp(jsonBlockObject.get("time").getAsLong());
//                block.setValue(jsonBlockObject.get("value").getAsString());
//
//                if (jsonBlockObject.get("type") != null && jsonBlockObject.get("type").getAsString().length() > 0)
//                    block.setType(BillingType.getBillingFromString(jsonBlockObject.get("type").getAsString()));
//
//                if (jsonBlockObject.get("tarrif") != null && jsonBlockObject.get("tarrif").getAsString().length() > 0)
//                    block.setTariff(jsonBlockObject.get("tarrif").getAsString());
//                else
//                    block.setTariff("P0");
//
//                String channel = jsonBlockObject.get("channel").getAsString();
//
//                if ("IR".equals(channel))
//                    channel = "Q1";
//                else if ("ER".equals(channel))
//                    channel = "Q4";
//
//                if (jsonBlockObject.get("maxDemandTime") != null && jsonBlockObject.get("maxDemandTime").getAsString().length() > 0)
//                    block.setMaxDemandTime(jsonBlockObject.get("maxDemandTime").getAsLong());
//
//                block.setChannel(BillingMagnitudeConverter.getBillingMagnitudeConverterByString(channel).getValue());
//                block.setMagnitude(jsonBlockObject.get("unitM").getAsString());
//                block.setCode(jsonBlockObject.get("code").getAsString());
//                blocks.add(block);
//            }
//            result.getBlocks().addAll(blocks);
//        } catch (Exception e) {
//            throw new MessageParsingException(e, "Exception while parsing measure [intervalBlocks] data");
//        }
//        return result;
        return null;
    }

    private Request convertToRequest(JsonObject jsonRequest) {
        Request result = new Request();
        String uuidRequest = jsonRequest.get("idRequest").getAsString();
        result.setIdRequest(uuidRequest);
        try {
            result.setTime(jsonRequest.get("time").getAsLong());
        } catch (Exception e) {

        }
        return result;
    }

}
