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

public interface CallStatsService  {
	
	public CountPerIntervalListDTO countLast24HoursCallsPerHourlyInterval();
	
	public CountPerIntervalListDTO last7DaysAverageRuntimePerDailyInterval();
		
	public CountPerIntervalListDTO averageRunTimePerRequestType(CallFilter callFilter);
	
}
