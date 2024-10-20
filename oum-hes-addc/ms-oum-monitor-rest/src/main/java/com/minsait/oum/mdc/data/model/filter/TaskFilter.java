package com.minsait.oum.mdc.data.model.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskFilter extends CallFilter {
	
	private String callId;

	public TaskFilter(String id) {
		super(id);
	}

}
