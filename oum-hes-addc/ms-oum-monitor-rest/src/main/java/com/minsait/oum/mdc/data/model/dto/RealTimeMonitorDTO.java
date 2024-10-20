package com.minsait.oum.mdc.data.model.dto;

import lombok.Data;

@Data
public class RealTimeMonitorDTO {

    Long runningCalls;

    Long buildingCalls;

    Long readyCalls;

    Long runningTasks;

    Long buildingTasks;

    Long readyTasks;
    
}