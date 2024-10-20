package com.minsait.oum.mdc.data.repository;

import org.springframework.data.domain.Page;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.Task;
import com.minsait.oum.mdc.data.model.filter.CallFilter;
import com.minsait.oum.mdc.data.model.filter.TaskFilter;

public interface CustomCallRepository {

	public Page<Call> findCallsByFilter(CallFilter filter);
	
	public Page<Task> findTasksByFilter(Call call, TaskFilter filter);	

}
