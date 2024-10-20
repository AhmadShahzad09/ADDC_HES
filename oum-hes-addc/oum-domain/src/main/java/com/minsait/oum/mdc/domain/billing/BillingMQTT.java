package com.minsait.oum.mdc.domain.billing;

import lombok.Data;

@Data
public class BillingMQTT {
    private long time;
    private double value;
    private String unitM;
    private String channel;
    private String code;
    private long maxDemandTime;
    private String tariff;
    private String type;
}
