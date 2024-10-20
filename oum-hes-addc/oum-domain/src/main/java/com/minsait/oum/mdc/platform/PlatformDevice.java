package com.minsait.oum.mdc.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PlatformDevice {
    @JsonProperty
    private List<PlatformMeter> meterCollector;
    @JsonProperty
    private String pid;
    @JsonProperty
    private Long taskId;
    @JsonProperty
    private Long orderId;
    @JsonProperty
    private Long requestId;
    @JsonProperty
    private String meterId;
    @JsonProperty
    private String deviceId;
    @JsonProperty
    private String deviceType;
}
