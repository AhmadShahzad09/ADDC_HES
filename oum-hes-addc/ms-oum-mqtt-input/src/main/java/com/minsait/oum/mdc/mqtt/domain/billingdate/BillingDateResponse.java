package com.minsait.oum.mdc.mqtt.domain.billingdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.mqtt.domain.Response;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BillingDateResponse extends Response {
    @Getter
    @JsonProperty("meterReadingList")
    private List<BillingDateDevice> devices;
}