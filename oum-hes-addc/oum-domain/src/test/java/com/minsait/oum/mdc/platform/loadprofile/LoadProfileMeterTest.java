package com.minsait.oum.mdc.platform.loadprofile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

public class LoadProfileMeterTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serializeLoadProfileMeterOK() throws JsonProcessingException {
        String loadProfileMeter = objectMapper.writeValueAsString(buildLoadProfileMeter());
        String json = createLoadProfileMeterJson();
        assertEquals(json, loadProfileMeter);
    }

    private LoadProfileMeter buildLoadProfileMeter() {
        return LoadProfileMeter.builder()
                .quality(Double.toString(20.5))
                .day(1657625215)
                .value(1234)
                .measurementIntervalCode("MIN15")
                .magnitudeCode("+A")
                .unitM("kWh")
                .obisCode("1.0.1.8.2.255")
                .build();
    }

    private String createLoadProfileMeterJson() {
        return  "{" +
                "\"quality\":\"20.5\"," +
                "\"day\":1657625215," +
                "\"value\":1234.0," +
                "\"measurementIntervalCode\":\"MIN15\"," +
                "\"magnitudeCode\":\"+A\"" +
//                "\"unitM\":\"kWh\"," +
//                "\"obiscode\":\"1.0.1.8.2.255\"" +
                "}";
    }
}
