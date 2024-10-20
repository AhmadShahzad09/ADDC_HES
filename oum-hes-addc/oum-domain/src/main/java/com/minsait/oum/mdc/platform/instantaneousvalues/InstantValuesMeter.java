package com.minsait.oum.mdc.platform.instantaneousvalues;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.platform.PlatformMeter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@Jacksonized
@Getter
public class InstantValuesMeter extends PlatformMeter {
    @JsonProperty
    private long day;
    @JsonProperty
    private double value;
    @JsonProperty
    private String magnitudeCode;
    @Setter
    @JsonProperty
    private String measurementIntervalCode;
    @JsonIgnore
//    @JsonProperty
    private String unitM;
    @JsonIgnore
//    @JsonProperty("obiscode")
    private String obisCode;
}
