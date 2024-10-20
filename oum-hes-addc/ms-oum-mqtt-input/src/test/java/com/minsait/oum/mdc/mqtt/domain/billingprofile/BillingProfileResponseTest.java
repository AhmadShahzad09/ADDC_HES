package com.minsait.oum.mdc.mqtt.domain.billingprofile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({BillingProfileResponse.class, com.fasterxml.jackson.databind.ObjectMapper.class})
public class BillingProfileResponseTest {

    @Autowired
    private BillingProfileResponse billingProfileResponse;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void mapBillingProfileResponseOK() throws JsonProcessingException {
        BillingProfileResponse billingProfileResponse = objectMapper.readValue(createBillingResponseJson(), BillingProfileResponse.class);
        assertEquals(billingProfileResponse.getIdRequest(), "84cd4f87-958f-49fe-5au6-e4089b4735d6");

    }

    @Test
    public void mapBillingProfileResponseIntervalBlocksEmptyOK() throws JsonProcessingException {
        BillingProfileResponse billingProfileResponse = objectMapper.readValue(createBillingResponseIntervalBlocksEmptyJson(), BillingProfileResponse.class);
        assertEquals(billingProfileResponse.getIdRequest(), "84cd4f87-958f-49fe-5au6-e4089b4735d6");
    }

    private String createBillingResponseJson() {
        String billingResponseJson = "{\n" +
                "        \"idRequest\": \"84cd4f87-958f-49fe-5au6-e4089b4735d6\",\n" +
                "        \"time\": 1657625215,\n" +
                "        \"statusCode\": \"OK\",\n" +
                "        \"errorMessage\": \"\",\n" +
                "        \"meterReadingList\": [\n" +
                "          {\n" +
                "              \"modelname\": \"PIPE20DUODLMB\",\n" +
                "              \"medium\": \"ELECTRICITY\",\n" +
                "              \"devicename\": \"I11D18D000471\",\n" +
                "              \"version\": \"1.3.0\",\n" +
                "              \"serialNumber\": \"109017135\",\n" +
                "              \"ipaddr\": \"10.170.4.13\",\n" +
                "              \"maintenanceMode\": \"\",\n" +
                "              \"deviceErrorMessage\": \"\",\n" +
                "              \"profileCode\": \"0.0.98.1.0.255\",\n" +
                "              \"intervalBlocks\": [\n" +
                "                {\n" +
                "                \"profileStatus\": \"30\",\n" +
                "                    \"blocks\": [\n" +
                "                {\n" +
                "                    \"time\": \"1657625215\",\n" +
                "                        \"value\": \"1234\",\n" +
                "                        \"unitM\": \"kWh\",\n" +
                "                        \"channel\": \"+A\",\n" +
                "                        \"code\": \"1.0.1.8.0.255\",\n" +
                "                        \"maxDemandTime\": \"\",\n" +
                "                        \"tariff\": \"\",\n" +
                "                        \"type\": \"\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"time\": \"1657625215\",\n" +
                "                        \"value\": \"0\",\n" +
                "                        \"unitM\": \"kWh\",\n" +
                "                        \"channel\": \"-A\",\n" +
                "                        \"code\": \"1.0.2.8.0.255\",\n" +
                "                        \"maxDemandTime\": \"\",\n" +
                "                        \"tariff\": \"\",\n" +
                "                        \"type\": \"\"\n" +
                "                },\n" +
                "            {\n" +
                "                \"time\": \"1657622644\",\n" +
                "                    \"value\": \"123\",\n" +
                "                    \"unitM\": \"w\",\n" +
                "                    \"channel\": \"+MDA\",\n" +
                "                    \"code\": \"1.0.1.6.0.255\",\n" +
                "                    \"maxDemandTime\": \"1655881900\",\n" +
                "                    \"tariff\": \"\",\n" +
                "                    \"type\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"time\": \"1657622644\",\n" +
                "                    \"value\": \"10\",\n" +
                "                    \"unitM\": \"w\",\n" +
                "                    \"channel\": \"-MDA\",\n" +
                "                    \"code\": \"1.0.2.6.0.255\",\n" +
                "                    \"maxDemandTime\": \"1655622700\",\n" +
                "                    \"tariff\": \"\",\n" +
                "                    \"type\": \"\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return billingResponseJson;
    }

    private String createBillingResponseIntervalBlocksEmptyJson() {
        String billingResponseJson = "{\n" +
                "        \"idRequest\": \"84cd4f87-958f-49fe-5au6-e4089b4735d6\",\n" +
                "        \"time\": 1657625215,\n" +
                "        \"statusCode\": \"OK\",\n" +
                "        \"errorMessage\": \"\",\n" +
                "        \"meterReadingList\": [\n" +
                "          {\n" +
                "              \"modelname\": \"PIPE20DUODLMB\",\n" +
                "              \"medium\": \"ELECTRICITY\",\n" +
                "              \"devicename\": \"I11D18D000471\",\n" +
                "              \"version\": \"1.3.0\",\n" +
                "              \"serialNumber\": \"109017135\",\n" +
                "              \"ipaddr\": \"10.170.4.13\",\n" +
                "              \"maintenanceMode\": \"\",\n" +
                "              \"deviceErrorMessage\": \"\",\n" +
                "              \"profileCode\": \"0.0.98.1.0.255\",\n" +
                "              \"intervalBlocks\": []\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return billingResponseJson;
    }

}
