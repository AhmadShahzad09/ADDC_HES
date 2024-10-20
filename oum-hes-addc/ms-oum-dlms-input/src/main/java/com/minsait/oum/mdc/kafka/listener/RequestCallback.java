package com.minsait.oum.mdc.kafka.listener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.indracompany.energy.dlms.cosem.addc.model.BillingProfileEntry;
import com.indracompany.energy.dlms.cosem.addc.model.EnergyRegistersEntry;
import com.indracompany.energy.dlms.cosem.addc.model.EventLogEntry;
import com.indracompany.energy.dlms.cosem.addc.model.FirmwareUpgradeStatus;
import com.indracompany.energy.dlms.cosem.addc.model.InstantaneousRegistersEntry;
import com.indracompany.energy.dlms.cosem.addc.model.InstrumentationProfileEntry;
import com.indracompany.energy.dlms.cosem.addc.model.LoadProfile1Entry;
import com.indracompany.energy.dlms.cosem.addc.model.LoadProfile2Entry;
import com.indracompany.energy.dlms.cosem.addc.model.MaxDemandRegistersEntry;
import com.indracompany.energy.dlms.cosem.addc.model.MeterNamePlateInfoEntry;
import com.indracompany.energy.dlms.cosem.addc.model.MeterStatus;
import com.indracompany.energy.dlms.cosem.addc.model.PowerQualityProfileEntry;
import com.indracompany.energy.dlms.cosem.addc.model.PushSetupDestination;
import com.indracompany.energy.dlms.cosem.addc.model.SwitchStatus;
import com.indracompany.energy.dlms.cosem.addc.request.IRequestCallback;
import com.minsait.commands.CommandOutput;
import com.minsait.commands.impl.action.AbstractAction;
import com.minsait.oum.mdc.converters.BillingProfile1Converter;
import com.minsait.oum.mdc.converters.BillingProfile2Converter;
import com.minsait.oum.mdc.converters.BillingProfileConverter;
import com.minsait.oum.mdc.converters.EnergyRegistersConverter;
import com.minsait.oum.mdc.converters.EventLogConverter;
import com.minsait.oum.mdc.converters.InstantaneousRegistersConverter;
import com.minsait.oum.mdc.converters.LoadProfile1Converter;
import com.minsait.oum.mdc.converters.LoadProfile2Converter;
import com.minsait.oum.mdc.converters.MaxDemandRegistersConverter;
import com.minsait.oum.mdc.converters.PowerQualityProfileConverter;
import com.minsait.oum.mdc.kafka.publisher.TaskStatusMessageService;
import com.minsait.oum.mdc.listener.task.PlatformTaskVO;
import com.minsait.oum.mdc.platform.PlatformDevice;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.PlatformRequest;

import gurux.dlms.objects.enums.PaymentMode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope(value = "prototype")
public class RequestCallback implements IRequestCallback {
	
	@Value("${confirmPassword.url}")
	private String confirmPasswordURL;
	
	@Value("${findEquipmentStatus.url}")
	private String findEquipmentStatusURL;
	
	@Value("${updateEquipmentStatus.url}")
	private String updateEquipmentStatusURL;
	
	@Value("${addTelemetryRemoteConfiguration.url}")
	private String addTelemetryRemoteConfigurationURL;
	
	@Value("${addTelemetryRemoteConfiguration.username}")
	private String addTelemetryRemoteConfigurationUsername;
	
	@Value("${addTelemetryRemoteConfiguration.password}")
	private String addTelemetryRemoteConfigurationPassword;

	@Autowired
	private TaskStatusMessageService taskStatusMessageService;
	
	@Autowired
	private ApplicationContext appCntx;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private EventLogConverter eventLogConverter;

	@Autowired
	private LoadProfile1Converter loadProfile1Converter;

	@Autowired
	private LoadProfile2Converter loadProfile2Converter;
	
	@Autowired
	private BillingProfileConverter billingProfileConverter;
	
	@Autowired
	private BillingProfile1Converter billingProfile1Converter;
	
	@Autowired
	private BillingProfile2Converter billingProfile2Converter;
	
	@Autowired
	private PowerQualityProfileConverter powerQualityProfileConverter;
	
	@Autowired
	private EnergyRegistersConverter energyRegistersConverter;

	@Autowired
	private InstantaneousRegistersConverter instantaneousRegistersConverter;

