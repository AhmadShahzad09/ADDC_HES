package com.minsait.oum.mdc.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.model.BillingProfileEntry;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.billingprofile.BillingProfileMeter;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;

@Component
public class BillingProfileConverter extends AbstractProfileConverter<BillingProfileEntry,List<PlatformMeter>>{


    public String getActionServiceName() {
        return "BillingProfileActionWS";
    }


    @Override
    public List<PlatformMeter> convert(BillingProfileEntry billingProfileEntry) {
    	
        long day = Long.valueOf(billingProfileEntry.getClock().getTime()/1000).longValue();
        
        List<PlatformMeter> list = new ArrayList<PlatformMeter>();
        
        // TOTAL
        if (billingProfileEntry.getTotalActiveEnergyImport() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+A")
	                .tariff("P0")
	                .obisCode("1.0.1.8.0.255")
	                .value(billingProfileEntry.getTotalActiveEnergyImport())
	                .build());
        }
        
        if (billingProfileEntry.getTotalRectiveEnergyImport() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+Q")
	                .tariff("P0")
	                .obisCode("1.0.3.8.0.255")
	                .value(billingProfileEntry.getTotalRectiveEnergyImport())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerImport() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+MDA")
	                .tariff("P0")
	                .obisCode("1.0.1.6.0.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerImport())
	                .build());
        }

