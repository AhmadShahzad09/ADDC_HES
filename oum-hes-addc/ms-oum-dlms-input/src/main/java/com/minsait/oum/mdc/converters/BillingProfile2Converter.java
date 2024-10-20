package com.minsait.oum.mdc.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.model.BillingProfileEntry;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;

@Component
public class BillingProfile2Converter extends AbstractProfileConverter<BillingProfileEntry,List<PlatformMeter>>{


    public String getActionServiceName() {
        return "BillingProfileActionWS";
    }


    @Override
    public List<PlatformMeter> convert(BillingProfileEntry billingProfileEntry) {
    	
        long day = Long.valueOf(billingProfileEntry.getClock().getTime()/1000).longValue();
        
        List<PlatformMeter> list = new ArrayList<PlatformMeter>();

        if (billingProfileEntry.getTotalActiveEnergyImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .day(day)
                .quality(String.valueOf(billingProfileEntry.getStatus()))
                .magnitudeCode("+A")
                .obisCode("1.0.1.8.0.255")
                .value(billingProfileEntry.getTotalActiveEnergyImport())
                .build());
        }

        if (billingProfileEntry.getTotalRectiveEnergyImport() != null) {
            list.add(LoadProfileMeter.builder()
                .day(day)
                .quality(String.valueOf(billingProfileEntry.getStatus()))
                .magnitudeCode("+Q")
                .obisCode("1.0.3.8.0.255")
                .value(billingProfileEntry.getTotalRectiveEnergyImport())
                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .day(day)
                .quality(String.valueOf(billingProfileEntry.getStatus()))
                .magnitudeCode("+MDA")
                .obisCode("1.0.1.6.0.255")
                .value(billingProfileEntry.getMaxDemandActivePowerImport())
                .build());
        }

        return list;
    }
}