	@Autowired
	private MaxDemandRegistersConverter maxDemandRegistersConverter;
	
	private Map<Integer, String> errorMap = new LinkedTreeMap<Integer, String>();
	private Map<Integer, String> infoMap = new LinkedTreeMap<Integer, String>();

	private String orderName;

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	
	private Long orderId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	private Long requestId;

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	private Long taskId;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	private String callId;

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	private String equipmentCode;

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	

	public void addErrorTrace(String trace) {
		errorMap.put(errorMap.size()+1, trace);
	}
	

	public Map<Integer, String> getErrorTraces() {
		return errorMap;
	}
	
	public void addInfoTrace(String trace) {
		infoMap.put(infoMap.size()+1, trace);
	}
	
	public Map<Integer, String> getInfoTraces() {
		return infoMap;
	}
	

	@Override
	public Date getSystemDate() {
		return Calendar.getInstance().getTime();
	}

	@Override
	public Object[] getFirmware(String id) {
//		String fileName = "RFWAN_4G_SIM7600E_SM_002823V1_20191218_1F08.cla";
		String fileName = "SM31_AMI_SA_HT6x3x_R7.0.1_20191215.bin7122";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		Object[] objects = new Object[2];
		objects[0] = fileName;
		objects[1] = toByteArray(inputStream);
		return objects;
	}
	
