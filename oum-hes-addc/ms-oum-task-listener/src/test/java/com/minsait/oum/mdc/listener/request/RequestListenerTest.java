package com.minsait.oum.mdc.listener.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsait.com.oum.mdc.repository.CallDomain;
import com.minsait.com.oum.mdc.repository.CallGroupDomain;
import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.data.model.CallGroup;
import com.minsait.oum.mdc.driver.Message;
import com.minsait.oum.mdc.publisher.EtisalatMessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({com.minsait.oum.mdc.listener.request.RequestListener.class, com.minsait.oum.mdc.publisher.EtisalatDriver.class})
public class RequestListenerTest {

    @MockBean
    private CallDomain callDomain;

    @MockBean
    private CallGroupDomain groupCallDomain;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private EtisalatMessageService messageService;

    @Autowired
    private RequestListener requestListener;

    private String message;

    @Before
    public void prepareMessage() {
        message = "{\n" +
                "  \"name\": \"Ondemand Test_I1C17D002465-3\",\n" +
                "  \"datetime\": \"1655715540087\",\n" +
                "  \"executionType\": \"MANUAL\",\n" +
                "  \"protocol\": {},\n" +
                "  \"communication\": {\n" +
                "    \"channelProperties\": null,\n" +
                "    \"properties\": [\n" +
                "      {\n" +
                "        \"category\": null,\n" +
                "        \"name\": \"PORT\",\n" +
                "        \"value\": \"5683\",\n" +
                "        \"valueOptions\": null,\n" +
                "        \"valuePattern\": null\n" +
                "      },\n" +
                "      {\n" +
                "        \"category\": null,\n" +
                "        \"name\": \"IP\",\n" +
                "        \"value\": \"10.170.3.4\",\n" +
                "        \"valueOptions\": null,\n" +
                "        \"valuePattern\": null\n" +
                "      }\n" +
                "    ],\n" +
                "    \"type\": \"TCP/IP\"\n" +
                "  },\n" +
                "  \"idGroup\": \"90642\",\n" +
                "  \"commanded\": false,\n" +
                "  \"synchronous\": false,\n" +
                "  \"tasks\": [\n" +
                "    {\n" +
                "      \"id\": \"0\",\n" +
                "      \"deviceId\": \"13466\",\n" +
                "      \"deviceName\": \"I1C17D002465\",\n" +
                "      \"driverClass\": \"Minsait_Etisalat_1\",\n" +
                "      \"protocol\": {},\n" +
                "      \"order\": [\n" +
                "        {\n" +
                "          \"id\": \"0\",\n" +
                "          \"name\": \"OnDemandAbsoluteProfile\",\n" +
                "          \"ordersParams\": {},\n" +
                "          \"executionId\": \"90643\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"idDC\": \"6a0842fb-89e1-4fee-8c90-b5fbe1dbc529\"\n" +
                "}";
    }

    @DisplayName("Happy flow for sending a message")
    @Test
    public void requestMessageIsSent() throws IOException {
        Call callExample = mapFromJson(message, Call.class);
        when(callDomain.save(any(Call.class))).thenReturn(callExample);
        doNothing().when(messageService).send(any(Message.class));
        requestListener.handlerTaskInput(message);
        verify(messageService, times(1)).send(any(Message.class));
    }

    @DisplayName("Call domain repository is called")
    @Test
    public void callRepositoryIsCalled() throws IOException {
        Call callExample = mapFromJson(message, Call.class);
        when(callDomain.save(any(Call.class))).thenReturn(callExample);
        doNothing().when(messageService).send(any(Message.class));
        requestListener.handlerTaskInput(message);
        verify(callDomain, times(1)).save(any(Call.class));
    }

    @DisplayName("CallGroup domain repository is called")
    @Test
    public void callGroupRepositoryIsCalled() throws IOException {
        Call callExample = mapFromJson(message, Call.class);
        when(callDomain.save(any(Call.class))).thenReturn(callExample);
        doNothing().when(messageService).send(any(Message.class));
        requestListener.handlerTaskInput(message);
        verify(groupCallDomain, times(1)).save(any(CallGroup.class));
    }

    @DisplayName("What happen when a message is empty...")
    @Test(expected = NullPointerException.class)
    public void handlerRequestInputEmptyMessage() {
        requestListener.handlerTaskInput("");
    }

    private <T> T mapFromJson(String json, Class<T> clazz)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
