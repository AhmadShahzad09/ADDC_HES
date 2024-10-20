package com.minsait.oum.mdc.data.model.filter;

import com.minsait.oum.mdc.data.model.Status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CallFilter extends BaseFilter {

	private String idGroup;
	private Status status;
	private String endPoint;
	private Long deviceId;
	private Long startDate;
	private Long endDate;
	
	public CallFilter(String id) {
		super(id);
	}

}
