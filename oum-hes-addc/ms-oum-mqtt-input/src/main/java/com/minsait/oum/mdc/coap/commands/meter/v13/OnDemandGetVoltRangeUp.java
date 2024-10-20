package com.minsait.oum.mdc.coap.commands.meter.v13;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandGetVoltRangeUp_V13")
public class OnDemandGetVoltRangeUp extends AbstractOnDemand {

//	@Override
//	protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				executeCommand(buildCommand(id, orders), AppConstants.OnDemand.COAP_PATH.GET_VOLT_RANGE_UP, ipPort);
//			}
//		}).start();
//	}
//
//	private OnDemandCommandVO buildCommand(String id, OnDemandOrder orders) {
//		OnDemandCommandVO commandVO = new OnDemandCommandVO();
//		commandVO.setIdRequest(id);
//		commandVO.setGWip("");	// TODO
//		commandVO.setGWsn("");
//		commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//		commandVO.setDevices(orders.getDevices());
//
//		return commandVO;
//	}
    
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.GET_VOLT_RANGE_UP;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
	}
}
