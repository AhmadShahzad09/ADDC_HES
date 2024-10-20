package com.minsait.oum.mdc.data.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.CallGroup;
import com.minsait.oum.mdc.data.model.Status;
import com.minsait.oum.mdc.data.model.filter.BaseFilter;
import com.minsait.oum.mdc.data.model.filter.GroupFilter;

public class CustomCallGroupRepositoryImpl implements CustomCallGroupRepository {
	
	@Autowired
	private MongoOperations mongoOperations;
	
//	@Override
//	public CallGroup insert(CallGroup entity) {
//		
//		if (null == entity.getId()) {
//			entity.setId(UUID.randomUUID());
//		}
//		entity.setUuid(entity.getId().toString());
//		return mongoOperations.save(entity);
//	}
	
	@Override
	public Page<CallGroup> findCallGroupsByFilter(GroupFilter filter) {
		
		Sort sort = Sort.unsorted();
		if (StringUtils.isNotBlank(filter.getSortFieldName())) {
			sort = Sort.by(filter.getSortFieldName());
		}
		else {
			sort = Sort.by(BaseFilter.DEFAULT_SORT_FIELD);
		}
		if (BaseFilter.SORT_ORDER_DESC.equalsIgnoreCase(filter.getSortOrder())) {
			sort = sort.descending();
		}
		else {
			sort = sort.ascending(); // default
		}
		final int page = filter.getFirstResult() / filter.getSizeNo();
		final int size = filter.getSizeNo();
		final Pageable pageable = PageRequest.of(page, size, sort);
		
		
		final Query query = new Query().with(pageable);
		if (filter.getId() != null) {
			query.addCriteria(Criteria.where("_id").is(filter.getId()));
		}

		if (filter.getStartDate() != null && filter.getEndDate() != null) {
			final Criteria andOperator = new Criteria().andOperator(Criteria.where("datetime").lte(filter.getEndDate()), Criteria.where("datetime").gte(filter.getStartDate()));
			query.addCriteria(andOperator);
		} else if (filter.getStartDate() != null && filter.getEndDate() == null) {
			final Criteria startDateCriteria = Criteria.where("datetime").gte(filter.getStartDate());
			query.addCriteria(startDateCriteria);
		} else if (filter.getStartDate() == null && filter.getEndDate() != null) {
			final Criteria endDateCriteria = Criteria.where("datetime").lte(filter.getEndDate());
			query.addCriteria(endDateCriteria);
		}

		if (filter.getName() != null) {
			query.addCriteria(Criteria.where("name").regex(".*" + filter.getName() + ".*"));
		}

		if (filter.getStatus() != null) {
			query.addCriteria(Criteria.where("status").is(filter.getStatus()));
		}

		if (filter.getExecutionType() != null) {
			query.addCriteria(Criteria.where("executionType").is(filter.getExecutionType()));
		}

//		if (filter.getDeviceName() != null) {
//			final Query deviceQuery = new Query();
//			deviceQuery.addCriteria(Criteria.where("tasks").elemMatch(Criteria.where("deviceName").is(filter.getDeviceName())));
//			deviceQuery.fields().include("idGroup");
//			final List<Call> callResult = mongoOperations.find(deviceQuery, Call.class);
//			final Set<String> idGroups = callResult.stream().map(call -> call.getIdGroup()).collect(Collectors.toSet());
//			query.addCriteria(Criteria.where("_id").in(idGroups));
//		}
		if (filter.getDeviceName() != null) {
			query.addCriteria(Criteria.where("equipmentCodes").is(filter.getDeviceName()));
		}
		

		final List<CallGroup> groupResult = mongoOperations.find(query, CallGroup.class);
		final Long totalElementsWithFilter = -1L; //mongoOperations.count(query.skip(-1).limit(-1), CallGroup.class);
		final Page<CallGroup> resultPage = new PageImpl<CallGroup>(groupResult , pageable, totalElementsWithFilter);
		
		return resultPage;
	}

	@Override
	public CallGroup updateStatus(CallGroup entity) {
		try {
			
			if (entity == null || entity.getId() == null)
				throw new Exception("The CallGroup entity cannot be null.");
			if (entity.getId() == null || entity.getId().isEmpty())
				throw new Exception("The CallGroup ID cannot be null or empty.");
			if (entity.getStatus() == null)
				throw new Exception("The CallGroup status cannot be null.");
			
			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(entity.getId()));
			query.fields().include("_id");
	
//			CallGroup callGroup_1 = mongoOperations.findOne(query, CallGroup.class);
	
			Update update = new Update();
			update.set("status", entity.getStatus());
			mongoOperations.updateFirst(query, update, CallGroup.class);
	
			// returns everything
			Query query1 = new Query();
			query1.addCriteria(Criteria.where("_id").is(entity.getId()));
			CallGroup callGroup_2 = mongoOperations.findOne(query1, CallGroup.class);
			
			return callGroup_2;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateStatusAndEquipmentCodes(String id, Status status, List<String> equipmentCodes) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		query.fields().include("_id");

		Update update = new Update();
		update.set("status", status);
		update.set("equipmentCodes", equipmentCodes);
		mongoOperations.updateFirst(query, update, CallGroup.class);
	}
	
}
