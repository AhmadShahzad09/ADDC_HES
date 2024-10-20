package com.minsait.oum.mdc.data.model.filter;

import com.minsait.oum.mdc.data.model.Status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupFilter extends BaseFilter {

	private String name;
	private Status status;
	private String executionType;
	private Long startDate;
	private Long endDate;
	private Long deviceId;
	private String deviceName;
	
	public GroupFilter(String id) {
		super(id);
	}
	
}
