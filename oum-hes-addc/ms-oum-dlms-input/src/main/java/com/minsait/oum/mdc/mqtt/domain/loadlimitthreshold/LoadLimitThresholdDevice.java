package com.minsait.oum.mdc.mqtt.domain.loadlimitthreshold;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.mqtt.domain.Device;
import lombok.Getter;

public class LoadLimitThresholdDevice extends Device {
    @Getter
    @JsonProperty("thresholdAct")
    private String activeThreshold;

    @Getter
    @JsonProperty("threshDuration")
    private String thresholdDuration;
}
