package com.minsait.oum.mdc.dlms.server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.indracompany.energy.dlms.cosem.addc.model.EventLogEntry;
import com.indracompany.energy.dlms.cosem.addc.server.INotificationCallback;
import com.minsait.commands.CommandOutput;
import com.minsait.commands.impl.action.AbstractAction;
import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.application.InventoryClient;
import com.minsait.oum.mdc.application.ProfileApplication;
import com.minsait.oum.mdc.converters.EventLogConverter;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.platform.PlatformDevice;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.PlatformRequest;

import gurux.common.GXCommon;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope(value = "prototype")
public class OnAlarmNotificationCallback implements INotificationCallback {

	@Autowired
	private ProfileApplication application;
	
	@Autowired
	private InventoryClient inventoryClient;
	
	@Autowired
	private EventLogConverter eventLogConverter;
	
	@Autowired
	private ApplicationContext appCntx;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${alarms.iskra.MT382.descriptor1}")
	String MT382Descriptor1;
	@Value("${alarms.iskra.MT382.descriptor2}")
	String MT382Descriptor2;
	
	@Value("${alarms.iskra.MT880.descriptor1}")
	String MT880Descriptor1;
	@Value("${alarms.iskra.MT880.descriptor2}")
	String MT880Descriptor2;
	
	@Value("${alarms.iskra.AM550.descriptor1}")
	String AM550Descriptor1;
	@Value("${alarms.iskra.AM550.descriptor2}")
	String AM550Descriptor2;

	
	@Override
	public void onData(String senderInfo, List<Object> values) {
		
		Gson gson = new Gson() ;
		String logicalDeviceName = "";
		String meterCode = "";
		String model =  "";
		List<Integer> alarmIds = new ArrayList<Integer>();
		
		log.info("onAlarm");
		int index = 0;
		for (Object value : values) {
			if (value != null) {
				if (value instanceof byte[]) {
					log.info(index++ + ": " + GXCommon.bytesToHex((byte[]) value));
				} else if (value instanceof Number) {
					log.info(index++ + ": " + value);
				} else if (value instanceof String) {
					log.info(index++ + ": " + value);
				} else {
					log.info(index++ + ": " + value);
				}
			} else {
				log.info(index++ + ": is null");
			}
		}

		try {
			logicalDeviceName = new String((byte[]) values.get(0), "UTF-8");
			values.set(2, logicalDeviceName);
		} catch (UnsupportedEncodingException|ClassCastException e) {
			logicalDeviceName = (String) values.get(2);
		}
		
		log.info("Meter logical device name: " + logicalDeviceName);
		
		JSONObject responseInventory = 	inventoryClient.getEquipmentPropsByLogicalDeviceName(logicalDeviceName);
				
		if (responseInventory == null) {
			log.error("Meter code not found for logical device name " + logicalDeviceName);
			return;
		}
		meterCode= (String)responseInventory.get("code");
		model = (String) responseInventory.get("equipmentModel");
		
		
		//Creation of call monitor entry with the info of the push event received
		List<Device> devices = new ArrayList<>();
		Device device = new Device();
		device.setSerialNumber(meterCode);
		device.setMeterStatus("OK");
		devices.add(device);

		Request onAlarmRequest = new Request();
		onAlarmRequest.setDevices(devices);
		
		onAlarmRequest.setRequestType(RequestType.ASYNCHRONOUS);
		onAlarmRequest.setResponseType(ResponseType.PUSH_ON_ALARM);
		onAlarmRequest.setPayload(gson.toJson(values));
		onAlarmRequest.setSerialnumber(meterCode);
		//onAlarmRequest.setStatus(RequestStatus.OK);
		
		alarmIds = decodeAlarm(model, ((Number)values.get(5)).longValue(), ((Number)values.get(7)).longValue());
		if (alarmIds.size() > 0) {

			// create call and update OnAlarm notification
			Call callMonitor = this.application.createCallMonitorEntry(onAlarmRequest);
			log.debug("call monitor created: " + callMonitor.getPid());
			
			responseInventory = inventoryClient.resetMeterAlarms(meterCode);
			
			//Create the detected alarms
			createEvents(alarmIds, meterCode, callMonitor.getPid());
		} else {
			Call callMonitor = this.application.createCallMonitorEntry(onAlarmRequest, Status.FINISH_OK);
			log.debug("call monitor created: " + callMonitor.getPid());
		}
		
	}
	
