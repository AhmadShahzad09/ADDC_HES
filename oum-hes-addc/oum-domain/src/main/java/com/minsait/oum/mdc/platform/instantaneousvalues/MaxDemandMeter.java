package com.minsait.oum.mdc.platform.instantaneousvalues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@Jacksonized
@Getter
public class MaxDemandMeter extends InstantValuesMeter {
    @JsonProperty
    private String maxDemandTime;
}
