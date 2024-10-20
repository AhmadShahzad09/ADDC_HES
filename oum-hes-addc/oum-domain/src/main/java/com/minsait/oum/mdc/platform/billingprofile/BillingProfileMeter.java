package com.minsait.oum.mdc.platform.billingprofile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.platform.PlatformMeter;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class BillingProfileMeter extends PlatformMeter {
    @JsonProperty
    private long timestamp;
    @JsonProperty
    private double value;
    @JsonProperty
    private String channel;
    @JsonProperty
    private String tariff;
    @JsonProperty
    private String type;
    @JsonIgnore
//    @JsonProperty
    private String unitM;
    @JsonIgnore
//    @JsonProperty("obiscode")
    private String obisCode;
}
