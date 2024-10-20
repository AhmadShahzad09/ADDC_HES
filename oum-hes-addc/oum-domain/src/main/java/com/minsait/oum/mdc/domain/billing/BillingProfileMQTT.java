package com.minsait.oum.mdc.domain.billing;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BillingProfileMQTT {

    public BillingProfileMQTT() {
        this.blocks = new ArrayList<>();
    }

    private Double profileStatus;
    private List<BillingMQTT> blocks;
}
