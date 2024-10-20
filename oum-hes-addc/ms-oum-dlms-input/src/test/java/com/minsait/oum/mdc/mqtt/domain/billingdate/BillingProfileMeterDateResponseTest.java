package com.minsait.oum.mdc.mqtt.domain.billingdate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({BillingDateResponse.class, ObjectMapper.class})
public class BillingProfileMeterDateResponseTest {

    @Autowired
    private BillingDateResponse billingDateResponse;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void mapBillingDateResponseOK() throws JsonProcessingException {
        BillingDateResponse billingDateResponse = objectMapper.readValue(createBillingDateResponseJson(), BillingDateResponse.class);
        assertEquals(billingDateResponse.getIdRequest(), "84cd4f87-958f-49fe-5au6-e4089b475555");
    }

    private String createBillingDateResponseJson() {
        String billingDateResponseJson = "{\n" +
                "        \"idRequest\": \"84cd4f87-958f-49fe-5au6-e4089b475555\",\n" +
                "        \"time\": 1657625215,\n" +
                "        \"devices\": [\n" +
                "           {\n" +
                "               \"serialNumber\": \"109017135\",\n" +
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
        return billingDateResponseJson;
    }
}
