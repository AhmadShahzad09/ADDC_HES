package com.minsait.oum.mdc.mqtt.domain.billingprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.mqtt.domain.IntervalBlock;
import lombok.Getter;

import java.util.List;

public class BillingProfileIntervalBlock extends IntervalBlock {

    @Getter
    @JsonProperty
    private String profileStatus;

    @Getter
    @JsonProperty
    private List<BillingProfileBlock> blocks;
}
