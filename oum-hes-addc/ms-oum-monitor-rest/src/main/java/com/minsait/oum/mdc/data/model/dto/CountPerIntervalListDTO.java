package com.minsait.oum.mdc.data.model.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class CountPerIntervalListDTO {
	
	Date firstPeriodStartDate;
	Date lastPeriodStartDate;
	
	String description;
	String countUnit;
	
	
	private List<CountPerIntervalDTO> list;

}