	private void createEvents (List<Integer> eventsIds, String meterCode, String pid) {
		
		List<EventLogEntry> eventsList = new ArrayList<EventLogEntry>();
	
		if (eventsIds.size() > 0) {
			
			for (int eventId:eventsIds) {
				EventLogEntry event = new EventLogEntry();
				event.setEventCode(eventId);
				event.setClock(new Date());
				eventsList.add(event);
			}
			
			List<PlatformMeter> platformMeters=eventLogConverter.toList(eventsList);

			PlatformDevice platformDevice = PlatformDevice.builder()
					.meterCollector(platformMeters)
					.deviceId(meterCode)
					.pid(pid)
					.taskId(1L)
					.orderId(1L)
					.requestId(1L)
					.deviceType("METER")
					.build();

			PlatformRequest platformRequest = PlatformRequest.builder()
					.device(Arrays.asList(platformDevice))
					.build();

			sendThroughActionService(eventLogConverter.getActionServiceName(),platformRequest);
		}
	}
	
	private List<Integer> decodeAlarm(String model, long descriptor1, long descriptor2) {
		
		
		log.info("MODEL: " + model + " Descriptor 1: " + descriptor1 + "Descriptor 2: " + descriptor2);
		
		switch  (model) {

		//ISKRA MT382
		case "MT382":
			return applyPattern(MT382Descriptor1, MT382Descriptor2, descriptor1, descriptor2);
		
		//ISKRA MT880
		case "MT880":
			return applyPattern(MT880Descriptor1, MT880Descriptor2, descriptor1, descriptor2);
		
		//ISKRA AM550TT
		case "AM550TT":
			return  applyPattern(AM550Descriptor1, AM550Descriptor2, descriptor1, descriptor2);
		}
		
		return null;
	}

	private List<Integer> applyPattern(String alarms1, String alarms2, long descriptor1, long descriptor2) {
	
		
		String[] alarmsIds1 = alarms1.split(",");
		String[] alarmsIds2 = alarms2.split(",");
		
		ArrayList<Integer> alarmsIds = new ArrayList<Integer>();
		
		String descriptorBits = Long.toBinaryString(descriptor1);
		descriptorBits = StringUtils.leftPad(descriptorBits,32,"0");
			
		for (int i = 0; i < descriptorBits.length(); i++){
		    char c = descriptorBits.charAt(i);        
		    if (c=='1' && !alarmsIds1[i].isEmpty()) {
		    	alarmsIds.add(Integer.parseInt(alarmsIds1[i]));
		    	log.info("Alarm to create: "  + alarmsIds1[i]);
		    }
		}
		
		descriptorBits = Long.toBinaryString(descriptor2);
		descriptorBits = StringUtils.leftPad(descriptorBits,32,"0");
			
		
		for (int i = 0; i < descriptorBits.length(); i++){
		    char c = descriptorBits.charAt(i);        
		    if (c=='1' && !alarmsIds2[i].isEmpty()) {
		    	alarmsIds.add(Integer.parseInt(alarmsIds2[i]));
		    	log.info("Alarm to create: "  + alarmsIds2[i]);
		    }
		}
		
		
		return alarmsIds;
		
	}
	
	private void sendThroughActionService(String actionServiceName, String data) {
		final CommandOutput outputResult = new CommandOutput();
		outputResult.setResult(data);
		// Se envía a kafka - MDM
		log.debug("about to send new message to kafka using action '{}': {}", actionServiceName, data);
		final AbstractAction actionService = (AbstractAction) appCntx.getBean(actionServiceName);
		actionService.executeActions(outputResult);
		log.debug("new message sent to kafka succesfully using action '{}'", actionServiceName, data);
	}

	private void sendThroughActionService(String serviceName, final PlatformRequest platformRequest) {
		String platformRequestString = "";
		try {
			platformRequestString = objectMapper.writeValueAsString(Arrays.asList(platformRequest));
		} catch (JsonProcessingException jpe) {
			throw new UnsupportedOperationException(String.format(
					"error building load profile platform request:  %s ", platformRequest.toString()));
		}
		sendThroughActionService(serviceName, platformRequestString);
	}
	

}