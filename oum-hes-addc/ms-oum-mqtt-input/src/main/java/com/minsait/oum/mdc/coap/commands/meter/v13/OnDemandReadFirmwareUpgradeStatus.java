package com.minsait.oum.mdc.coap.commands.meter.v13;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandReadFirmwareUpgradeStatus_V13")
public class OnDemandReadFirmwareUpgradeStatus extends AbstractOnDemand {

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.GET_FIRMWARE_UPGRADE_STATUS;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
	}

}
