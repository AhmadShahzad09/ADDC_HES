package com.minsait.oum.mdc.platform.instantaneousvalues;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InstantaneousValuesMeterTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serializeInstantaneousValuesMeterOK() throws JsonProcessingException {
        String instantaneousValuesMeter = objectMapper.writeValueAsString(buildInstantaneousValuesMeter());
        String json = createInstantaneousValuesMeterJson();
        assertEquals(json, instantaneousValuesMeter);
    }

    private InstantaneousValuesMeter buildInstantaneousValuesMeter() {
        return InstantaneousValuesMeter.builder()
                .quality(Double.toString(20.5))
                .day(1657625215)
                .value(1685)
                .magnitudeCode("IA_P_L2")
                .measurementIntervalCode("MIN15")
                .meterCode("E5T10D003004")
                .unitM("kWh")
                .obisCode("1.0.1.7.0.255")
                .build();
    }

    private String createInstantaneousValuesMeterJson() {
        return  "{" +
                "\"quality\":\"20.5\"," +
                "\"day\":1657625215," +
                "\"value\":1685.0," +
                "\"magnitudeCode\":\"IA_P_L2\"," +
                "\"measurementIntervalCode\":\"MIN15\"," +
//                "\"unitM\":\"kWh\"," +
                "\"meterCode\":\"E5T10D003004\"" +
//                "\"obiscode\":\"1.0.1.7.0.255\"" +
                "}";
    }
}
