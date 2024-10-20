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
import com.minsait.oum.mdc.data.model.Status;
import com.minsait.oum.mdc.data.model.dto.CallGroupDTO;
import com.minsait.oum.mdc.data.model.dto.Pagination;
import com.minsait.oum.mdc.data.model.filter.GroupFilter;
import com.minsait.oum.mdc.data.repository.CallGroupRepository;
import com.minsait.oum.mdc.data.repository.CallRepository;

import lombok.extern.slf4j.Slf4j;

@Service
public class GroupOperationServiceImpl implements OperationService<CallGroup, CallGroupDTO, GroupFilter> {

	@Value("${execution.timeout}")
	private Long executionTimeout;

	@Autowired
	private CallRepository callRepository;
	
	@Autowired
	private CallGroupRepository callGroupRepository;

	@Autowired
	private ModelMapper modelMapper;


	@Override
	public Pagination<CallGroupDTO> find(GroupFilter filter) {
		// Para los busqueda, solo se utilizan los campos 
		// id, datetime, name executionType y equipmentCodes del objeto callGroup
		// Por este motivo, el metodo generateCallGroupDTO no deberia
		// modificar estas propiedades de las recuperdas de la base de datos
		final Page<CallGroup> pageResult = callGroupRepository.findCallGroupsByFilter(filter);
		final List<CallGroupDTO> data = pageResult.getContent().stream().map(x -> generateCallGroupDTO(x)).collect(Collectors.toList());
		
		
		final Pagination<CallGroupDTO> result = new Pagination<>();
		result.setData(data);
		result.setPages(pageResult.getTotalPages());
		result.setPageSize(pageResult.getNumberOfElements());
		result.setTotal(pageResult.getTotalElements());
		
//		final CallGroup myCallGroup = new CallGroup();
//		myCallGroup.setId(UUID.randomUUID());
//		myCallGroup.setUuid(myCallGroup.getId().toString());
//		callGroupRepository.insert(myCallGroup);

//		final CallGroup myCallGroup = new CallGroup();
//		callGroupRepository.insert(myCallGroup);
		
		return result;
	}
	
	private CallGroupDTO generateCallGroupDTO(CallGroup callGroup) {
		final CallGroupDTO callGroupDTO = modelMapper.map(callGroup, CallGroupDTO.class);
		final List<Call> groupCalls = callRepository.findByIdGroup(callGroup.getId());
		callGroupDTO.updateCounters(groupCalls, executionTimeout);
		// Volvemos a poner el estado original ya que el metodo
		// updateConunters modifica el estado y estariamos devolviendo
		// un estado modificado con respecto al obtenido en el grupo
		callGroupDTO.setStatus(callGroup.getStatus());
		callGroupDTO.setDatetime(callGroup.getDatetime());
		return callGroupDTO;
	}

	@Override
	public void reschedule(GroupFilter filter) {
	}
	
	@Override
	public boolean cancel(GroupFilter filter) {
		return false;
	}
	
	@Override
	public CallGroup updateStatus(CallGroup callGroup) {
		if (callGroup.getStatus() != null) {
			callGroupRepository.updateStatus(callGroup);
		} else {
			CallGroupDTO callGroupDTO = generateCallGroupDTO(callGroup);
			callGroupDTO.updateCounters(callRepository.findByIdGroup(callGroup.getId()), executionTimeout);

			callGroupRepository.updateStatusAndEquipmentCodes(
					callGroupDTO.getId(), 
					callGroupDTO.getStatus(), 
					callGroupDTO.getEquipmentCodes());
		}
		return callGroup;
	}
	
}
