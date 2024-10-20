package com.minsait.oum.mdc.coap.commands.device.management.and.monitoring;

import com.minsait.mdc.util.AppConstants;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service("OnDemandGatewayPing_V13")
public class OnDemandGatewayPing extends AbstractOnDemand {

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.GATEWAY_PING;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
        commandVO.setDevices(null);
	}
}
