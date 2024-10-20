package com.minsait.oum.mdc.mqtt.input.getbillingdate;

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
public class BillingDateAdapter extends AbstractMessageAdapter {

//    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyyHH:mm:ss");
//    private static final ZoneId zoneId = ZoneId.of("UTC");

    @Override
    public ResponseType getResponseType() {
        return ResponseType.GET_BILLING_DATE;
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
//            long period = Long.valueOf(jsonObject.get("billingProfile_Period").getAsLong()) * 1000;
//            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(period), zoneId);
//            device.setBillingDate(localDateTime.format(dateTimeFormatter));
            device.setBillingDate(jsonObject.get("billingProfile_Period").getAsString());

            return device;
        });

        return request;
    }


//    @Autowired
//    private ObjectMapper objectMapper;
//
//    public BillingDateResponse convert(final String jsonResponse) throws IOException {
//        log.debug("BillingProfileHandler -> Going to convert json message into Request");
//        String normalizedJsonRequest = jsonResponse.replaceAll("\\^M", "").replaceAll("\\^", "");
//        log.info("Normalized Json Request: " + normalizedJsonRequest);
//        BillingDateResponse billingDateResponse = objectMapper.readValue(jsonResponse, BillingDateResponse.class);
//
//        return billingDateResponse;
//    }

}
