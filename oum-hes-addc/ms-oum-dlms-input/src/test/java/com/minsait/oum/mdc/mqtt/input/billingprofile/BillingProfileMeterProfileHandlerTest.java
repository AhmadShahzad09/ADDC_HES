package com.minsait.oum.mdc.mqtt.input.billingprofile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.minsait.commands.impl.action.AbstractAction;
import com.minsait.mdc.data.model.Call;
import com.minsait.oum.mdc.application.ProfileApplication;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.mqtt.domain.billingprofile.BillingProfileResponse;
import com.minsait.oum.mdc.mqtt.input.exception.MessageParsingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({BillingProfileHandler.class})
public class BillingProfileMeterProfileHandlerTest {

    @MockBean
    private ProfileApplication application;

    @MockBean
    private BillingProfileAdapter adapter;

    @MockBean
    private Message message;

    @MockBean
    private AbstractAction abstractAction;

    @MockBean
    private Gson gson;

    @Autowired
    private BillingProfileHandler billingProfileHandler;

    private String mqttMessage;
    private String callMessage;

    @DisplayName("Happy flow for handler a message")
    @Test
    public void handleMessageHappyFlow() throws MessageParsingException, IOException {
        BillingProfileResponse billingProfileResponse = mapFromJson(mqttMessage, BillingProfileResponse.class);
//        when(adapter.convert(anyString())).thenReturn(billingProfileResponse);
        Request request = mapFromJson(mqttMessage, Request.class);
        when(adapter.convert(anyString())).thenReturn(request);
        Call callExample = mapFromJson(callMessage, Call.class);
        when(application.createCallMonitorEntry(any(Request.class))).thenReturn(callExample);
        when(message.getHeaders()).thenReturn(any());
        when(message.getPayload()).thenReturn(any());
        when(message.getPayload().toString()).thenReturn(createPayloadMessage());
        doNothing().when(abstractAction).executeActions(any(com.minsait.commands.CommandOutput.class));
        billingProfileHandler.handleMessage(message);
        //verify(messageService, times(1)).send(any(com.minsait.oum.mdc.driver.Message.class));
    }

    @Before
    public void createMqttMessage() {
        mqttMessage = "{\n" +
                "     \"idRequest\": \"66658888-958f-49fe-6666-e4089b4735d6\",\n" +
                "     \"time\": \"1657625215\",\n" +
                "     \"statusCode\": \"OK\",\n" +
                "     \"errorMessage\": \"\",\n" +
                "     \"meterReadingList\": [\n" +
                "          {\n" +
                "               \"modelname\": \"PIPE20DUODLMB\",\n" +
                "               \"medium\": \"ELECTRICITY\",\n" +
                "               \"devicename\": \"I11D18D000471\",\n" +
                "               \"version\": \"1.3.0\",\n" +
                "               \"serialNumber\": \"109017135\",\n" +
                "               \"ipaddr\": \"10.170.4.13\",\n" +
                "               \"maintenanceMode\": \"\",\n" +
                "               \"deviceErrorMessage\": \"\",\n" +
                "               \"profileCode\": \"0.0.98.1.0.255\",\n" +
                "               \"intervalBlocks\": [\n" +
                "                    {\n" +
                "                         \"profileStatus\": \"30\",\n" +
                "                         \"blocks\": [\n" +
                "                              {\n" +
                "                                   \"time\": \"1657625222\",\n" +
                "                                   \"value\": \"1234\",\n" +
                "                                   \"unitM\": \"kWh\",\n" +
                "                                   \"channel\": \"+A\",\n" +
                "                                   \"code\": \"1.0.1.8.0.255\",\n" +
                "                                   \"maxDemandTime\": \"\",\n" +
                "                                   \"tariff\": \"\",\n" +
                "                                   \"type\": \"\"\n" +
                "                              },\n" +
                "                              {\n" +
                "                                   \"time\": \"1657625228\",\n" +
                "                                   \"value\": \"0\",\n" +
                "                                   \"unitM\": \"kWh\",\n" +
                "                                   \"channel\": \"-A\",\n" +
                "                                   \"code\": \"1.0.2.8.0.255\",\n" +
                "                                   \"maxDemandTime\": \"\",\n" +
                "                                   \"tariff\": \"\",\n" +
                "                                   \"type\": \"\"\n" +
                "                              }\n" +
                "                         ]\n" +
                "                    }\n" +
                "               ]\n" +
                "          }\n" +
                "     ]\n" +
                "}";
        createCallMessage();
    }

    public void createCallMessage() {
        String mqttMessagePayload = createPayloadMessage();
        callMessage = "{\n" +
                "\t\"status\" : \"RUNNING\",\n" +
                "\t\"priority\" : 0,\n" +
                "\t\"name\" : \"BILLING_PROFILE\",\n" +
                "\t\"direction\" : \"IN\",\n" +
                "\t\"idGroup\" : \"84cf3e97-958y-485e-b046-e4089b4735d6\",\n" +
                "\t\"user\" : NumberLong(\"999\"),\n" +
                "\t\"commanded\" : false,\n" +
                "\t\"synchronous\" : false,\n" +
                "\t\"tasks\" : [\n" +
                "\t\t{\n" +
                "\t\t\t\"_id\" : NumberLong(\"1\"),\n" +
                "\t\t\t\"deviceName\" : \"I11D18D000471\",\n" +
                "\t\t\t\"driverClass\" : \"Minsait_Etisalat_1\",\n" +
                "\t\t\t\"status\" : \"RUNNING\",\n" +
                "\t\t\t\"order\" : [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"_id\" : NumberLong(\"1\"),\n" +
                "\t\t\t\t\t\"name\" : \"OnDemandBillingDataProfile\",\n" +
                "\t\t\t\t\t\"status\" : \"RUNNING\",\n" +
                "\t\t\t\t\t\"payload\" : "+ mqttMessagePayload +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t],\n" +
                "}";
    }

    private String createPayloadMessage() {
        String mqttMessagePayload = mqttMessage.trim();
        mqttMessagePayload = mqttMessagePayload.replaceAll("\\t", "");
        mqttMessagePayload = mqttMessagePayload.replaceAll("\\n", "");

        return mqttMessagePayload;
    }

    private <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
