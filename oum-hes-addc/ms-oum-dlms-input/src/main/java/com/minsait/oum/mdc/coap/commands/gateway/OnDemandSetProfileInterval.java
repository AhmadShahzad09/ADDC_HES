package com.minsait.oum.mdc.coap.commands.gateway;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import com.minsait.oum.mdc.coap.commands.bean.SchedulerConfig;
import com.minsait.oum.mdc.coap.commands.meter.v13.AbstractOnDemandIntervalPeriod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandGatewaySetIntervalPeriod_V13")
public class OnDemandSetProfileInterval extends AbstractOnDemandIntervalPeriod {

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_PROFILE_INTERVAL;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setDevices(null);
		commandVO.setSchedulerConfig(this.toSchedulerConfig(orders));
	}

	private List<SchedulerConfig> toSchedulerConfig(OnDemandOrder order) {
		List<SchedulerConfig> result = new ArrayList<>();
		result.add(this.resolveSchedulerConfig(order));
		return result;
	}
}