	private static byte[] toByteArray(InputStream in) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		// read bytes from the input stream and store them in buffer
		try {
			while ((len = in.read(buffer)) != -1) {
				// write bytes from the buffer into output stream
				os.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.toByteArray();
	}

	@Override
	public void onMeterPing(String id, String dateTime, String logicalDeviceName) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] TIME: %s", id, dateTime));
		log.info(String.format("[%s] LOGICAL_DEVICE_NAME: %s", id, logicalDeviceName));
		log.info("-----------------------------------------------------------------------------------------");
		try {
			String currentStatus = findEquipmentStatus();
			log.info(getEquipmentCode() + " currentStatus: " + currentStatus);
			if (!currentStatus.equals("ACTIVE")) {
				updateEquipmentStatus("ACTIVE");
			}
		} catch (Exception e) {
			log.error("Can not process equipment status: " + e.getMessage());
		}
		sendToMdmResult("Fh-m", dateTime);
		sendToMdmResult("LogicalDeviceName", logicalDeviceName);
	}

	@Override
	public void onDateTime(String id, String result) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] TIME: %s", id, result));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("Fh-m", result);
	}

	@Override
	public void onLoadProfile1Buffer(String id, List<LoadProfile1Entry> profileEntries) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info("LOAD_PROFILE_1: " + profileEntries.size());
		for (LoadProfile1Entry profileEntry : profileEntries) {
			log.info(ToStringBuilder.reflectionToString(profileEntry, ToStringStyle.MULTI_LINE_STYLE));
		}
		log.info("-----------------------------------------------------------------------------------------");
		
		if (profileEntries == null || profileEntries.isEmpty()) {
			addInfoTrace("empty list");
			PlatformTaskVO platformTaskVO = PlatformTaskVO
					.builder()
					.pid(callId)
					.orderId(orderId)
					.requestId(requestId)
					.taskId(taskId)
					.applyToAllTasks(false)
					.error(errorMap)
					.info(infoMap)
					.build();
			taskStatusMessageService.send(platformTaskVO);
			return;
		}

		log.info("------------------------------Sending to platform----------------------------------------");

		List<PlatformMeter> platformMeters=loadProfile1Converter.toList(profileEntries);

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.meterId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(loadProfile1Converter.getActionServiceName(),platformRequest);
	}	
	
	@Override
	public void onLoadProfile1CapturePeriod(String id, Integer result) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info("LOAD_PROFILE_1_CAPTURE_PERIOD: " + result);
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("LoadProfile1CapturePeriod", result);
	}

	@Override
	public void onLoadProfile2CapturePeriod(String id, Integer result) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info("LOAD_PROFILE_2_CAPTURE_PERIOD: " + result);
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("LoadProfile2CapturePeriod", result);
	}
	
	@Override
	public void onEventLogBuffer(String id, List<EventLogEntry> profileEntries) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info("EVENT_LOG: " + profileEntries.size());
		for (EventLogEntry profileEntry : profileEntries) {
			log.info(ToStringBuilder.reflectionToString(profileEntry, ToStringStyle.MULTI_LINE_STYLE));
		}
		log.info("-----------------------------------------------------------------------------------------");		
		
		if (profileEntries == null || profileEntries.isEmpty()) {
			addInfoTrace("empty list");
			PlatformTaskVO platformTaskVO = PlatformTaskVO
					.builder()
					.pid(callId)
					.orderId(orderId)
					.requestId(requestId)
					.taskId(taskId)
					.applyToAllTasks(false)
					.error(errorMap)
					.info(infoMap)
					.build();
			taskStatusMessageService.send(platformTaskVO);
			return;
		}

		log.info("------------------------------Sending to platform----------------------------------------");
		
		List<PlatformMeter> platformMeters=eventLogConverter.toList(profileEntries);

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.deviceId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.deviceType("METER")
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(eventLogConverter.getActionServiceName(),platformRequest);
	}	
	
	@Override
	public void onBillingProfile1Buffer(String id,List<BillingProfileEntry> profileEntries) {

		
		log.info("-----------------------------------------------------------------------------------------");
		log.info("BILLING_PROFILE_1: " + profileEntries.size());
		for (BillingProfileEntry profileEntry : profileEntries) {
			log.info(ToStringBuilder.reflectionToString(profileEntry, ToStringStyle.MULTI_LINE_STYLE));
		}
		log.info("-----------------------------------------------------------------------------------------");		
		
		if (profileEntries == null || profileEntries.isEmpty()) {
			addInfoTrace("empty list");
			PlatformTaskVO platformTaskVO = PlatformTaskVO
					.builder()
					.pid(callId)
					.orderId(orderId)
					.requestId(requestId)
					.taskId(taskId)
					.applyToAllTasks(false)
					.error(errorMap)
					.info(infoMap)
					.build();
			taskStatusMessageService.send(platformTaskVO);
			return;
		}
		
		log.info("------------------------------Sending to platform----------------------------------------");
		
		List<PlatformMeter> platformMeters=billingProfile1Converter.toList(profileEntries);

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.meterId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(billingProfile1Converter.getActionServiceName(),platformRequest);
	}
	
	@Override
	public void onBillingProfile2Buffer(String id, List<BillingProfileEntry> profileEntries) {
		
		log.info("-----------------------------------------------------------------------------------------");
		log.info("BILLING_PROFILE_2: " + profileEntries.size());
		for (BillingProfileEntry profileEntry : profileEntries) {
			log.info(ToStringBuilder.reflectionToString(profileEntry, ToStringStyle.MULTI_LINE_STYLE));
		}
		log.info("-----------------------------------------------------------------------------------------");		
		
		if (profileEntries == null || profileEntries.isEmpty()) {
			addInfoTrace("empty list");
			PlatformTaskVO platformTaskVO = PlatformTaskVO
					.builder()
					.pid(callId)
					.orderId(orderId)
					.requestId(requestId)
					.taskId(taskId)
					.applyToAllTasks(false)
					.error(errorMap)
					.info(infoMap)
					.build();
			taskStatusMessageService.send(platformTaskVO);
			return;
		}
		
		log.info("------------------------------Sending to platform----------------------------------------");
		
		List<PlatformMeter> platformMeters=billingProfile2Converter.toList(profileEntries);

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.meterId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(billingProfile2Converter.getActionServiceName(),platformRequest);
	}

	@Override
	public void onLoadProfile2Buffer(String id, List<LoadProfile2Entry> profileEntries) {

		log.info("-----------------------------------------------------------------------------------------");
		log.info("LOAD_PROFILE_2: " + profileEntries.size());
		for (LoadProfile2Entry profileEntry : profileEntries) {
			log.info(ToStringBuilder.reflectionToString(profileEntry, ToStringStyle.MULTI_LINE_STYLE));
		}
		log.info("-----------------------------------------------------------------------------------------");

		if (profileEntries == null || profileEntries.isEmpty()) {
			addInfoTrace("empty list");
			PlatformTaskVO platformTaskVO = PlatformTaskVO
					.builder()
					.pid(callId)
					.orderId(orderId)
					.requestId(requestId)
					.taskId(taskId)
					.applyToAllTasks(false)
					.error(errorMap)
					.info(infoMap)
					.build();
			taskStatusMessageService.send(platformTaskVO);
			return;
		}

		log.info("------------------------------Sending to platform----------------------------------------");

		List<PlatformMeter> platformMeters=loadProfile2Converter.toList(profileEntries);

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.meterId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(loadProfile2Converter.getActionServiceName(),platformRequest);
	}

	@Override
	public void onConnectionState(String id, boolean state) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info("ConnectionState = " + state);
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("Status", state ? "CONNECTED" : "DISCONNECTED");
	}

	@Override
	public void onDaylightsSavingsDates(String id, String daylightsSavingsTimeBegin, String daylightsSavingsTimeEnd) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] dst Begin TIME: %s", id, daylightsSavingsTimeBegin.toString()));
		log.info(String.format("[%s] dst End TIME: %s", id, daylightsSavingsTimeEnd.toString()));
		log.info("-----------------------------------------------------------------------------------------");
	}

	@Override
	public void onRegisterValue(String id, Object register, String dateTime) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] %s, %s", id, register.toString(), dateTime));
		log.info("-----------------------------------------------------------------------------------------");
	}

	@Override
	public void onActiveActivityCalendar(String id, String activeActivityCalendar) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info("ACTIVE_ACTIVITY_CALENDAR: " + activeActivityCalendar);
		log.info(ToStringBuilder.reflectionToString(activeActivityCalendar, ToStringStyle.MULTI_LINE_STYLE));
		log.info(activeActivityCalendar.toString());
		log.info("-----------------------------------------------------------------------------------------");
	}

	@Override
	public void onPassiveActivityCalendar(String id, String passiveActivityCalendar) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info("PASSIVE_ACTIVITY_CALENDAR: " + passiveActivityCalendar);
		log.info(ToStringBuilder.reflectionToString(passiveActivityCalendar, ToStringStyle.MULTI_LINE_STYLE));
		log.info(passiveActivityCalendar.toString());
		log.info("-----------------------------------------------------------------------------------------");
	}

	@Override
	public void onTrace(String id, String trace) {
		log.info(trace);

	}

	@Override
	public void onPowerQualityProfileBuffer(String id, List<PowerQualityProfileEntry> profileEntries) {
		
		log.info("-----------------------------------------------------------------------------------------");
		log.info("POWER_QUALITY_PROFILE: " + profileEntries.size());
		for (PowerQualityProfileEntry profileEntry : profileEntries) {
			log.info(ToStringBuilder.reflectionToString(profileEntry, ToStringStyle.MULTI_LINE_STYLE));
		}
		log.info("-----------------------------------------------------------------------------------------");
		
		if (profileEntries == null || profileEntries.isEmpty()) {
			addInfoTrace("empty list");
			PlatformTaskVO platformTaskVO = PlatformTaskVO
					.builder()
					.pid(callId)
					.orderId(orderId)
					.requestId(requestId)
					.taskId(taskId)
					.applyToAllTasks(false)
					.error(errorMap)
					.info(infoMap)
					.build();
			taskStatusMessageService.send(platformTaskVO);
			return;
		}
		
		log.info("------------------------------Sending to platform----------------------------------------");
		
		List<PlatformMeter> platformMeters= powerQualityProfileConverter.toList(profileEntries);

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.meterId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(powerQualityProfileConverter.getActionServiceName(),platformRequest);
	}
	@Override
	public void onInstrumentationProfileBuffer(String id, List<InstrumentationProfileEntry> profileEntries) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info("INSTRUMENTATION_PROFILE: " + profileEntries.size());
		for (InstrumentationProfileEntry profileEntry : profileEntries) {
			log.info(ToStringBuilder.reflectionToString(profileEntry, ToStringStyle.MULTI_LINE_STYLE));
		}
		log.info("-----------------------------------------------------------------------------------------");
		
		if (profileEntries == null || profileEntries.isEmpty()) {
			addInfoTrace("empty list");
			PlatformTaskVO platformTaskVO = PlatformTaskVO
					.builder()
					.pid(callId)
					.orderId(orderId)
					.requestId(requestId)
					.taskId(taskId)
					.applyToAllTasks(false)
					.error(errorMap)
					.info(infoMap)
					.build();
			taskStatusMessageService.send(platformTaskVO);
			return;
		}
	}

	private void sendToMdmResult(String dataType, Integer result) {
		sendToMdmResult(dataType, result.toString());
	}
	
	private String findEquipmentStatus() {
		if (StringUtils.isNotBlank(findEquipmentStatusURL)) {

			HttpHeaders httpHeaders = new HttpHeaders() {
				{
					String auth = addTelemetryRemoteConfigurationUsername + ":" + addTelemetryRemoteConfigurationPassword;
					byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
					String authHeader = "Basic " + new String(encodedAuth);
					set("Authorization", authHeader);
				}
			};
			
			HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);
			log.info("findEquipmentStatusURL: ");
			ResponseEntity<String> response = new RestTemplate().exchange(
					UriUtils.encodePath(findEquipmentStatusURL + "/" + getEquipmentCode(), "UTF-8"),
					HttpMethod.GET, httpEntity, String.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				log.info("findEquipmentStatusURL: " + response.toString());
				JSONObject responseBody = new JSONObject(response.getBody());
				String status = responseBody.getString("status");
				if (StringUtils.isNotEmpty(status)) {
					return status;
				}
			}
		}
		return StringUtils.EMPTY;
	}
	
	private void updateEquipmentStatus(String status) {
		if (StringUtils.isNotBlank(updateEquipmentStatusURL)) {
			log.info("Sending new data to WS");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("serialNumber", getEquipmentCode());
			jsonObject.put("status", status);

			HttpHeaders httpHeaders = new HttpHeaders() {
				{
					String auth = addTelemetryRemoteConfigurationUsername + ":" + addTelemetryRemoteConfigurationPassword;
					byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
					String authHeader = "Basic " + new String(encodedAuth);
					set("Authorization", authHeader);
				}
			};
			
			HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), httpHeaders);
			log.info("updateEquipmentStatus: ");
			ResponseEntity<String> changeStatusResponse = new RestTemplate().exchange(updateEquipmentStatusURL, HttpMethod.POST, httpEntity, String.class);
			if (changeStatusResponse.getStatusCode().equals(HttpStatus.OK)) {
				log.info("updateEquipmentStatus: " + changeStatusResponse.toString());
			}
		}
	}
	
	private void sendToMdmResult(String dataType, String result) {
		
		if (StringUtils.isNotBlank(addTelemetryRemoteConfigurationURL)) {
			log.info("Sending new data to WS");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("serialNumber", getEquipmentCode());
			jsonObject.put("dataType", dataType);
			jsonObject.put("value", result);
			jsonObject.put("registerDate", System.currentTimeMillis());

			HttpHeaders httpHeaders = new HttpHeaders() {
				{
					String auth = addTelemetryRemoteConfigurationUsername + ":" + addTelemetryRemoteConfigurationPassword;
					byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
					String authHeader = "Basic " + new String(encodedAuth);
					set("Authorization", authHeader);
				}
			};
			
			HttpEntity<String> httpEntity = new HttpEntity<String>(jsonObject.toString(), httpHeaders);
			log.info("addTelemetryRemoteConfiguration: ");
			ResponseEntity<String> changeStatusResponse = new RestTemplate().exchange(addTelemetryRemoteConfigurationURL, HttpMethod.POST, httpEntity, String.class);
			if (changeStatusResponse.getStatusCode().equals(HttpStatus.OK)) {
				log.info("addTelemetryRemoteConfiguration: " + changeStatusResponse.toString());
			}
		}

		PlatformTaskVO platformTaskVO = PlatformTaskVO
				.builder()
				.pid(callId)
				.orderId(orderId)
				.requestId(requestId)
				.taskId(taskId)
				.applyToAllTasks(false)
				.error(errorMap)
				.info(infoMap)
				.build();
		taskStatusMessageService.send(platformTaskVO);
	}
	
	private void confirmPasswordToMdm() {
		
		if (StringUtils.isNotBlank(confirmPasswordURL)) {
			log.info("Sending new data to WS");

			HttpHeaders httpHeaders = new HttpHeaders() {
				{
					String auth = addTelemetryRemoteConfigurationUsername + ":" + addTelemetryRemoteConfigurationPassword;
					byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
					String authHeader = "Basic " + new String(encodedAuth);
					set("Authorization", authHeader);
				}
			};
			
			HttpEntity<String> httpEntity = new HttpEntity<String>(new Gson().toJson(getEquipmentCode()), httpHeaders);
			log.info("confirmPasswordURL: ");
			ResponseEntity<String> changeStatusResponse = new RestTemplate().exchange(confirmPasswordURL, HttpMethod.POST, httpEntity, String.class);
			if (changeStatusResponse.getStatusCode().equals(HttpStatus.OK)) {
				log.info("confirmPasswordURL: " + changeStatusResponse.toString());
			}
		}

		PlatformTaskVO platformTaskVO = PlatformTaskVO
				.builder()
				.pid(callId)
				.orderId(orderId)
				.requestId(requestId)
				.taskId(taskId)
				.applyToAllTasks(false)
				.error(errorMap)
				.info(infoMap)
				.build();
		taskStatusMessageService.send(platformTaskVO);
	}

	@Override
	public void onInstrumentationProfileCapturePeriod(String id, Integer capturePeriod) {
		sendToMdmResult("InstrumentationProfileCapturePeriod", capturePeriod);
	}

	@Override
	public void onPowerQualityProfileCapturePeriod(String id, Integer capturePeriod) {
		sendToMdmResult("PowerQualityProfileCapturePeriod", capturePeriod);
	}

	@Override
	public void onBillingDate(String id, String billingDate) {
		sendToMdmResult("BillingDate", billingDate);
	}
	
	@Override
	public void onPaymentMode(String id, PaymentMode paymentMode) {
		sendToMdmResult("PaymentMode", paymentMode.name());
	}

	@Override
	public void onPushSetupDestination(String id, PushSetupDestination pushSetupDestination) {
		sendToMdmResult("PushDestinationAddress", new GsonBuilder()
			    .serializeNulls()
			    .create().toJson(pushSetupDestination));
	}

	@Override
	public void onMeterStatus(String id, MeterStatus meterStatus) {
		sendToMdmResult("MeterStatus", new GsonBuilder()
			    .serializeNulls()
			    .create().toJson(meterStatus));
	}
	
	@Override
	public void onEnergyRegisters(String id, EnergyRegistersEntry energyRegistersEntry) {
		
		log.info("-----------------------------------------------------------------------------------------");
		log.info("ENERGY_REGISTERS: " );
		log.info(ToStringBuilder.reflectionToString(energyRegistersEntry, ToStringStyle.MULTI_LINE_STYLE));
		log.info("-----------------------------------------------------------------------------------------");
		
		List<PlatformMeter> platformMeters = energyRegistersConverter.toList(Arrays.asList(energyRegistersEntry));

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.meterId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(energyRegistersConverter.getActionServiceName(), platformRequest);
	}
		
	
	@Override
	public void onMaxDemandRegisters(String id, MaxDemandRegistersEntry maxDemandRegistersEntry) {

		log.info("-----------------------------------------------------------------------------------------");
		log.info("MAX_DEMAND_REGISTERS: " );
		log.info(ToStringBuilder.reflectionToString(maxDemandRegistersEntry, ToStringStyle.MULTI_LINE_STYLE));
		log.info("-----------------------------------------------------------------------------------------");

		List<PlatformMeter> platformMeters= maxDemandRegistersConverter.toList(Arrays.asList(maxDemandRegistersEntry));

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.meterId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(maxDemandRegistersConverter.getActionServiceName(),platformRequest);

	}
	
	@Override
	public void onInstantaneousRegisters(String id, InstantaneousRegistersEntry instantaneousRegistersEntry) {

		log.info("-----------------------------------------------------------------------------------------");
		log.info("INSTANTANEOUS_REGISTERS: " );
		log.info(ToStringBuilder.reflectionToString(instantaneousRegistersEntry, ToStringStyle.MULTI_LINE_STYLE));
		log.info("-----------------------------------------------------------------------------------------");

		List<PlatformMeter> platformMeters= instantaneousRegistersConverter.toList(Arrays.asList(instantaneousRegistersEntry));

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.meterId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(instantaneousRegistersConverter.getActionServiceName(),platformRequest);
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

		@Override
	public void onDisconnect(String id, long value) {
		System.out.println(id + ": " + value);
	}

	@Override
	public void onConnect(String id, long value) {
		System.out.println(id + ": " + value);
	}

	@Override
	public void onSwitchStatus(String id, SwitchStatus switchStatus) {
		sendToMdmResult("SwitchStatus", new GsonBuilder()
			    .serializeNulls()
			    .create().toJson(switchStatus));
	}


	public void onLoadLimitThreshold(String id, Integer status, Integer duration){
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] DURATION: %s", id, duration));
		log.info(String.format("[%s] STATUS: %s", id, status));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("ThresholdActive", status);
		sendToMdmResult("ThresholdDuration", duration);
	}


	public void onVoltRangeUp(String id, Integer result){
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] RESULT: %s", id, result));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("range_up_status", result);
	}

	public void onVoltRangeLow(String id, Integer result){
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] RESULT: %s", id, result));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("range_low_status", result);
	}

	public void onDemandIntegrationPeriod(String id, Integer result){
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] RESULT: %s", id, result));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("DemandInterval", result);
	}

	public void onMeterNamePlateInfo (String id, MeterNamePlateInfoEntry meterNamePlate){

		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] RESULT: %s", id, meterNamePlate));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("firmware-version", meterNamePlate.getFirmwareVersion());
		sendToMdmResult("address", meterNamePlate.getMeterAddress());
		sendToMdmResult("ct-numerator", meterNamePlate.getCtNumerator());
		sendToMdmResult("ct-denominator", meterNamePlate.getCtDenominator());
		sendToMdmResult("vt-numerator", meterNamePlate.getVtNumerator());
		sendToMdmResult("vt-denominator", meterNamePlate.getVtDenominator());
		sendToMdmResult("NS", meterNamePlate.getCustomerSerialNumber());
		sendToMdmResult("device-id", meterNamePlate.getManufacturerSerialNumber());

	}

	@Override
	public void onFirmwareUpgradeStatus(String id, FirmwareUpgradeStatus firmwareUpgradeStatus) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] RESULT: %s", id, firmwareUpgradeStatus));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("FirmwareUpgradeStatus", new GsonBuilder()
			    .serializeNulls()
			    .create().toJson(firmwareUpgradeStatus));
		
	}

	@Override
	public void onCurrentActiveTariff(String id, String currentActiveTariff) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] RESULT: %s", id, currentActiveTariff));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("CurrentActiveTariff", new GsonBuilder()
			    .serializeNulls()
			    .create().toJson(currentActiveTariff));
		
	}

	@Override
	public void onActiveCalendar(String id, String activeCalendar) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] RESULT: %s", id, activeCalendar));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("ActiveCalendar", new GsonBuilder()
			    .serializeNulls()
			    .create().toJson(activeCalendar));
	}

	@Override
	public void onPasiveCalendar(String id, String pasiveCalendar) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] RESULT: %s", id, pasiveCalendar));
		log.info("-----------------------------------------------------------------------------------------");
		sendToMdmResult("PasiveCalendar", new GsonBuilder()
			    .serializeNulls()
			    .create().toJson(pasiveCalendar));
	}

	@Override
	public void onBillingProfileBuffer(String id, List<BillingProfileEntry> profileEntries) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info("BILLING_PROFILE: " + profileEntries.size());
		for (BillingProfileEntry profileEntry : profileEntries) {
			log.info(ToStringBuilder.reflectionToString(profileEntry, ToStringStyle.MULTI_LINE_STYLE));
		}
		log.info("-----------------------------------------------------------------------------------------");
		
		if (profileEntries == null || profileEntries.isEmpty()) {
			addInfoTrace("empty list");
			PlatformTaskVO platformTaskVO = PlatformTaskVO
					.builder()
					.pid(callId)
					.orderId(orderId)
					.requestId(requestId)
					.taskId(taskId)
					.applyToAllTasks(false)
					.error(errorMap)
					.info(infoMap)
					.build();
			taskStatusMessageService.send(platformTaskVO);
			return;
		}
		
		log.info("------------------------------Sending to platform----------------------------------------");
		
		List<PlatformMeter> platformMeters = billingProfileConverter.toList(profileEntries);

		PlatformDevice platformDevice = PlatformDevice.builder()
				.meterCollector(platformMeters)
				.meterId(equipmentCode)
				.pid(callId)
				.taskId(taskId)
				.orderId(orderId)
				.requestId(requestId)
				.build();

		PlatformRequest platformRequest = PlatformRequest.builder()
				.device(Arrays.asList(platformDevice))
				.build();

		sendThroughActionService(billingProfileConverter.getActionServiceName(), platformRequest);
	}

	@Override
	public void onMeterPasswordUpdated(String id) {
		log.info("-----------------------------------------------------------------------------------------");
		log.info(String.format("[%s] RESULT", id));
		log.info("-----------------------------------------------------------------------------------------");
		confirmPasswordToMdm();
	}

}
