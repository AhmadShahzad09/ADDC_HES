package com.minsait.oum.mdc.mqtt.domain.billingprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.minsait.oum.mdc.mqtt.domain.Device;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BillingProfileDevice extends Device {
    @JsonProperty(value = "modelname")
    private String modelName;

    @JsonProperty
    private MediumType medium;

    @Getter
    @JsonProperty(value = "devicename")
    private String deviceName;

    @JsonProperty
    private String version;

    @JsonProperty("ipaddr")
    private String ipAddress;

    @JsonProperty
    private boolean maintenanceMode;

    @JsonProperty
    private long interval;

    @Getter
    @Setter
    @JsonProperty("deviceErrorMessage")
    private String errorMessage;

    @JsonProperty
    private String profileCode;

    @Getter
    @JsonProperty("intervalBlocks")
    private List<BillingProfileIntervalBlock> billingProfileIntervalBlocksList;
}