        if (billingProfileEntry.getTotalActiveEnergyExport() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-A")
	                .tariff("P0")
	                .obisCode("1.0.2.8.0.255")
	                .value(billingProfileEntry.getTotalActiveEnergyExport())
	                .build());
        }

        if (billingProfileEntry.getTotalRectiveEnergyExport() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-Q")
	                .tariff("P0")
	                .obisCode("1.0.4.8.0.255")
	                .value(billingProfileEntry.getTotalRectiveEnergyExport())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerExport() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-MDA")
	                .tariff("P0")
	                .obisCode("1.0.2.6.0.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerExport())
	                .build());
        }
        
        // TARIFF 1
        if (billingProfileEntry.getActiveEnergyImportTariff1() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+A_T1")
	                .tariff("P1")
	                .obisCode("1.0.1.8.0.255")
	                .value(billingProfileEntry.getActiveEnergyImportTariff1())
	                .build());
        }
        
        if (billingProfileEntry.getReactiveEnergyImportTariff1() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+Q_T1")
	                .tariff("P1")
	                .obisCode("1.0.3.8.1.255")
	                .value(billingProfileEntry.getReactiveEnergyImportTariff1())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerImportTariff1() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+MDA_T1")
	                .tariff("P1")
	                .obisCode("1.0.1.6.1.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerImportTariff1())
	                .build());
        }

        if (billingProfileEntry.getActiveEnergyExportTariff1() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-A_T1")
	                .tariff("P1")
	                .obisCode("1.0.2.8.1.255")
	                .value(billingProfileEntry.getActiveEnergyExportTariff1())
	                .build());
        }

        if (billingProfileEntry.getReactiveEnergyExportTariff1() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-Q_T1")
	                .tariff("P1")
	                .obisCode("1.0.4.8.1.255")
	                .value(billingProfileEntry.getReactiveEnergyExportTariff1())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerExportTariff1() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-MDA_T1")
	                .tariff("P1")
	                .obisCode("1.0.2.6.1.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerExportTariff1())
	                .build());
        }
        
        // TARIFF 2
        if (billingProfileEntry.getActiveEnergyImportTariff2() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+A_T2")
	                .tariff("P2")
	                .obisCode("1.0.1.8.2.255")
	                .value(billingProfileEntry.getActiveEnergyImportTariff2())
	                .build());
        }
        
        if (billingProfileEntry.getReactiveEnergyImportTariff2() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+Q_T2")
	                .tariff("P2")
	                .obisCode("1.0.3.8.2.255")
	                .value(billingProfileEntry.getReactiveEnergyImportTariff2())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerImportTariff2() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+MDA_T2")
	                .tariff("P2")
	                .obisCode("1.0.1.6.2.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerImportTariff2())
	                .build());
        }

        if (billingProfileEntry.getActiveEnergyExportTariff2() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-A_T2")
	                .tariff("P2")
	                .obisCode("1.0.2.8.2.255")
	                .value(billingProfileEntry.getActiveEnergyExportTariff2())
	                .build());
        }

        if (billingProfileEntry.getReactiveEnergyExportTariff2() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-Q_T2")
	                .tariff("P2")
	                .obisCode("1.0.4.8.2.255")
	                .value(billingProfileEntry.getReactiveEnergyExportTariff2())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerExportTariff2() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-MDA_T2")
	                .tariff("P2")
	                .obisCode("1.0.2.6.2.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerExportTariff2())
	                .build());
        }
        
        // TARIFF 3
        if (billingProfileEntry.getActiveEnergyImportTariff3() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+A_T3")
	                .tariff("P3")
	                .obisCode("1.0.1.8.3.255")
	                .value(billingProfileEntry.getActiveEnergyImportTariff3())
	                .build());
        }
        
        if (billingProfileEntry.getReactiveEnergyImportTariff3() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+Q_T3")
	                .tariff("P3")
	                .obisCode("1.0.3.8.3.255")
	                .value(billingProfileEntry.getReactiveEnergyImportTariff3())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerImportTariff3() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+MDA_T3")
	                .tariff("P3")
	                .obisCode("1.0.1.6.3.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerImportTariff3())
	                .build());
        }

        if (billingProfileEntry.getActiveEnergyExportTariff3() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-A_T3")
	                .tariff("P3")
	                .obisCode("1.0.2.8.3.255")
	                .value(billingProfileEntry.getActiveEnergyExportTariff3())
	                .build());
        }

        if (billingProfileEntry.getReactiveEnergyExportTariff3() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-Q_T3")
	                .tariff("P3")
	                .obisCode("1.0.4.8.3.255")
	                .value(billingProfileEntry.getReactiveEnergyExportTariff3())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerExportTariff3() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-MDA_T3")
	                .tariff("P3")
	                .obisCode("1.0.2.6.3.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerExportTariff3())
	                .build());
        }
        
        // TARIFF 4
        if (billingProfileEntry.getActiveEnergyImportTariff4() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+A_T4")
	                .tariff("P4")
	                .obisCode("1.0.4.8.4.255")
	                .value(billingProfileEntry.getActiveEnergyImportTariff4())
	                .build());
        }
        
        if (billingProfileEntry.getReactiveEnergyImportTariff4() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+Q_T4")
	                .tariff("P4")
	                .obisCode("1.0.3.8.4.255")
	                .value(billingProfileEntry.getReactiveEnergyImportTariff4())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerImportTariff4() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("+MDA_T4")
	                .tariff("P4")
	                .obisCode("1.0.1.6.4.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerImportTariff4())
	                .build());
        }

        if (billingProfileEntry.getActiveEnergyExportTariff4() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-A_T4")
	                .tariff("P4")
	                .obisCode("1.0.2.8.4.255")
	                .value(billingProfileEntry.getActiveEnergyExportTariff4())
	                .build());
        }

        if (billingProfileEntry.getReactiveEnergyExportTariff4() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-Q_T4")
	                .tariff("P4")
	                .obisCode("1.0.4.8.4.255")
	                .value(billingProfileEntry.getReactiveEnergyExportTariff4())
	                .build());
        }

        if (billingProfileEntry.getMaxDemandActivePowerExportTariff4() != null) {
	    	list.add(BillingProfileMeter.builder()
	                .timestamp(day)
	                .quality(String.valueOf(billingProfileEntry.getStatus()))
	                .channel("-MDA_T4")
	                .tariff("P4")
	                .obisCode("1.0.2.6.4.255")
	                .value(billingProfileEntry.getMaxDemandActivePowerExportTariff4())
	                .build());
        }

        return list;
    }
}
