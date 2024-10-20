package com.minsait.oum.mdc.listener.request;

import com.google.gson.GsonBuilder;
import com.minsait.com.oum.mdc.repository.CallDomain;
import com.minsait.com.oum.mdc.repository.CallGroupDomain;
import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.data.model.CallGroup;
import com.minsait.mdc.data.model.Status;
import com.minsait.oum.mdc.domain.EquipmentTask;
import com.minsait.oum.mdc.driver.BusAbstract;
import com.minsait.oum.mdc.driver.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@Slf4j
public class RequestListener {

	@Autowired
	CallDomain callDomain;

	@Autowired
	CallGroupDomain groupCallDomain;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	private ApplicationContext appCntx;

	@StreamListener(InStream.INPUT_REQUEST)
	public void handlerTaskInput(@Payload String message) {
		log.info("Message input: " + message);

		try {
			
			Call calldb = new GsonBuilder().create().fromJson(message, Call.class);

			Call savedCall = null;
			String pid = UUID.randomUUID().toString();
			long datetime = Calendar.getInstance().getTimeInMillis();
			calldb.setPid(pid);
			calldb.setIdDC(pid);
			calldb.setDatetime(datetime);
			calldb.getTasks().stream().forEach(
					task -> {
						task.setInitTime(datetime);
						task.getOrder().stream().forEach(
								order -> {
									order.setInitTime(datetime);
									order.setPayload(message);
								});
					
					});
			savedCall = callDomain.save(calldb);

			CallGroup group = null;
			try {
				group = groupCallDomain.findById(savedCall.getIdGroup()).get();
			} catch (NoSuchElementException e) {
				if (group == null) {
					group = new CallGroup();
					group.setDatetime(datetime);
					group.setExecutionType("MANUAL");
					group.setStatus(Status.READY);
					group.setName(calldb.getName());
					group.setPriority(savedCall.getPriority());
					group.setPid(savedCall.getIdGroup());
					group.setUserId(calldb.getUser());
					try {
						groupCallDomain.save(group);
					} catch (Exception ex) {
						ex.printStackTrace();
						log.error("Error inserting group - It is duplicated");
					}
				}
			}
			EquipmentTask call = savedCall.getTasks().stream().findFirst().get();
			BusAbstract bus = null;
			if (appCntx.containsBean(call.getDriverClass())) {
				bus = (BusAbstract) appCntx.getBean(call.getDriverClass());
			} else {
				String[] protocol = call.getDriverClass().split("_");
				while (protocol.length > 1 && bus == null) {
					protocol = Arrays.copyOf(protocol, protocol.length - 1);
					String beanName = StringUtils.join(protocol, "_");
					if (appCntx.containsBean(beanName)) {
						bus = (BusAbstract) appCntx.getBean(beanName);
					}
				}
			}
	
			if (bus != null) {
				JSONObject jsonObject = new JSONObject(message);
				jsonObject.put("idDC", savedCall.getIdDC());
		
				bus.setMessage(new Message(new Date().getTime(), jsonObject.toString()));
				bus.setTaskId(call.getId());
				bus.publish();
			} else {
				log.error("Bus not found for " + call.getDriverClass());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}