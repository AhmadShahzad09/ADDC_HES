package com.minsait.oum.mdc.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.model.InstantaneousRegistersEntry;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;

@Component
public class InstantaneousRegistersConverter extends AbstractProfileConverter<InstantaneousRegistersEntry, List<PlatformMeter>> {

    public String getActionServiceName() {
        return "InstValuesActionWS";
    }


    @Override
    public List<PlatformMeter> convert(InstantaneousRegistersEntry source) {

    	Date currentDate = new Date();
        long day = Long.valueOf(currentDate.getTime()/1000).longValue();
        
        List<PlatformMeter> list = new ArrayList<PlatformMeter>();
        

        if (source.getImportActivePower() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("IA_P")
                .day(day)
                .value(source.getImportActivePower())
                .build());
        }

        if (source.getImportReactivePower() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("IR_P")
                .day(day)
                .value(source.getImportReactivePower())
                .build());
        }

        if (source.getImportApparentPower() != null) {
        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("IApp_P")
                .day(day)
                .value(source.getImportApparentPower())
                .build());
        }

        if (source.getExportActivePower() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("EA_P")
                .day(day)
                .value(source.getExportActivePower())
                .build());
        }

        if (source.getExportReactivePower() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("ER_P")
                .day(day)
                .value(source.getExportReactivePower())
                .build());
        }

        if (source.getExportApparentPower() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("EApp_P")
                .day(day)
                .value(source.getExportApparentPower())
                .build());
        }

        if (source.getImportActivePowerPhase1() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("IA_P_L1")
                .day(day)
                .value(source.getImportActivePowerPhase1())
                .build());
        }

        if (source.getImportActivePowerPhase2() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("IA_P_L2")
                .day(day)
                .value(source.getImportActivePowerPhase2())
                .build());
        }

        if (source.getImportActivePowerPhase3() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("IA_P_L2")
                .day(day)
                .value(source.getImportActivePowerPhase3())
                .build());
        }

        if (source.getTotalPowerFactorImport() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("Inst_PF+")
                .day(day)
                .value(source.getTotalPowerFactorImport())
                .build());
        }

        if (source.getPowerFactorPhase1() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("Inst_PF_L1")
                .day(day)
                .value(source.getPowerFactorPhase1())
                .build());
        }

        if (source.getPowerFactorPhase2() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("Inst_PF_L2")
                .day(day)
                .value(source.getPowerFactorPhase2())
                .build());
        }

        if (source.getPowerFactorPhase3() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("Inst_PF_L3")
                .day(day)
                .value(source.getPowerFactorPhase3())
                .build());
        }

        if (source.getFrequency() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("Freq")
                .day(day)
                .value(source.getFrequency())
                .build());
        }

        if (source.getNoOfPowerFailures() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("No_PwrOff")
                .day(day)
                .value(source.getNoOfPowerFailures())
                .build());
        }

        if (source.getCumProgrammingCount() != null) {

        	list.add(LoadProfileMeter.builder()
                .magnitudeCode("Cum_Prg_Count")
                .day(day)
                .value(source.getCumProgrammingCount())
                .build());
        }

        return list;
    }
}
