package com.minsait.oum.mdc.kafka.listener;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.indracompany.energy.dlms.cosem.addc.request.GXDLMSReader;
import com.minsait.mdc.data.model.Call;
import com.minsait.oum.mdc.domain.EquipmentTask;
import com.minsait.oum.mdc.domain.Order;
import com.minsait.oum.mdc.driver.Message;
import com.minsait.oum.mdc.kafka.publisher.TaskStatusMessageService;
import com.minsait.oum.mdc.listener.task.PlatformTaskVO;

import gurux.common.IGXMedia;
import gurux.common.enums.TraceLevel;
import gurux.dlms.GXDLMSClient;
import gurux.dlms.enums.Authentication;
import gurux.dlms.enums.InterfaceType;
import gurux.dlms.secure.GXDLMSSecureClient;
import gurux.net.GXNet;
import gurux.net.enums.NetworkType;
import lombok.extern.slf4j.Slf4j;

//import com.minsait.commands.impl.action.AbstractAction;

@Slf4j
@Component
public class MessageListener {
	
//	@Autowired
//	@Qualifier(value = "LoadProfileActionWS")
//	AbstractAction actionService;
	
	@Autowired
	private Gson gson;

	@Autowired
	private TaskStatusMessageService taskStatusMessageService;

	@Autowired
	private ApplicationContext appCntx;
	
	@Autowired
	@Qualifier("asynchronouProcessingCommands")
	private List<String> asynchronouProcessingCommands;

	@StreamListener(MessageStreamDlms.DLMS)
	public void getDlmsMsg(@Payload final Message message) throws Exception {

		log.info("RequestProcessApplication >> Getting message: {}", message.getMessage().toString());
		
		Map<Integer, String> errorMap = new LinkedTreeMap<Integer, String>();
		Map<Integer, String> infoMap = new LinkedTreeMap<Integer, String>();

		Call call = gson.fromJson(message.getMessage(), Call.class);
		
		if (CollectionUtils.isEmpty(call.getTasks())) {
			errorMap.put(errorMap.size()+1, "Tasks is empty");
			PlatformTaskVO platformTaskVO = PlatformTaskVO
					.builder()
					.pid(call.getIdDC())
					.applyToAllTasks(true)
					.error(errorMap)
					.info(infoMap)
					.build();
			taskStatusMessageService.send(platformTaskVO);

			log.info("MessageListener >> Call processed: No orders");
			return;
		}

		String serialNumber = getSerialNumber(call);
		if (StringUtils.isBlank(serialNumber)) {
			log.error("Serial number is null");
			errorMap.put(errorMap.size()+1, "Serial number is empty");
		} else {
			log.info("Serial number is " + serialNumber);
		}
		boolean applyToAllTasks = false;
		if (errorMap.isEmpty()) {
		
			log.info("MessageListener >> Processing call: {}...", call.toString());
			
			GXDLMSReader reader = getReader(call);
			try {
				infoMap.put(infoMap.size()+1, "initializingConnection");
				reader.getMedia().open();
	            reader.initializeConnection();
				infoMap.put(infoMap.size()+1, "connectionInitialized");
	            
	            for (EquipmentTask task : call.getTasks()) {
	            	List<Order> orders = task.getOrder();
	            	for (Order order : orders) {
	            		
			    		RequestCallback requestCallback = appCntx.getBean(RequestCallback.class);
						requestCallback.setEquipmentCode(task.getDeviceName());
			    		requestCallback.setCallId(call.getIdDC());
			    		requestCallback.setTaskId(task.getId());
			    		requestCallback.setOrderId(order.getId());
			    		requestCallback.setRequestId(order.getExecutionId());
			    		requestCallback.setOrderName(order.getName());
			    		
	            		try {
				    		String command = order.getName();
				    		Properties parameters = new Properties();
				    		parameters.putAll(order.getOrdersParams());
				    		
				    		requestCallback.addInfoTrace("initializingRequest: " + command);
				            
				            final com.indracompany.energy.dlms.cosem.addc.request.Request request = 
				            		(com.indracompany.energy.dlms.cosem.addc.request.Request) appCntx.getBean(command);
				            request.setId(call.getIdDC());
							request.setReader(reader);
							request.setParameters(parameters);
							request.setRequestCallback(requestCallback);
				            
							request.execute();

				    		requestCallback.addInfoTrace("requestFinished");
				    		
				    		if (!asynchronouProcessingCommands.contains(command)) {
				    			taskStatusMessageService.send(PlatformTaskVO
				    				.builder()
				    				.pid(call.getIdDC())
				    				.taskId(task.getId())
				    				.orderId(order.getId())
				    				.requestId(order.getExecutionId())
				    				.applyToAllTasks(false)
				    				.error(requestCallback.getErrorTraces())
				    				.info(requestCallback.getInfoTraces())
				    				.build());
				    		}
	            		} catch (Exception e) {
	        				e.printStackTrace();
				    		requestCallback.addErrorTrace("requestError: " + e.getMessage());
				    		taskStatusMessageService.send(PlatformTaskVO
				    				.builder()
				    				.pid(call.getIdDC())
				    				.taskId(task.getId())
				    				.orderId(order.getId())
				    				.requestId(order.getExecutionId())
				    				.applyToAllTasks(false)
				    				.error(requestCallback.getErrorTraces())
				    				.info(requestCallback.getInfoTraces())
				    				.build());
	        			}
					}
				}

				
			} catch (Exception e) {
				e.printStackTrace();
				applyToAllTasks = true;
				errorMap.put(errorMap.size()+1, e.getMessage());
			} finally {
				infoMap.put(infoMap.size()+1, "clossingConnection");
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
					reader.getMedia().close();
				}
				infoMap.put(infoMap.size()+1, "connectionClosed");
			}
		}
		
