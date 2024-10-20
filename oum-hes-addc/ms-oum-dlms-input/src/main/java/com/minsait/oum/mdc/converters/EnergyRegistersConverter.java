package com.minsait.oum.mdc.converters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.model.EnergyRegistersEntry;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;

@Component
public class EnergyRegistersConverter extends AbstractProfileConverter<EnergyRegistersEntry,List<PlatformMeter>>{


    public String getActionServiceName() {
        return "AbsoluteLoadProfileActionWS";
    }

    //TODO: optimize using maps of: field,value in this way we will have just one class to convert all profiles. Or we could use java reflection instead of reading value per value

    @Override
    public List<PlatformMeter> convert(EnergyRegistersEntry energyregistersEntry) {
    	
    	List<PlatformMeter> list = new ArrayList<PlatformMeter>();
    	
    	Date currentDate = new Date();
        long day = Long.valueOf(currentDate.getTime()/1000).longValue();

        if (energyregistersEntry.getTotalActiveEnergyImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+A")
                .day(day)
                .value(energyregistersEntry.getTotalActiveEnergyImport())
                .obisCode("1.0.1.8.0.255")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getTotalActiveEnergyExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-A")
                .day(day)
                .obisCode("1.0.2.8.0.255")
                .value(energyregistersEntry.getTotalActiveEnergyExport())
                .build());
        }

