package com.minsait.oum.mdc.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.model.MaxDemandRegistersEntry;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;

@Component
public class MaxDemandRegistersConverter extends AbstractProfileConverter<MaxDemandRegistersEntry,List<PlatformMeter>>{


    public String getActionServiceName() {
        return "InstValuesActionWS";
    }


    @Override
    public List<PlatformMeter> convert(MaxDemandRegistersEntry maxDemandRegistersEntry) {

    	Date currentDate = new Date();
    	long day = Long.valueOf(currentDate.getTime()/1000).longValue();
    	

    	List<PlatformMeter> list = new ArrayList<PlatformMeter>();
    	

    	if (maxDemandRegistersEntry.getMaxDemandActivePowerImport() != null) {
    		list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerImport())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandActivePowerExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerExport())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerImport())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerExport())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerImport())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerExport())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandActivePowerImportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA_T1")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerImportTariff1())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandActivePowerImportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA_T2")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerImportTariff2())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandActivePowerImportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA_T3")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerImportTariff3())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandActivePowerImportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA_T4")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerImportTariff4())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandActivePowerExportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA_T1")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerExportTariff1())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandActivePowerExportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA_T2")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerExportTariff2())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandActivePowerExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA_T3")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerExportTariff3())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandActivePowerExportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA_T4")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandActivePowerExportTariff4())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerImportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR_T1")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerImportTariff1())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerImportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR_T2")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerImportTariff2())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerImportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR_T3")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerImportTariff3())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerImportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR_T4")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerImportTariff4())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerExportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR_T1")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerExportTariff1())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerExportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR_T2")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerExportTariff2())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR_T3")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerExportTariff3())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandReactivePowerExportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR_T4")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandReactivePowerExportTariff4())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerImportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS_T1")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerImportTariff1())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerImportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS_T2")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerImportTariff2())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerImportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS_T3")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerImportTariff3())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerImportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS_T4")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerImportTariff4())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerExportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS_T1")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerExportTariff1())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerExportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS_T2")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerExportTariff2())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS_T3")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerExportTariff3())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandApparentPowerExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS_T4")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandApparentPowerExportTariff4())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerImport().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerExport().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerImport().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerExport().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerImport().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerExport().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerImportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA_T1_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerImportTariff1().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerImportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA_T2_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerImportTariff2().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerImportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA_T3_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerImportTariff3().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerImportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDA_T4_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerImportTariff4().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerExportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA_T1_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerExportTariff1().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerExportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA_T2_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerExportTariff2().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA_T3_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerExportTariff3().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateActivePowerExportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDA_T4_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateActivePowerExportTariff4().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerImportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR_T1_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerImportTariff1().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerImportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR_T2_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerImportTariff2().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerImportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR_T3_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerImportTariff3().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerImportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDR_T4_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerImportTariff4().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerExportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR_T1_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerExportTariff1().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerExportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR_T2_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerExportTariff2().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR_T3_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerExportTariff3().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateReactivePowerExportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDR_T4_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateReactivePowerExportTariff4().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerImportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS_T1_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerImportTariff1().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerImportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS_T2_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerImportTariff2().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerImportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS_T3_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerImportTariff3().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerImportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("+MDS_T4_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerImportTariff4().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerExportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS_T1_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerExportTariff1().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerExportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS_T2_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerExportTariff2().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS_T3_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerExportTariff3().getTime())
                .build());
        }

        if (maxDemandRegistersEntry.getMaxDemandDateApparentPowerExportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("-MDS_T4_TS")
                .day(day)
                .value(maxDemandRegistersEntry.getMaxDemandDateApparentPowerExportTariff4().getTime())
                .build());
        }
        
        return list;
    }
}
