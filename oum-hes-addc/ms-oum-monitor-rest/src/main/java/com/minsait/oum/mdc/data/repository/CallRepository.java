package com.minsait.oum.mdc.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.minsait.oum.mdc.data.model.Call;
import com.minsait.oum.mdc.data.model.CountPerInterval;
import com.minsait.oum.mdc.data.model.dto.CallDTO;

@Repository
public interface CallRepository extends MongoRepository<Call, String>, CustomCallRepository {

	public Page<Call> findByIdGroup(Pageable pageable, String idGroup);

	public List<Call> findByIdGroup(String idGroup);

	public Long countByIdGroup(String idGroup);

	public Page<Call> findByIdGroupAndDatetimeBetween(Pageable pageable, String idGroup, Long from, Long to);

	@Query("{'datetime':{ $gte: ?0, $lte: ?1 }, 'idGroup':?2}")
	public List<CallDTO> findestoy(Long from, Long to, String idGroup);

	@Query("{'idDC': ?0}")
	public Call findbyIdC(String idDC);
	
	
	@Aggregation(pipeline = {
			"{$match:{datetime:{$gte:?0, $lt  :?1}}}",
			"{$project: {_id:1, yearMonthDayHourUTC: { $dateToString: { format: '%Y-%m-%dT%H:00:00Z', date: {$toDate: '$datetime'} } }}}",
			"{$group: { _id: '$yearMonthDayHourUTC', count: { $sum: 1 } } }",
			"{$sort : {_id:1}}",
	})
	List<CountPerInterval> hourlyCallsCount(long fromTimestamp, long toTimestamp);
	
//	@Aggregation(pipeline = {
//			"{$match:{datetime:{$gte:?0, $lt  :?1}}}",
//			"{$project: {_id:1, yearMonthDay: { $dateToString: { format: '%Y-%m-%dT00:00:00Z', date: {$toDate: '$datetime'} } }, runTime : { $subtract: [ '$finishDate', '$datetime' ] } }}",
//			"{$group: { _id: '$yearMonthDay', count: { $sum: 1 } } }",
//			"{$sort : {_id:1}}",
//	})
//	List<CountPerInterval> dailyCallsRunTime(Long fromTimestamp, Long toTimestamp);
	
	@Aggregation(pipeline = {
			"{$match:{datetime:{$gte:?0, $lt  :?1}}}",
			"{$project: {_id:1, yearMonthDay: { $dateToString: { format: '%Y-%m-%dT00:00:00Z', date: {$toDate: '$datetime'} } }, runTime : { $subtract: [ '$finishDate', '$datetime' ] } }}",
			"{$group: { _id: '$yearMonthDay', totalRunTime: { $sum: '$runTime' }, totalCalls: { $sum: 1 } } }",
			"{$project: { _id: '$_id', count: {$toInt:{ $divide: ['$totalRunTime','$totalCalls'] }} } }",
			"{$sort : {_id:1}}",
	})
	List<CountPerInterval> dailyCallsAverageRunTime(long fromTimestamp, long toTimestamp);
	
	
	@Aggregation(pipeline = {
			"{$match:{datetime:{$gte:?0, $lt  :?1},executionType: {$ne:'MANUAL'}}}",
			"{$project: {_id:1, name:1, runTime : { $subtract: [ '$finishDate', '$datetime' ] } }}",
			"{$group: { _id: '$name', totalRunTime: { $sum: '$runTime' }, totalCalls: { $sum: 1 } } }",
			"{$project: { _id: '$_id', count: {$toInt:{ $divide: ['$totalRunTime','$totalCalls'] }} } }",
			"{$sort : {?2:?3}}",
	})
	List<CountPerInterval> averageRunTimePerRequestType(long fromTimestamp, long toTimestamp, String sortField, int sortOrder);
}