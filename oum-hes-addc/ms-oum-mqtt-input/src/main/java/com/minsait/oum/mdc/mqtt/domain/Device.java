package com.minsait.oum.mdc.mqtt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class Device {
    @Getter
    @JsonProperty
    private String serialNumber;
}
