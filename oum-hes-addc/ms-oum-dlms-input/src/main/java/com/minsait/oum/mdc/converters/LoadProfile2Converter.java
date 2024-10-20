package com.minsait.oum.mdc.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.model.LoadProfile2Entry;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;

@Component
public class LoadProfile2Converter extends AbstractProfileConverter<LoadProfile2Entry, List<PlatformMeter>>{


    public String getActionServiceName() {
        return "InstValuesActionWS";
    }


    @Override
    public List<PlatformMeter> convert(LoadProfile2Entry loadProfile2Entry) {

        long day = Long.valueOf(loadProfile2Entry.getClock().getTime()/1000).longValue();
        
        String measurementIntervalCode = getMeasurementIntervalCode(loadProfile2Entry.getCapturePeriod());
    	
        List<PlatformMeter> list = new ArrayList<PlatformMeter>();
        

        if (loadProfile2Entry.getVoltageL1() != null) {
        	list.add(LoadProfileMeter.builder()
                .day(day)
                .measurementIntervalCode(measurementIntervalCode)
                .quality(String.valueOf(loadProfile2Entry.getStatus()))
                .magnitudeCode("Inst_V_L1")
                .obisCode("1.0.32.7.0.255")
                .value(loadProfile2Entry.getVoltageL1())
                .build());
        }

        if (loadProfile2Entry.getVoltageL2() != null) {
        	list.add(LoadProfileMeter.builder()
                .day(day)
                .measurementIntervalCode(measurementIntervalCode)
                .quality(String.valueOf(loadProfile2Entry.getStatus()))
                .magnitudeCode("Inst_V_L2")
                .obisCode("1.0.52.7.0.255")
                .value(loadProfile2Entry.getVoltageL2())
                .build());
        }

        if (loadProfile2Entry.getVoltageL3() != null) {
        	list.add(LoadProfileMeter.builder()
                .day(day)
                .measurementIntervalCode(measurementIntervalCode)
                .quality(String.valueOf(loadProfile2Entry.getStatus()))
                .magnitudeCode("Inst_V_L3")
                .obisCode("1.0.72.7.0.255")
                .value(loadProfile2Entry.getVoltageL3())
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
