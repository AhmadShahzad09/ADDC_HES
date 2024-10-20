package com.minsait.oum.mdc.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.CallGroup;
import com.minsait.oum.mdc.data.model.dto.CallDTO;
import com.minsait.oum.mdc.data.model.dto.Pagination;
import com.minsait.oum.mdc.data.model.filter.CallFilter;
import com.minsait.oum.mdc.data.repository.CallGroupRepository;
import com.minsait.oum.mdc.data.repository.CallRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CallOperationServiceImpl implements OperationService<Call, CallDTO, CallFilter> {

	@Value("${execution.timeout}")
	private Long executionTimeout;

	@Autowired
	private CallRepository callRepository;
	
	@Autowired
	private CallGroupRepository callGroupRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Pagination<CallDTO> find(CallFilter filter) {
		log.info("find-> {}", filter);

		final Page<Call> pageResult = callRepository.findCallsByFilter(filter);
		final List<CallDTO> data = pageResult.getContent().stream().map(x -> generateCallDTO(x)).collect(Collectors.toList());
		
		final Pagination<CallDTO> result = new Pagination<>();
		result.setData(data);
		result.setPages(pageResult.getTotalPages());
		result.setPageSize(pageResult.getNumberOfElements());
		result.setTotal(pageResult.getTotalElements());
		
		return result;
	}
	
	private CallDTO generateCallDTO(Call call) {
		final CallGroup callGroup = callGroupRepository.findById(call.getIdGroup()).get();
		final CallDTO callDTO = modelMapper.map(call, CallDTO.class);
		callDTO.updateCounters(callGroup, executionTimeout);
		return callDTO;
	}
	
	@Override
	public void reschedule(CallFilter filter) {
	}
	
	@Override
	public boolean cancel(CallFilter filter) {
		return false;
	}

	@Override
	public Call updateStatus(Call object) {
		return null;
	}
	
}
