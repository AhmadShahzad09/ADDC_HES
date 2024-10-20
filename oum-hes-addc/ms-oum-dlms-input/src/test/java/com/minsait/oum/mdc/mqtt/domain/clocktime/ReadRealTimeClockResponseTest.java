package com.minsait.oum.mdc.mqtt.domain.clocktime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ReadRealTimeClockResponse.class, ObjectMapper.class})
public class ReadRealTimeClockResponseTest {
    @Autowired
    private ReadRealTimeClockResponse readRealTimeClockResponse;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void mapReadRealTimeClockResponseOK() throws JsonProcessingException {
        ReadRealTimeClockResponse readRealTimeClockResponse = objectMapper.readValue(createReadRealTimeClockResponseJson(), ReadRealTimeClockResponse.class);
        assertEquals(readRealTimeClockResponse.getIdRequest(), "84cd4f87-958f-49fe-5au6-e4089b475555");
    }

    private String createReadRealTimeClockResponseJson() {
        String readRealTimeClockResponseJson = "{\n" +
                "        \"idRequest\": \"84cd4f87-958f-49fe-5au6-e4089b475555\",\n" +
                "        \"time\": 1657625215,\n" +
                "        \"device\": [\n" +
                "           {\n" +
                "               \"device\": \"109017135\",\n" +
                "               \"billingProfile_Period\": \"135456346526256\"\n" +
                "           },\n" +
                "           {\n" +
                "               \"serialNumber\": \"109088832\",\n" +
                "               \"billingProfile_Period\": \"135456346526777\"\n" +
                "           },\n" +
                "           {\n" +
                "               \"serialNumber\": \"109999932\",\n" +
                "               \"billingProfile_Period\": \"135456346222277\"\n" +
                "           }\n" +
                "         ]\n" +
                "}";
        return readRealTimeClockResponseJson;
    }

}
