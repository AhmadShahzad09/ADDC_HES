package com.minsait.oum.mdc.coap.commands.gateway;

import com.minsait.mdc.util.AppConstants;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service("OnDemandGatewayFirmwareUpdate_V13")
public class OnDemandFirmwareUpdate extends AbstractOnDemand {

//	@Override
//	protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				executeCommand(buildCommand(id), AppConstants.OnDemand.COAP_PATH.GATEWAY_FIRMWARE_UPDATE, ipPort);
//			}
//		}).start();
//	}
//
//	private OnDemandCommandVO buildCommand(String id) {
//		OnDemandCommandVO commandVO = new OnDemandCommandVO();
//		commandVO.setIdRequest(id);
//		commandVO.setGUti(""); //FIXME
//		commandVO.setSerialnumber(""); //TODO  need to discuss how I will get
//		commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//		return commandVO;
//	}
	

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.GATEWAY_FIRMWARE_UPDATE;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setDevices(null);
		commandVO.setFilePath(orders.getParameters().get("URL"));
	}
}
