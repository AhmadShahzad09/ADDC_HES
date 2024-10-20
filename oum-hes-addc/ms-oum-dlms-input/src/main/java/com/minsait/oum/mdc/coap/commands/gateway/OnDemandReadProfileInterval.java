package com.minsait.oum.mdc.coap.commands.gateway;

import com.minsait.mdc.util.AppConstants;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import com.minsait.oum.mdc.coap.commands.meter.v13.AbstractOnDemandCapturePeriod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service("OnDemandGatewayReadIntervalPeriod_V13")
public class OnDemandReadProfileInterval extends AbstractOnDemandProfileIntervalPeriod {

//	@Override
//	protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				executeCommand(buildCommand(id), AppConstants.OnDemand.COAP_PATH.READ_PROFILE_INTERVAL, ipPort);
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
//		commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//		return commandVO;
//	}
	

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.READ_PROFILE_INTERVAL;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setDevices(null);
		ProfileIntervalPeriod periodProfile = this.resolveIntervalProfile(orders);
		if (periodProfile.getProfileName().equals("EnergyProfile")) {
			commandVO.setEnergyProfile(Arrays.asList("group1", "group2", "group3"));
		} else if (periodProfile.getProfileName().equals("InstantaneousValues")) {
			commandVO.setInstantaneous(Arrays.asList("group1", "group2", "group3", "group4"));
		} else {
			commandVO.setProfile(Arrays.asList(periodProfile.getProfileName()));
		}
	}
}
