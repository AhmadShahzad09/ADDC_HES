package com.minsait.mdc.util;

import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MeasurementUnitConverterUtils {

    private static final List<String> allowedMeasurementUnits = new ArrayList<>(Arrays.asList(
            "VAh", "bar", "VA", "m3", "var",
            "V", "Wh", "Hz", "ºC", "W", "A", "varh"));

    private static final List<String> possiblePrefix = new ArrayList<>(Arrays.asList(
       "n", "u", "m", "c", "d", "k", "M", "G", "T"
    )); // nano, micro, mili, centi, deci, kilo, Mega, Giga, Tera

    private static final List<String> possibleSuffix = new ArrayList<>(Arrays.asList("/h"));

    public static Pair<Double, String> convert(final Double value, final String unit) {
        Pair<Double, String> pairTransformed = treatExceptionalCase(value, unit);
        if (isAllowed(isolateUnit(unit)) && !isAllowedExactly(unit)) {
            pairTransformed = treatAddition(pairTransformed.getFirst(), pairTransformed.getSecond());
        }

        return pairTransformed;
    }

    private static boolean isAllowed(final String unit) {
        return allowedMeasurementUnits.stream()
                .anyMatch(amu -> amu.toLowerCase().indexOf(unit.toLowerCase()) != -1);
    }

    private static boolean isAllowedExactly(final String unit) {
        return allowedMeasurementUnits.stream()
                .anyMatch(amu -> amu.toLowerCase().indexOf(unit.toLowerCase()) == 0 && amu.length() == unit.length());
    }

    private static Pair<Double, String> treatAddition(final Double value, final String unit) {
        Pair<Double, String> pairTransformed = Pair.of(value, unit);
        Optional<String> prefix = getPrefix(unit);
        Optional<String> suffix = getSuffix(unit);
        if (prefix.isPresent()) {
            pairTransformed = treatPrefixCase(pairTransformed.getFirst(), pairTransformed.getSecond(), prefix.get());
        }
        if (suffix.isPresent()) {
            pairTransformed = treatSuffixCase(pairTransformed.getFirst(), pairTransformed.getSecond(), suffix.get());
        }

        return pairTransformed;
    }

    private static Optional<String> getPrefix(final String unit) {
        Optional<String> optionalPrefix = possiblePrefix.stream()
                .filter(s -> unit.indexOf(s) == 0)
                .findFirst();

        if (optionalPrefix.isPresent())
            return optionalPrefix;

        return Optional.empty();
    }

    private static Optional<String> getSuffix(final String unit) {
        Optional<String> optionalSuffix = possibleSuffix.stream()
                .filter(s -> unit.indexOf(s) != -1 && unit.indexOf(s) != 0)
                .findFirst();

        if (optionalSuffix.isPresent())
            return optionalSuffix;

        return Optional.empty();
    }

    private static String isolateUnit(final String unit) {
        String isolateUnit = unit;
        Optional<String> prefix = getPrefix(unit);
        Optional<String> suffix = getSuffix(unit);
        if (prefix.isPresent()) {
            isolateUnit = unit.substring(prefix.get().length());
        }
        if (suffix.isPresent()) {
            isolateUnit = isolateUnit.substring(0, unit.length() - suffix.get().length() - 1);
        }

        return isolateUnit;
    }

    private static Pair<Double, String> treatPrefixCase(final Double value, final String unit, final String prefix) {
        Pair<Double, String> transformed = Pair.of(value, unit);
        String isolateUnit = isolateUnit(unit);
        switch(prefix) {
            case "n":
                transformed = Pair.of(value * 0.0000000001, isolateUnit);
                break;
            case "u":
                transformed = Pair.of(value * 0.00000001, isolateUnit);
                break;
            case "m":
                transformed = Pair.of(value * 0.000001, isolateUnit);
                break;
            case "c":
                transformed = Pair.of(value * 0.01, isolateUnit);
                break;
            case "d":
                transformed = Pair.of(value * 0.1, isolateUnit);
                break;
            case "k":
                transformed = Pair.of(value * 1000, isolateUnit);
                break;
            case "M":
                transformed = Pair.of(value * 1000000, isolateUnit);
                break;
            case "G":
                transformed = Pair.of(value * 1000000000, isolateUnit);
                break;
            case "T":
                transformed = Pair.of(value * 1000000000000l, isolateUnit);
                break;
        }

        return transformed;
    }

    private static Pair<Double, String> treatSuffixCase(final Double value, final String unit, final String suffix) {
        Pair<Double, String> transformed = Pair.of(value, unit);
        String isolateUnit = isolateUnit(unit);
        switch (suffix) {
            case "/h":
                transformed = Pair.of(value * 24, isolateUnit);
                break;
        }

        return transformed;
    }

    private static Pair<Double, String> treatExceptionalCase(final Double value, final String unit) {
        Pair<Double, String> transformed = Pair.of(value, unit);
        switch(unit) {
            case "m^3":
                transformed = Pair.of(value, "m3");
                break;
            case "l":
                transformed = Pair.of(value/1000, "m3");
                break;
            case "gal":
                transformed = Pair.of(value/264.172, "m3");
                break;

        }

        return transformed;
    }

}
