package com.minsait.oum.mdc.domain;

import lombok.Data;

@Data
public class NamePlateInformation {
    private String ctNumerator;
    private String ctDenominator;
    private String vtNumerator;
    private String vtDenominator;
    private String ctRatio;
    private String vtTransferRatio;
    private String currentRating;
    private String yearOfManufacture;
    private String firmwareVersion;
    private String address;
    private String totaliserserialNumber;
    private String traducerSerialNumber;
    private String manufacturerCode;
    private String type;
}