        if (energyregistersEntry.getTotalReactiveEnergyImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+Q")
                .day(day)
                .value(energyregistersEntry.getTotalReactiveEnergyImport())
                .obisCode("1.0.3.8.0.255")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getTotalReactiveEnergyExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-Q")
                .day(day)
                .value(energyregistersEntry.getTotalReactiveEnergyExport())
                .obisCode("1.0.4.8.0.255")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getTotalApparentEnergyImport() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+S")
                .day(day)
                .value(energyregistersEntry.getTotalApparentEnergyImport())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getTotalApparentEnergyExport() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-S")
                .day(day)
                .value(energyregistersEntry.getTotalApparentEnergyExport())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyQ1() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("Q1")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyQ1())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyQ2() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("Q2")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyQ2())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyQ3() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("Q3")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyQ3())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyQ4() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("Q4")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyQ4())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getAbsoluteActiveEnergy() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("ABS")
                .day(day)
                .value(energyregistersEntry.getAbsoluteActiveEnergy())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getNetActiveEnergy() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("NET")
                .day(day)
                .value(energyregistersEntry.getNetActiveEnergy())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getImportAveragePowerFactor() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("Avg_PF+")
                .day(day)
                .value(energyregistersEntry.getImportAveragePowerFactor())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

    	if (energyregistersEntry.getExportAveragePowerFactor() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("Avg_PF-")
	                .day(day)
	                .value(energyregistersEntry.getExportAveragePowerFactor())
	                .obisCode("1.0.84.5.0.255")
	                .unitM(" ")
	                .build());
        }

    	if (energyregistersEntry.getImportActiveEnergyPhase1() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("+A_L1")
	                .day(day)
	                .value(energyregistersEntry.getImportActiveEnergyPhase1())
	                .obisCode("1.0.21.8.0.255")
	                .unitM(" ")
	                .build());
        }

    	if( energyregistersEntry.getImportActiveEnergyPhase2() != null ) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("+A_L2")
	                .day(day)
	                .value(energyregistersEntry.getImportActiveEnergyPhase2())
	                .obisCode("1.0.41.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if(energyregistersEntry.getImportActiveEnergyPhase3() != null ) { 
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("+A_L3")
	                .day(day)
	                .value(energyregistersEntry.getImportActiveEnergyPhase3())
	                .obisCode("1.0.61.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if(energyregistersEntry.getExportActiveEnergyPhase1() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("-A_L1")
	                .day(day)
	                .value(energyregistersEntry.getExportActiveEnergyPhase1())
	                .obisCode("1.0.22.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getExportActiveEnergyPhase2() != null ) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("-A_L2")
	                .day(day)
	                .value(energyregistersEntry.getExportActiveEnergyPhase2())
	                .obisCode("1.0.42.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getExportActiveEnergyPhase3() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("-A_L3")
	                .day(day)
	                .value(energyregistersEntry.getExportActiveEnergyPhase3())
	                .obisCode("1.0.62.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getImportReactiveEnergyPhase1() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("+Q_L1")
	                .day(day)
	                .value(energyregistersEntry.getImportReactiveEnergyPhase1())
	                .obisCode("1.0.23.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getImportReactiveEnergyPhase2() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("+Q_L2")
	                .day(day)
	                .value(energyregistersEntry.getImportReactiveEnergyPhase2())
	                .obisCode("1.0.43.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getImportReactiveEnergyPhase3() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("+Q_L3")
	                .day(day)
	                .value(energyregistersEntry.getImportReactiveEnergyPhase3())
	                .obisCode("1.0.63.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getExportReactiveEnergyPhase1() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("-Q_L1")
	                .day(day)
	                .value(energyregistersEntry.getExportReactiveEnergyPhase1())
	                .obisCode("1.0.24.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getExportReactiveEnergyPhase2() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("-Q_L2")
	                .day(day)
	                .value(energyregistersEntry.getExportReactiveEnergyPhase2())
	                .obisCode("1.0.44.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if(energyregistersEntry.getExportReactiveEnergyPhase3() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("-Q_L3")
	                .day(day)
	                .value(energyregistersEntry.getExportReactiveEnergyPhase3())
	                .obisCode("1.0.64.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getImportApparentEnergyPhase1() != null ) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("+S_L1")
	                .day(day)
	                .value(energyregistersEntry.getImportApparentEnergyPhase1())
	                .obisCode("1.0.29.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getImportApparentEnergyPhase2() != null ) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("+S_L2")
	                .day(day)
	                .value(energyregistersEntry.getImportApparentEnergyPhase2())
	                .obisCode("1.0.49.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getImportApparentEnergyPhase3() != null ) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("+S_L3")
	                .day(day)
	                .value(energyregistersEntry.getImportApparentEnergyPhase3())
	                .obisCode("1.0.69.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getExportApparentEnergyPhase1() != null ) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("-S_L1")
	                .day(day)
	                .value(energyregistersEntry.getExportApparentEnergyPhase1())
	                .obisCode("1.0.30.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getExportApparentEnergyPhase2() != null ) {
    		list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-S_L2")
                .day(day)
                .value(energyregistersEntry.getExportApparentEnergyPhase2())
                .obisCode("1.0.50.8.0.255")
                .unitM(" ")
                .build());
    	}
    	
    	if (energyregistersEntry.getExportApparentEnergyPhase3() != null ) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("-S_L3")
	                .day(day)
	                .value(energyregistersEntry.getExportApparentEnergyPhase3())
	                .obisCode("1.0.70.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	
    	if (energyregistersEntry.getAbsoluteActiveEnergyPhase1() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("ABS_L1")
	                .day(day)
	                .value(energyregistersEntry.getAbsoluteActiveEnergyPhase1())
	                .obisCode("1.0.35.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	if (energyregistersEntry.getAbsoluteActiveEnergyPhase2() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("ABS_L2")
	                .day(day)
	                .value(energyregistersEntry.getAbsoluteActiveEnergyPhase2())
	                .obisCode("1.0.55.8.0.255")
	                .unitM(" ")
	                .build());
    	}
    	if (energyregistersEntry.getAbsoluteActiveEnergyPhase3() != null) {
	    	list.add(LoadProfileMeter.builder()
	                .measurementIntervalCode("H")
	                .magnitudeCode("ABS_L3")
	                .day(day)
	                .value(energyregistersEntry.getAbsoluteActiveEnergyPhase3())
	                .obisCode("1.0.75.8.0.255")
	                .unitM(" ")
	                .build());
        }

        if (energyregistersEntry.getActiveEnergyImportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+A_T1")
                .day(day)
                .value(energyregistersEntry.getActiveEnergyImportTariff1())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getActiveEnergyImportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+A_T2")
                .day(day)
                .value(energyregistersEntry.getActiveEnergyImportTariff2())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getActiveEnergyImportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+A_T3")
                .day(day)
                .value(energyregistersEntry.getActiveEnergyImportTariff3())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getActiveEnergyImportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+A_T4")
                .day(day)
                .value(energyregistersEntry.getActiveEnergyImportTariff4())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getActiveEnergyExportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-A_T1")
                .day(day)
                .value(energyregistersEntry.getActiveEnergyExportTariff1())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getActiveEnergyExportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-A_T2")
                .day(day)
                .value(energyregistersEntry.getActiveEnergyExportTariff2())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }
         
        if (energyregistersEntry.getActiveEnergyExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-A_T3")
                .day(day)
                .value(energyregistersEntry.getActiveEnergyExportTariff3())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getActiveEnergyExportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-A_T4")
                .day(day)
                .value(energyregistersEntry.getActiveEnergyExportTariff4())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyImportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+Q_T1")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyImportTariff1())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyImportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+Q_T2")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyImportTariff2())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyImportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+Q_T3")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyImportTariff3())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyImportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+Q_T4")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyImportTariff4())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyExportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-Q_T1")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyExportTariff1())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyExportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-Q_T2")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyExportTariff2())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-Q_T3")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyExportTariff3())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getReactiveEnergyExportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-Q_T4")
                .day(day)
                .value(energyregistersEntry.getReactiveEnergyExportTariff4())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getApparentEnergyImportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+S_T1")
                .day(day)
                .value(energyregistersEntry.getApparentEnergyImportTariff1())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getApparentEnergyImportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+S_T2")
                .day(day)
                .value(energyregistersEntry.getApparentEnergyImportTariff2())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getApparentEnergyImportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+S_T3")
                .day(day)
                .value(energyregistersEntry.getApparentEnergyImportTariff3())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getApparentEnergyImportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("+S_T4")
                .day(day)
                .value(energyregistersEntry.getApparentEnergyImportTariff4())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getApparentEnergyExportTariff1() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-S_T1")
                .day(day)
                .value(energyregistersEntry.getApparentEnergyExportTariff1())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getApparentEnergyExportTariff2() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-S_T2")
                .day(day)
                .value(energyregistersEntry.getApparentEnergyExportTariff2())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getApparentEnergyExportTariff3() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-S_T3")
                .day(day)
                .value(energyregistersEntry.getApparentEnergyExportTariff3())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        if (energyregistersEntry.getApparentEnergyExportTariff4() != null) {
        	list.add(LoadProfileMeter.builder()
                .measurementIntervalCode("H")
                .magnitudeCode("-S_T4")
                .day(day)
                .value(energyregistersEntry.getApparentEnergyExportTariff4())
                .obisCode(" ")
                .unitM(" ")
                .build());
        }

        return list;

    }
}
