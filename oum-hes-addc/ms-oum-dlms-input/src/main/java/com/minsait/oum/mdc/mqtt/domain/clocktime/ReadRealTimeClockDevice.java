package com.minsait.oum.mdc.mqtt.domain.clocktime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.mqtt.domain.Device;
import lombok.Getter;

public class ReadRealTimeClockDevice extends Device {
    @Getter
    @JsonProperty
    private String device;

    @Getter
    @JsonProperty
    private long time;

    @Getter
    @JsonProperty
    private long clock;

    @Getter
    @JsonProperty("obisCode")
    private String code;
}
