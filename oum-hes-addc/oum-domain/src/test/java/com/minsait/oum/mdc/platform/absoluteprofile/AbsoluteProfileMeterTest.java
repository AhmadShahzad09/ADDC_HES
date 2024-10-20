package com.minsait.oum.mdc.platform.absoluteprofile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

public class AbsoluteProfileMeterTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serializeLoadProfileMeterOK() throws JsonProcessingException {
        String absoluteProfileMeter = objectMapper.writeValueAsString(buildAbsoluteProfileMeter());
        String json = createAbsoluteProfileMeterJson();
        assertEquals(json, absoluteProfileMeter);
    }

    private AbsoluteProfileMeter buildAbsoluteProfileMeter() {
        return AbsoluteProfileMeter.builder()
                .quality(Double.toString(20.5))
                .day(1657625215)
                .value(1234)
                .measurementIntervalCode("MIN30")
                .magnitudeCode("+A")
                .unitM("kWh")
                .obisCode("1.0.1.8.2.255")
                .build();
    }

    private String createAbsoluteProfileMeterJson() {
        return  "{" +
                "\"quality\":\"20.5\"," +
                "\"day\":1657625215," +
                "\"value\":1234.0," +
                "\"measurementIntervalCode\":\"MIN30\"," +
                "\"magnitudeCode\":\"+A\"" +
//                "\"unitM\":\"kWh\"," +
//                "\"obiscode\":\"1.0.1.8.2.255\"" +
                "}";
    }
}
