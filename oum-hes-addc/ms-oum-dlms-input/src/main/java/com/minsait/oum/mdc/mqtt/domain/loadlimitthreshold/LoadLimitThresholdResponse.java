package com.minsait.oum.mdc.mqtt.domain.loadlimitthreshold;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.mqtt.domain.Response;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadLimitThresholdResponse extends Response {
    @Getter
    @JsonProperty("devices")
    private List<LoadLimitThresholdDevice> devices;
}
