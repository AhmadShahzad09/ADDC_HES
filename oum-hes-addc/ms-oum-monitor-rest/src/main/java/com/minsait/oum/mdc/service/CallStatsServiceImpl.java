package com.minsait.oum.mdc.service;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minsait.oum.mdc.data.model.CountPerInterval;
import com.minsait.oum.mdc.data.model.dto.CountPerIntervalDTO;
import com.minsait.oum.mdc.data.model.dto.CountPerIntervalListDTO;
import com.minsait.oum.mdc.data.model.filter.CallFilter;
import com.minsait.oum.mdc.data.repository.CallRepository;

import lombok.extern.slf4j.Slf4j;

@Service
public class CallStatsServiceImpl implements CallStatsService {
	
	@Autowired
	private CallRepository callRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	public CountPerIntervalListDTO countLast24HoursCallsPerHourlyInterval() {
		// Se pediran datos para las ultimas 24 horas, tomando solo horas completas
		// Ejemplo estubieramos a 2022-09-07T12:54:30:321Z Se pediran datos entre 2022-09-06T11:00:00:000Z y 2022-09-07T23:59:59:999Z
		
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		startCalendar.add(Calendar.HOUR_OF_DAY, -24);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);		
		
		
		//Long queryStarTime =  DateUtils.setMilliseconds(DateUtils.setSeconds(DateUtils.setMinutes(DateUtils.addHours(new Date(), -24),0),0),0).getTime();
		Long queryStarTime = startCalendar.getTimeInMillis();
		Long queryEndTime =  queryStarTime + (24 * 60 * 60 * 1000) -1 ;
		
		
		List<CountPerInterval> queryResult = callRepository.hourlyCallsCount(queryStarTime, queryEndTime);
		final List<CountPerIntervalDTO> list = 
				queryResult.stream().map(x -> generateCountPerIntervalDTOWithTimeId(x, 60 * 60 * 1000l))
					.collect(Collectors.toList());
		
		CountPerIntervalListDTO result = new CountPerIntervalListDTO();
		result.setList(list);	
		result.setFirstPeriodStartDate(new Date(queryStarTime));
		result.setLastPeriodStartDate(new Date(queryStarTime +  23 *60 * 60 * 1000l));
		result.setCountUnit("call");
		result.setDescription("Last 24 hours, call performed per hour");
		
		return  result;
	}
	
	public CountPerIntervalListDTO last7DaysAverageRuntimePerDailyInterval() {
		// Se pediran datos para los ultimos 7 dias, tomando solo dias completos
		// Ejemplo estubieramos a 2022-09-07T12:54:30:321Z Se pediran datos entre 2022-08-30T00:00:00:000Z y 2022-09-06T23:59:59:999Z
		
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		startCalendar.add(Calendar.DATE, -7);
		startCalendar.set(Calendar.HOUR_OF_DAY, 0);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MILLISECOND, 0);
		
		Long queryStarTime =  startCalendar.getTimeInMillis();
		Long queryEndTime =  queryStarTime + (7 * 24 * 60 * 60 * 1000) -1 ;
				
		
		List<CountPerInterval> queryResult = callRepository.dailyCallsAverageRunTime(queryStarTime, queryEndTime);
		final List<CountPerIntervalDTO> list = 
				queryResult.stream().map(x -> generateCountPerIntervalDTOWithTimeId(x, 24 * 60 * 60 * 1000l))
					.collect(Collectors.toList());
		
		CountPerIntervalListDTO result = new CountPerIntervalListDTO();
		result.setList(list);	
		result.setFirstPeriodStartDate(new Date(queryStarTime));
		result.setLastPeriodStartDate(new Date(queryStarTime +  6 * 24 *60 * 60 * 1000l));
		result.setCountUnit("ms");
		result.setDescription("Last 7 days, average runtime per call");
		
		return  result;
	}
		
	public CountPerIntervalListDTO averageRunTimePerRequestType(CallFilter callFilter) {
		
		
		Long queryEndTime =  callFilter.getEndDate();		
		if (queryEndTime == null) {
			queryEndTime = new Date().getTime();
		}
		
		Long queryStarTime =  callFilter.getStartDate();
		if (queryStarTime == null) {
			queryStarTime = queryEndTime - ( 5 * 24 * 60 * 60 * 1000);
		}
				
		Date startDate = new Date(queryStarTime);
		Date endDate = new Date(queryEndTime);
		
		String sortFieldName = callFilter.getSortFieldName();		
		if (sortFieldName == null || !sortFieldName.equalsIgnoreCase("duration")) {			
			sortFieldName = "_id";
		} else  {
			sortFieldName = "count";
		}
		
		int sortOrder = -1;
		
		if (("_id".equals(sortFieldName) && !"name".equalsIgnoreCase( callFilter.getSortFieldName()))
				|| !"desc".equalsIgnoreCase(callFilter.getSortOrder())) {
			sortOrder = 1;
		} 
				
		
		List<CountPerInterval> queryResult = callRepository.averageRunTimePerRequestType(queryStarTime, queryEndTime, sortFieldName, sortOrder);
		final List<CountPerIntervalDTO> list = 
				queryResult.stream().map(x -> generateCountPerIntervalDTO(x, startDate, endDate))
					.collect(Collectors.toList());
		
		CountPerIntervalListDTO result = new CountPerIntervalListDTO();
		result.setList(list);	
		result.setFirstPeriodStartDate(startDate);
		result.setLastPeriodStartDate(startDate);
		result.setCountUnit("ms");
		result.setDescription("Average runtime per call of request type, betwen " + startDate + " and " + endDate);
		
		return  result;
	}
	
	private CountPerIntervalDTO generateCountPerIntervalDTOWithTimeId(CountPerInterval countPerInterval, Long intervalMilis) {
		final CountPerIntervalDTO countPerIntervalDTO = modelMapper.map(countPerInterval, CountPerIntervalDTO.class);
		
		Date intervalStarDate = Date.from(Instant.parse(countPerIntervalDTO.getId()));
		countPerIntervalDTO.setIntervalStartDate(intervalStarDate);
		
		Instant intervalEndInstant = Instant.parse(countPerIntervalDTO.getId());
		Date intervalEndDate = Date.from(intervalEndInstant.plusMillis(intervalMilis - 1));
		countPerIntervalDTO.setIntervalEndDate(intervalEndDate);
		
		return countPerIntervalDTO;
	}
	
	private CountPerIntervalDTO generateCountPerIntervalDTO(CountPerInterval countPerInterval, Date startDate, Date endDate) {
		final CountPerIntervalDTO countPerIntervalDTO = modelMapper.map(countPerInterval, CountPerIntervalDTO.class);		
		countPerIntervalDTO.setIntervalStartDate(startDate);
		countPerIntervalDTO.setIntervalEndDate(endDate);		
		return countPerIntervalDTO;
	}
	
	
	


}
