package com.minsait.oum.mdc.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import com.minsait.oum.mdc.data.model.CallGroup;
import com.minsait.oum.mdc.data.model.Status;
import com.minsait.oum.mdc.data.model.filter.GroupFilter;

public interface CustomCallGroupRepository {
	
	public Page<CallGroup> findCallGroupsByFilter(GroupFilter filter);
	
	public CallGroup updateStatus(CallGroup entity);
	
	public void updateStatusAndEquipmentCodes(String id, Status status, List<String> equipmentCodes);

	//public CallGroup insert(CallGroup entity);

}
