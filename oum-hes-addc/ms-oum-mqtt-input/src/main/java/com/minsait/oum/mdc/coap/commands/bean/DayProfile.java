package com.minsait.oum.mdc.coap.commands.bean;

import lombok.Data;

import java.util.List;

@Data
public class DayProfile {

    private Long dayId;
    private Long qty;
    private List<DayItems> dayItem;


    @Data
    public static class DayItems{
        private String startTime;
        private String scriptLogicalName;
        private Long scriptSelector;
    }
}
