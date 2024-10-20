package com.minsait.oum.mdc.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.model.PowerQualityProfileEntry;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;

@Component
public class PowerQualityProfileConverter extends AbstractProfileConverter<PowerQualityProfileEntry,List<PlatformMeter>>{

    public String getActionServiceName() {
        return "InstValuesActionWS";
    }


    @Override
    public List<PlatformMeter> convert(PowerQualityProfileEntry powerQualityProfileEntry) {
    	
    	List<PlatformMeter> list = new ArrayList<PlatformMeter>();

    	if (powerQualityProfileEntry.getVoltageL1Avg() != null) {
    		list.add(LoadProfileMeter.builder()
                .day(powerQualityProfileEntry.getClock().getTime())
                .quality(String.valueOf(powerQualityProfileEntry.getStatus()))
                .magnitudeCode("Avg_V_L1")
                .obisCode("1.0.32.24.0.255")
                .value(powerQualityProfileEntry.getVoltageL1Avg())
                .build());
    	}
    	
    	if (powerQualityProfileEntry.getVoltageL2Avg() != null) {
    		list.add(LoadProfileMeter.builder()
                .day(powerQualityProfileEntry.getClock().getTime())
                .quality(String.valueOf(powerQualityProfileEntry.getStatus()))
                .magnitudeCode("Avg_V_L2")
                .obisCode("1.0.52.24.0.255")
                .value(powerQualityProfileEntry.getVoltageL2Avg())
                .build());
    	}
    	

    	if (powerQualityProfileEntry.getVoltageL3Avg() != null) {
    		list.add(LoadProfileMeter.builder()
                .day(powerQualityProfileEntry.getClock().getTime())
                .quality(String.valueOf(powerQualityProfileEntry.getStatus()))
                .magnitudeCode("Avg_V_L3")
                .obisCode("1.0.72.24.0.255")
                .value(powerQualityProfileEntry.getVoltageL3Avg())
                .build());
    	}

        return list;
    }
}
