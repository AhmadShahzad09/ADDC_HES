package com.minsait.oum.mdc.platform.instantaneousvalues;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@RunWith(SpringJUnit4ClassRunner.class)
public class MaxDemandMeterTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serializeMaxDemandMeterOK() throws JsonProcessingException {
        String maxDemandMeter = objectMapper.writeValueAsString(buildMaxDemandMeter());
        String json = createMaxDemandMeterJson();
        assertEquals(json, maxDemandMeter);
    }

    private MaxDemandMeter buildMaxDemandMeter() {
        return MaxDemandMeter.builder()
                .quality(Double.toString(20.5))
                .day(1657625215)
                .value(1685)
                .magnitudeCode("IA_P_L2")
                .measurementIntervalCode("MIN30")
                .unitM("kWh")
                .obisCode("1.0.1.7.0.255")
                .maxDemandTime("1657282450")
                .build();
    }

    private String createMaxDemandMeterJson() {
        return  "{" +
                "\"quality\":\"20.5\"," +
                "\"day\":1657625215," +
                "\"value\":1685.0," +
                "\"magnitudeCode\":\"IA_P_L2\"," +
                "\"measurementIntervalCode\":\"MIN30\"," +
//                "\"unitM\":\"kWh\"," +
                "\"maxDemandTime\":\"1657282450\"" +
//                "\"obiscode\":\"1.0.1.7.0.255\"" +
                "}";
    }
}
