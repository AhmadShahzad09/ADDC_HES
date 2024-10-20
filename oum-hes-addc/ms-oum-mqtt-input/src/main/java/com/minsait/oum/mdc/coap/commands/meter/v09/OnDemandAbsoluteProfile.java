package com.minsait.oum.mdc.coap.commands.meter.v09;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

@Service("OnDemandAbsoluteProfile_V09")
public class OnDemandAbsoluteProfile extends AbstractOnDemand {

//	@Override
//	protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				executeCommand(buildCommand(id,orders,ipPort), AppConstants.OnDemand.COAP_PATH.ENERGY_PROFILE, ipPort);
//			}
//		}).start();
//	}
//
//	private OnDemandCommandVO buildCommand(String id, OnDemandOrder orders, String ipPort) {
//		OnDemandCommandVO commandVO = new OnDemandCommandVO();
//		commandVO.setDevices(orders.getDevices());
////		commandVO.setGroups(orders.getGroups());
//		commandVO.setGroups(Arrays.asList(new String[]{"Group1", "Group2", "Group3"}));
//		commandVO.setIdRequest(id);
//		commandVO.setGWsn(orders.getGatewaySerialNumber());
//		commandVO.setGWip(StringUtils.EMPTY);
//		commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//		return commandVO;
//	}

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.ENERGY_PROFILE;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setGroups(Arrays.asList(new String[]{"Group1", "Group2", "Group3"}));
	}

}
