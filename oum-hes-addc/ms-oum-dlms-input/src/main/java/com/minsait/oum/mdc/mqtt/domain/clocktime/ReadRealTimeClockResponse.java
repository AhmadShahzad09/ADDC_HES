package com.minsait.oum.mdc.mqtt.domain.clocktime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.mqtt.domain.Response;
import com.minsait.oum.mdc.mqtt.domain.loadlimitthreshold.LoadLimitThresholdDevice;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReadRealTimeClockResponse extends Response {
    @Getter
    @JsonProperty("device")
    private List<LoadLimitThresholdDevice> devices;
}
