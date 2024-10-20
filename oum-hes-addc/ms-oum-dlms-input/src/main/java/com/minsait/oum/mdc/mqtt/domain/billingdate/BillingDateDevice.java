package com.minsait.oum.mdc.mqtt.domain.billingdate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.mqtt.domain.Device;
import lombok.Getter;

public class BillingDateDevice extends Device {
    @Getter
    @JsonProperty("billingProfile_Period")
    private String period;
}
