package com.minsait.oum.mdc.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.minsait.mdc.util.Status;

import lombok.Data;

@Data
@Document
public class EquipmentTask {

	@Id
	Long id;
	
	Long parentId;

	Long deviceId;

	String deviceName;

	String driverClass;

	String obisCode;

	Map<String, String> protocol;

	Map<String, Object> communication;
	
	Status status;

	@Indexed
	List<Order> order;
	
	Long initTime;

	Long finishTime;
	
}
