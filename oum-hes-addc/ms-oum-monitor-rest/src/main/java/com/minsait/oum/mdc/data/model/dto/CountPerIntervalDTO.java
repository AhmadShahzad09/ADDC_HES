package com.minsait.oum.mdc.data.model.dto;

import java.util.Date;


import com.minsait.oum.mdc.data.model.CountPerInterval;

import lombok.Data;

@Data
public class CountPerIntervalDTO extends CountPerInterval{
	
	Date intervalStartDate;
	Date intervalEndDate;
	
	public CountPerIntervalDTO() {
		super();
	}

	@Override
	public String toString() {
		return "CountPerIntervalDTO [id=" + getId() + ", count=" + getCount() 
			+ ", intervalStartDate=" + intervalStartDate + ", intervalEndDate=" + intervalEndDate + "]";
	}	
	
	

}
