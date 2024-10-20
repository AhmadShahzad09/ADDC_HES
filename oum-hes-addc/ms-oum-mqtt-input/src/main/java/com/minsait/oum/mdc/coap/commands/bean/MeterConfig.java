package com.minsait.oum.mdc.coap.commands.bean;

import lombok.Data;

@Data
public class MeterConfig {

    private String serialNumber;

    @Data
    public static class ProtocolConfig {
        private String param1;
        private String param2;
    }
}
