package com.minsait.oum.mdc.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.model.LoadProfile1Entry;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;

@Component
public class LoadProfile1Converter extends AbstractProfileConverter<LoadProfile1Entry,List<PlatformMeter>>{


    public String getActionServiceName() {
        return "LoadProfileActionWS";
    }


    @Override
    public List<PlatformMeter> convert(LoadProfile1Entry loadProfile1Entry) {
    	
        long day = Long.valueOf(loadProfile1Entry.getClock().getTime()/1000).longValue();

        String measurementIntervalCode = getMeasurementIntervalCode(loadProfile1Entry.getCapturePeriod());
    	
        List<PlatformMeter> list = new ArrayList<PlatformMeter>();

        if (loadProfile1Entry.getTotalActiveEnergyExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .day(day)
                .measurementIntervalCode(measurementIntervalCode)
                .quality(String.valueOf(loadProfile1Entry.getStatus()))
                .magnitudeCode("+A")
                .obisCode("1.0.1.8.0.255")
                .value(loadProfile1Entry.getTotalActiveEnergyImport())
                .build());
        }

        if (loadProfile1Entry.getTotalActiveEnergyImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .day(day)
                .measurementIntervalCode(measurementIntervalCode)
                .quality(String.valueOf(loadProfile1Entry.getStatus()))
                .magnitudeCode("-A")
                .obisCode("1.0.2.8.0.255")
                .value(loadProfile1Entry.getTotalActiveEnergyExport())
                .build());
        }

        if (loadProfile1Entry.getTotalReactiveEnergyExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .day(day)
                .measurementIntervalCode(measurementIntervalCode)
                .quality(String.valueOf(loadProfile1Entry.getStatus()))
                .magnitudeCode("+Q")
                .obisCode("1.0.3.8.0.255")
                .value(loadProfile1Entry.getTotalReactiveEnergyImport())
                .build());
        }

        if (loadProfile1Entry.getTotalReactiveEnergyImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .day(day)
                .measurementIntervalCode(measurementIntervalCode)
                .quality(String.valueOf(loadProfile1Entry.getStatus()))
                .magnitudeCode("-Q")
                .obisCode("1.0.4.8.0.255")
                .value(loadProfile1Entry.getTotalReactiveEnergyExport())
                .build());
        }

        return list;
    }


	private String getMeasurementIntervalCode(Integer capturePeriod) {
		if (capturePeriod != null) {
			if (capturePeriod.intValue() == 86400) {
				return "D";
			} else if (capturePeriod.intValue() == 3600) {
				return "H";
			} else if (capturePeriod.intValue() == 1800) {
				return "MIN30";
			} else if (capturePeriod.intValue() == 1200) {
				return "MIN20";
			} else if (capturePeriod.intValue() == 900) {
				return "MIN15";
			} else if (capturePeriod.intValue() == 600) {
				return "MIN10";
			} else if (capturePeriod.intValue() == 300) {
				return "MIN05";
			} else if (capturePeriod.intValue() == 60) {
				return "MIN01";
			}
		}
		return null;
	}
}
