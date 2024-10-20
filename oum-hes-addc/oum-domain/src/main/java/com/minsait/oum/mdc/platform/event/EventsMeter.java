package com.minsait.oum.mdc.platform.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.platform.PlatformMeter;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@Jacksonized
@Getter
public class EventsMeter extends PlatformMeter {
    @JsonProperty
    private long eventDateTime;
    @JsonProperty
    private String eventCode;
}
