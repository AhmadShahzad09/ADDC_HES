package com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.v09;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.google.gson.Gson;
import com.minsait.commands.CommandOutput;
import com.minsait.commands.impl.action.AbstractAction;
import com.minsait.dcu.DcuParameter;
import com.minsait.dcu.DcuParameterEntry;
import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.AppConstants;
import com.minsait.mdc.util.MdmRestClient;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.application.ProfileApplication;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.EquipmentTask;
import com.minsait.oum.mdc.domain.HeartBeat;
import com.minsait.oum.mdc.domain.Order;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.domain.ResultVO;
import com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.HearBeatMDMProfile;
import com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.HeartBeatException;
import com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.HeartBeatMDM;
import com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.HeartBeatProcessResult;
import com.minsait.oum.mdc.mqtt.input.gateway.heartbeat.MyHeartBeatRunnable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatHandler implements MessageHandler {
	@Autowired
	private ProfileApplication application;

	@Autowired
	@Qualifier(value = "HeartBeatAdapter_V9")
	private HeartBeatAdapter adapter;

	@Value("${param.gateway.params}")
	private String gatewayParam;

	@Value("${param.find.meter}")
	private String findMeter;

	@Value("${changeip.gateway}")
	private String changeIpWS;

	@Value("${user.hb}")
	private String userHb;

	@Value("${pass.hb}")
	private String passHb;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Gson gson;

	private MdmRestClient _restClient;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		try {
			Request request = null;

			log.debug("==> Message: " + message.getHeaders() + "->" + message.getPayload());
			try {
				request = adapter.convert(message.getPayload().toString());
				log.info("[HeartBeatAdapter] request id: " + request.getIdRequest());
			} catch (HeartBeatException hbx) {
				request = hbx.getRequest();
				request.setErrorMessage(hbx.getMessage());
				request.setResponseType(ResponseType.HEART_BEAT);

			}
			Call callMonitor = this.application.createCallMonitorEntry(request);
			if (callMonitor != null) {
				ResultVO resultVO = process(callMonitor, request);
				application.updateCallStatus(callMonitor, resultVO.getStatus(), resultVO.getMessage());
			}

		} catch (Exception ex) {
			log.error("ERROR: " + ex.getMessage());
		}

	}

	private ResultVO process(Call call, Request request) {
		HeartBeatProcessResult result = HeartBeatProcessResult.from(null, new ResultVO().setStatus(Status.ERROR));
		log.debug("process >> Processing HeartBeat..");

		log.debug("LoadProfileProcess >> Processing heartbeat");

		List<DcuParameter> dcuParamList = new ArrayList<DcuParameter>();
		// List<E>
		for (EquipmentTask equipment : call.getTasks()) {
			HearBeatMDMProfile profile = new HearBeatMDMProfile();
			List<HeartBeatMDM> listProfiles = new ArrayList<HeartBeatMDM>();
			HeartBeatMDM hertBeatMDM = new HeartBeatMDM();
			hertBeatMDM.setDate(String.valueOf(new Date().getTime()));
			hertBeatMDM.setMeterCode(equipment.getDeviceName());
			listProfiles.add(hertBeatMDM);
			profile.setMeterCollector(listProfiles);
			profile.setPid(call.getPid());
			profile.setOrderId(1l);
			profile.setTaskId(equipment.getId());
			profile.setRequestId(equipment.getOrder().get(0).getRequestId());
			// TODO 1.- Hacer llamada al WS que nos devuelve la info sobre la gateway y sus
			// hijos

			// solo info de los meterse q no estan comisionados
			// TODO 2.- Hacer llamada COAP y esperar a la respuesta.
			String ip = request.getDevices().get(0).getIp();
			Callable<HeartBeatProcessResult> myHearBeatCallable = new MyHeartBeatRunnable(_restClient, ip,
					equipment.getDeviceName());
			FutureTask<HeartBeatProcessResult> futureTask = new FutureTask<>(myHearBeatCallable);
			Thread mythread = new Thread(futureTask);
			log.info("Thread with id:" + mythread.getId());
			mythread.start();

			try {
				result = futureTask.get();
			} catch (InterruptedException | ExecutionException e) {
				log.error(e.getMessage(), e);
				return new ResultVO().setStatus(Status.ERROR);
			}

//			Device currentDevice = super.getDeviceByName(equipment.getDeviceName());
			Device currentDevice = mapDevice(equipment.getDeviceName(), request);
			if (equipment != null) {
				List<Order> orders = equipment.getOrder();
				for (Order actual : orders) {
					DcuParameter kafkaMessage = this.buildMDMKafkaMessage(call.getPid(), actual.getId(),
							equipment.getId(), currentDevice, actual.getRequestId());
					dcuParamList.add(kafkaMessage);
				}
			}
		}
		this.executeAction(dcuParamList);
		return result.getResult();
	}

	private void executeAction(List<DcuParameter> dcuParamList) {
		final CommandOutput ouputResult = new CommandOutput();
		ouputResult.setResult(gson.toJson(dcuParamList).replaceAll("\\\\", ""));

		// Se envía a kafka - MDM
		log.debug("LoadProfileProcess >> Sending LoadProfile to kafka");

		final AbstractAction actionService = (AbstractAction) this.context.getBean("DcuParametersActionWS");
		actionService.executeActions(ouputResult);

		log.debug("LoadProfileProcess >> Sent LoadProfile to \"MDM\"");
	}

	private Device mapDevice(String deviceName, Request request) {
		List<Device> devices = request.getDevices();
		return devices.stream().filter(device -> device.getSerialNumber().equals(deviceName)).findFirst().orElse(null);
	}

	private DcuParameter buildMDMKafkaMessage(String pid, Long orderId, Long taskId, Device device, Long requestId) {
		HeartBeat hb = new HeartBeat();
		hb.setModelName(device.getModel());
		hb.setDeviceName(device.getName());
		hb.setVersion(device.getVersion());
		hb.setMacAddress(device.getMacAddress());
		hb.setSerialNumber(device.getSerialNumber());
		hb.setWwanIP(device.getIp());

		DcuParameter dcuParameter = new DcuParameter();
		DcuParameterEntry entry = new DcuParameterEntry(AppConstants.DcuParameterKey.HEART_BEAT, gson.toJson(hb),
				hb.getSerialNumber());

		List<DcuParameterEntry> meterCollector = new ArrayList<DcuParameterEntry>();
		meterCollector.add(entry);

		dcuParameter.setMeterCollector(meterCollector);
		dcuParameter.setMeterId(device.getSerialNumber());
		dcuParameter.setOrderId(orderId);
		dcuParameter.setRequestId(requestId);
		dcuParameter.setPid(pid);
		dcuParameter.setTaskId(taskId);

		return dcuParameter;
	}
}
