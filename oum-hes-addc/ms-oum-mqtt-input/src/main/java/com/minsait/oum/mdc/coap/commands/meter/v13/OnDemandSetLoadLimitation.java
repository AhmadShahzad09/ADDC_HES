package com.minsait.oum.mdc.coap.commands.meter.v13;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandSetLoadLimitation_V13")
public class OnDemandSetLoadLimitation extends AbstractOnDemand {

	/*@Override
	protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
		new Thread(new Runnable() {
			@Override
			public void run() {
			executeCommand(buildCommand(id, orders), AppConstants.OnDemand.COAP_PATH.SET_LOAD_LIMITATION, ipPort);
			}
		}).start();
	}

	private OnDemandCommandVO buildCommand(String id, OnDemandOrder orders) {
		OnDemandCommandVO commandVO = new OnDemandCommandVO();
		commandVO.setIdRequest(id);
		commandVO.setType(AppConstants.OnDemand.COMMAND_TYPE.LOAD_LIMIT);
		commandVO.setLimitThreshold("");
		commandVO.setThreshDuration(0l);
		commandVO.setGWip("");
		commandVO.setGWsn("");
		commandVO.setDevices(orders.getDevices());
		//commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
		return commandVO;
	}*/

    
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_LOAD_LIMITATION;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setType(AppConstants.OnDemand.COMMAND_TYPE.LOAD_LIMIT);
		commandVO.setLimitThreshold(orders.getParameters().get("LoadLimitThreshold(W)"));
		commandVO.setThreshDuration(orders.getParameters().get("LoadLimitThresholdDuration(S)"));
	}}
