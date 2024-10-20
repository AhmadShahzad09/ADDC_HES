package com.minsait.oum.mdc.coap.commands.meter.v09;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandSetMeterSynchronization_V09")
public class OnDemandSetMeterSynchronization extends AbstractOnDemand {

//	@Override
//	protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				executeCommand(buildCommand(id, orders), AppConstants.OnDemand.COAP_PATH.SET_SYNCHRONIZATION, ipPort);
//			}
//		}).start();
//	}
//
//	private OnDemandCommandVO buildCommand(String id, OnDemandOrder orders) {
//		OnDemandCommandVO commandVO = new OnDemandCommandVO();
//		commandVO.setIdRequest(id);
//		commandVO.setDevices(orders.getDevices());
//		commandVO.setDate(AppUtils.getTimeInSeconds(new Date().getDate()));
//		commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//		return commandVO;
//	}
    
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_SYNCHRONIZATION;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
	}
}
