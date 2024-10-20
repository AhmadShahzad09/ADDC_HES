package com.minsait.oum.mdc.coap.commands.bean;

import lombok.Data;

import java.util.List;

@Data
public class SchedulerConfig {

    private String cronTemplate;
    private String operation;
    private List<String> groups;

    private Long loadProfile1;
    private Long LoadProfile2;
    private Long PowerQualityProfile;
    private Long InstrumentationProfile;
    private Long EnergyProfile;
    private Long MaxDemandProfile;
    private Long InstantaneousValues;
    private Long waterProfile;
    private Long gwEvents;
    private Long Events;
    private Long Heartbeat;
    private Long eMonthlyBillingProfile;
    private Long eDailyBillingProfile;
    private Long wMonthlyBillingProfile;
}
