package com.minsait.oum.mdc.service;

import com.minsait.oum.mdc.data.model.dto.Pagination;

public interface OperationService<ObjectType, ObjectDtoType, FilterType> {

	public Pagination<ObjectDtoType> find(FilterType filter);
	
	public void reschedule(FilterType object);
	
	public boolean cancel(FilterType object);
	
	public ObjectType updateStatus(ObjectType object);
}
