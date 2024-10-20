package com.minsait.oum.mdc.data.repository;

import java.util.Comparator;
import java.util.List;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.Task;
import com.minsait.oum.mdc.data.model.filter.BaseFilter;
import com.minsait.oum.mdc.data.model.filter.CallFilter;
import com.minsait.oum.mdc.data.model.filter.TaskFilter;

public class CustomCallRepositoryImpl implements CustomCallRepository {
	
	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Page<Task> findTasksByFilter(Call call, TaskFilter filter) {
		
		final List<Task> tasks = call.getTasks();

		// FILTRO
		Stream<Task> filtered = tasks.stream();
		if (null != filter.getId()) {
			filtered = filtered.filter(x -> Long.valueOf(filter.getId()).equals(x.getId()));
		}
		if (null != filter.getDeviceId()) {
			filtered = filtered.filter(x -> filter.getDeviceId().equals(x.getDeviceId()));
		}
		if (null != filter.getStatus()) {
			filtered = filtered.filter(x -> filter.getStatus().equals(x.getStatus()));
		}
		if (null != filter.getOrderName()) {
			filtered = filtered.filter(t -> t.getOrder().stream().anyMatch(p -> p.getName().equals(filter.getOrderName())));
		}

		// ORDENACIÓN
		Comparator<Task> comparator = Comparator.comparing(Task::getId);
		if ("_id".equals(filter.getSortFieldName())) {
			comparator = Comparator.comparing(Task::getId);
		} else if ("equipmentId".equals(filter.getSortFieldName())) {
			comparator = Comparator.comparing(Task::getDeviceId);
		} else if ("status".equals(filter.getSortFieldName())) {
			comparator = Comparator.comparing(Task::getStatus);
		}
		if (BaseFilter.SORT_ORDER_DESC.equalsIgnoreCase(filter.getSortOrder())) {
			comparator = comparator.reversed();
		}
		
		final int skip = (int) ((filter.getFirstResult()) * 1);
		final Sort sort = Sort.unsorted();
		final int page = filter.getFirstResult() / filter.getSizeNo();
		final int size = filter.getSizeNo();
		final Pageable pageable = PageRequest.of(page, size, sort);
		
		final List<Task> taskResult = filtered.parallel().sorted(comparator).skip(skip).limit(size).collect(Collectors.toList());
		final Long totalElementsWithFilter = (long) tasks.size();
		final Page<Task> resultPage = new PageImpl<Task>(taskResult , pageable, totalElementsWithFilter);
		
		return resultPage;
	}
	
	@Override
	public Page<Call> findCallsByFilter(CallFilter filter) {
		
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
		if (filter.getIdGroup() != null) {
			final Criteria criteria = Criteria.where("idGroup").is(filter.getIdGroup());
			query.addCriteria(criteria);
		}

		if (filter.getId() != null) {
			final Criteria criteria = Criteria.where("id").is(filter.getId());
			query.addCriteria(criteria);
		}

		if (filter.getDeviceId() != null) {
			final Criteria criteria = Criteria.where("tasks.deviceId").is(filter.getDeviceId());
			query.addCriteria(criteria);
		}

		if (filter.getStatus() != null) {
			final Criteria criteria = Criteria.where("status").is(filter.getStatus());
			query.addCriteria(criteria);
		}

		if (filter.getStartDate() != null && filter.getEndDate() != null) {
			final Criteria andOperator = new Criteria().andOperator(Criteria.where("datetime").lte(filter.getEndDate()), Criteria.where("datetime").gte(filter.getStartDate()));
			query.addCriteria(andOperator);
		} else if (filter.getStartDate() != null) {
			final Criteria criteria = Criteria.where("datetime").gte(filter.getStartDate());
			query.addCriteria(criteria);
		} else if (filter.getEndDate() != null) {
			final Criteria criteria = Criteria.where("datetime").lte(filter.getEndDate());
			query.addCriteria(criteria);
		}

		if (filter.getEndPoint() != null) {
			final String[] ipAndPort = filter.getEndPoint().split(":", 2);
			if (!ipAndPort[0].isEmpty()) {
				final Criteria criteria = Criteria.where("tasks.protocol.IP_DCU").is(ipAndPort[0]);
				query.addCriteria(criteria);
			}
			if (ipAndPort.length > 1) {
				final Criteria criteria = Criteria.where("tasks.protocol.PORT_DCU").is(ipAndPort[1]);
				query.addCriteria(criteria);
			}
		}
		
		final List<Call> callResult = mongoOperations.find(query, Call.class);
		final Long totalElementsWithFilter = mongoOperations.count(query.skip(-1).limit(-1), Call.class);
		final Page<Call> resultPage = new PageImpl<Call>(callResult , pageable, totalElementsWithFilter);
		
		return resultPage;
//
//		Query queryPageable = Query.of(query).with(pageable);
//
//		List<Call> list = mongoOps.find(queryPageable, Call.class);
//		Long totalCalls = mongoOps.count(query, Call.class);
//		Page<Call> resultPage = new PageImpl<Call>(list, pageable, totalCalls);
//
//		List<ICallLevel1> targetList = mapResult(resultPage.toList());
//
//		ICallGroupLevel1Dto dtoOut = new ICallGroupLevel1Dto();
//
//		Integer nPages = (int) Math.ceil((Double.valueOf(totalCalls) / filter.getSizeNo()));
//		dtoOut.setData(targetList);
//		dtoOut.setPages(nPages);
//		dtoOut.setPageSize(filter.getSizeNo().intValue());
//		dtoOut.setTotal(totalCalls.intValue());
//		return dtoOut;
		
		
		

	}
	
	
}
