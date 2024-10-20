package com.minsait.oum.mdc.coap.commands.meter.v13;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandCutOffReconnection_V13")
public class OnDemandCutOffReconnection extends AbstractOnDemand {

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.RECONNECTION;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setMode("Mode" + orders.getParameters().get("ControlMode"));
		commandVO.setType(
				orders.getParameters().get("Type").equals("0") ? AppConstants.OnDemand.COMMAND_TYPE.DISCONNECTION
						: AppConstants.OnDemand.COMMAND_TYPE.CONNECTION);
		
	}
}
