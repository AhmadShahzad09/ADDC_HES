package com.minsait.oum.mdc.coap.commands.gateway.meterdiscovery;

import com.minsait.mdc.util.AppConstants;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.MeterConfig;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service("SendMetersInformation")
public class SendMetersInformation extends AbstractOnDemand {

//	@Override
//	protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				executeCommand(buildCommand(id), AppConstants.OnDemand.COAP_PATH.SEND_METERS_INFORMATION, ipPort);
//			}
//		}).start();
//	}
//
//	private OnDemandCommandVO buildCommand(String id) {
//		OnDemandCommandVO commandVO = new OnDemandCommandVO();
//		commandVO.setIdRequest(id);
//		commandVO.setGWip("");	// TODO
//		commandVO.setGWsn("");
//		commandVO.setGUti(""); //FIXME
//		// set meterList
//		commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//		return commandVO;
//	}
	
	
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SEND_METERS_INFORMATION;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setMeterList(null); // TODO -- from where we will get this info to set here
	}
}
