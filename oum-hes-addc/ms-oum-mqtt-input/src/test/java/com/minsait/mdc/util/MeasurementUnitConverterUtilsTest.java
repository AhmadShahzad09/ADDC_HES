package com.minsait.mdc.util;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.data.util.Pair;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({com.minsait.mdc.util.MeasurementUnitConverterUtils.class})
public class MeasurementUnitConverterUtilsTest {

    @DisplayName("Same measurement Unit")
    @Test
    public void exactMeasurementUnit() {
        Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(5.5, "m3");
        assertEquals(unitConverted.getFirst(), Double.valueOf(5.5));
        assertEquals(unitConverted.getSecond(), "m3");
        unitConverted = MeasurementUnitConverterUtils.convert(3.5, "VAh");
        assertEquals(unitConverted.getFirst(), Double.valueOf(3.5));
        assertEquals(unitConverted.getSecond(), "VAh");
    }

    @DisplayName("Same measurement Unit in different case")
    @Test
    public void exactDifferentCaseMeasurementUnit() {
        Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(5.435, "w");
        assertEquals(unitConverted.getFirst(), Double.valueOf(5.435));
        assertEquals(unitConverted.getSecond(), "w");
        unitConverted = MeasurementUnitConverterUtils.convert(45.8, "hz");
        assertEquals(unitConverted.getFirst(), Double.valueOf(45.8));
        assertEquals(unitConverted.getSecond(), "hz");
        unitConverted = MeasurementUnitConverterUtils.convert(26.7, "BAR");
        assertEquals(unitConverted.getFirst(), Double.valueOf(26.7));
        assertEquals(unitConverted.getSecond(), "BAR");
    }

    @DisplayName("Exceptional measurement m^3 Unit")
    @Test
    public void exceptionalMetersMeasurementUnit() {
        Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(23), "m^3");
        assertEquals(unitConverted.getFirst(), Double.valueOf(23));
        assertEquals(unitConverted.getSecond(), "m3");
    }

    @DisplayName("Exceptional measurement litres Unit")
    @Test
    public void exceptionalLitresMeasurementUnit() {
        Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(5000), "l");
        assertEquals(unitConverted.getFirst(), Double.valueOf(5));
        assertEquals(unitConverted.getSecond(), "m3");
    }

    @DisplayName("Exceptional measurement galons Unit")
    @Test
    public void exceptionalGalonsMeasurementUnit() {
        Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(6000), "gal");
        assertEquals(unitConverted.getFirst(), Double.valueOf(22.7124752055479));
        assertEquals(unitConverted.getSecond(), "m3");
    }

    @DisplayName("Prefix measurement Unit")
    @Test
    public void prefixMeasurementUnit() {
        Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(60000000000l), "ubar");
        assertEquals(unitConverted.getFirst(), Double.valueOf(6));
        assertEquals(unitConverted.getSecond(), "bar");
        unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(500000000), "nW");
        assertEquals(unitConverted.getFirst(), Double.valueOf(5));
        assertEquals(unitConverted.getSecond(), "W");
        unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(6700000), "mVAh");
        assertEquals(unitConverted.getFirst(), Double.valueOf(6.7));
        assertEquals(unitConverted.getSecond(), "VAh");
        unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(58000), "cVA");
        assertEquals(unitConverted.getFirst(), Double.valueOf(580));
        assertEquals(unitConverted.getSecond(), "VA");
        unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(598), "dm3");
        assertEquals(unitConverted.getFirst(), Double.valueOf(59.8));
        assertEquals(unitConverted.getSecond(), "m3");
        unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(3), "kvar");
        assertEquals(unitConverted.getFirst(), Double.valueOf(3000));
        assertEquals(unitConverted.getSecond(), "var");
        unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(0.1), "MV");
        assertEquals(unitConverted.getFirst(), Double.valueOf(100000));
        assertEquals(unitConverted.getSecond(), "V");
        unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(0.8), "GWh");
        assertEquals(unitConverted.getFirst(), Double.valueOf(800000000));
        assertEquals(unitConverted.getSecond(), "Wh");
        unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(0.0058), "THz");
        assertEquals(unitConverted.getFirst(), Double.valueOf(5800000000l));
        assertEquals(unitConverted.getSecond(), "Hz");
    }

    @DisplayName("Suffix measurement Unit")
    @Test
    public void suffixMeasurementUnit() {
        Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(56), "w/h");
        assertEquals(unitConverted.getFirst(), Double.valueOf(1344));
        assertEquals(unitConverted.getSecond(), "w");
    }

    @DisplayName("Suffix & Prefix measurement Unit")
    @Test
    public void suffixAndPrefixMeasurementUnit() {
        Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(Double.valueOf(56), "kw/h");
        assertEquals(unitConverted.getFirst(), Double.valueOf(1344000));
        assertEquals(unitConverted.getSecond(), "w");
    }
}
