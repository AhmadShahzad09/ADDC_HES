package com.minsait.oum.mdc.domain;

import lombok.Data;

@Data
public class GatewayInfo {
    private String serialNumber;
    private String model;
    private String mac_address;
    private String firmwareVersion;
    private SimCard simCard;
    private NetworkInformation networkInformation;

    @Data
    public class SimCard {
        private String iccId;
        private String msisdn;
    }
    
    @Data
    public class NetworkInformation {
        private String rsrp;
        private String rssi;
        private String rsrq;
        private String sinr;
        private String band;
        private String typeCommunication;
        private String networkIP;
    }

}
