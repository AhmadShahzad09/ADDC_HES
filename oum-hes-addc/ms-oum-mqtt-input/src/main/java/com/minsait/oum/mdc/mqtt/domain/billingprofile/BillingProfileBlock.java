package com.minsait.oum.mdc.mqtt.domain.billingprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.mqtt.domain.Block;
import lombok.Getter;

@Getter
public class BillingProfileBlock extends Block {
    @JsonProperty
    private double value;
    @JsonProperty
    private String unitM;
    @JsonProperty
    private String channel;
    @JsonProperty
    private String code;
    @JsonProperty
    private long maxDemandTime;
    @JsonProperty
    private String tariff;
    @JsonProperty
    private String type;
}
