package com.minsait.dcu;

import java.util.List;

import com.minsait.common.enu.ProfileCommon;

import lombok.Data;

@Data
public class DcuParameter extends ProfileCommon {
	private List<DcuParameterEntry> meterCollector;

}