		taskStatusMessageService.send(PlatformTaskVO
				.builder()
				.pid(call.getIdDC())
				.applyToAllTasks(applyToAllTasks)
				.error(errorMap)
				.info(infoMap)
				.build());
		

		log.info("MessageListener >> Call processed");
	}

	private String getSerialNumber(Call call) {
		Map<String, Object> communication = call.getCommunication();
		
		List<Map<String, Object>> communicationProperties = (List<Map<String, Object>>) communication.get("properties");
		String serialNumber = null;
		for (Map<String, Object> map : communicationProperties) {
			if (map.get("name").equals("_SERIAL_NUMBER")) {
				serialNumber = (String) map.get("value");
				break;
			}
		}
		return serialNumber;
	}

	private GXDLMSReader getReader(Call call) {
		
//		Properties prop = new Properties();
//		prop.setProperty("ip", "176.204.236.73");
//		prop.setProperty("port", "4059");
//		prop.setProperty("useLogicalNameReferencing", "true");
//		prop.setProperty("authentication", "LOW");
//		prop.setProperty("password", "23322332");
//		prop.setProperty("interfaceType", "WRAPPER");
//		prop.setProperty("clientAddress", "1");
//		prop.setProperty("serverAddress", "1");
		
		Map<String, Object> communication = call.getCommunication();
		
		String communicationType = (String) communication.get("type");
		IGXMedia media = null;
		if (communicationType.equals("TCP/IP")) {
			
			List<Map<String, Object>> communicationProperties = (List<Map<String, Object>>) communication.get("properties");
			String ip = null;
			String port = null;
			for (Map<String, Object> map : communicationProperties) {
				if (map.get("name").equals("IP")) {
					ip = (String) map.get("value");
				} else if (map.get("name").equals("PORT")) {
					port = (String) map.get("value");
				}
			}
//			ip = "127.0.0.1";
	    	media = new GXNet(NetworkType.TCP, ip, Integer.valueOf(port));
		}
		
		Map<String, String> protocol = call.getProtocol();
		String useLogicalNameReferencing = protocol.getOrDefault("LOGICAL_NAME_REFERENCING", "NO");
		String authentication = protocol.get("AUTHENTICATION_LEVEL");
		String password = protocol.get("PASSWORD");
		String interfaceType = protocol.get("INTERFACE_TYPE");
		String clientAddress = protocol.get("CLIENT_ADDRESS");
		String serverAddress = protocol.get("SERVER_ADDRESS");
		String useSerialNumberAddressing = protocol.getOrDefault("SERIAL_NUMBER_ADDRESSING", "NO");
		String waitTime = protocol.getOrDefault("WAIT_TIME", "20000");
	    
	    GXDLMSSecureClient client = new GXDLMSSecureClient();
        client.setUseLogicalNameReferencing(useLogicalNameReferencing.equalsIgnoreCase("yes"));
        client.setAuthentication(Authentication.valueOf(authentication));
        client.setPassword(password);
        client.setInterfaceType(InterfaceType.valueOf(interfaceType));
        
        client.setClientAddress(Integer.valueOf(clientAddress));
        if (useSerialNumberAddressing.equalsIgnoreCase("yes")) {
        	client.setServerAddress(GXDLMSClient.getServerAddress(Integer.valueOf(serverAddress)));
        } else {
            client.setServerAddress(Integer.valueOf(serverAddress));
        }
        
        TraceLevel trace = TraceLevel.VERBOSE;

	    String invocationCounter = null;
	    
	    GXDLMSReader reader = new GXDLMSReader(client, media, trace, invocationCounter);
	    reader.setWaitTime(Integer.valueOf(waitTime));
        
        return reader;
	}
}
