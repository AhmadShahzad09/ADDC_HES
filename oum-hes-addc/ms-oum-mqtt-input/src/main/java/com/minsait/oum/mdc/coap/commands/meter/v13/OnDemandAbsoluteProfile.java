package com.minsait.oum.mdc.coap.commands.meter.v13;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service("OnDemandAbsoluteProfile_V13")
public class OnDemandAbsoluteProfile extends AbstractOnDemand {

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.ENERGY_PROFILE;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		if (!orders.getParameters().isEmpty() && !orders.getParameters().get("Group").isEmpty()) {
			commandVO.setGroups(Arrays.asList("group" + (Integer.valueOf(orders.getParameters().get("Group"))+1)));
		} else {
			log.error("Cannot execute command OnDemandAbsoluteProfile, error building command: Group is not selected");
			// capture command error to finish call on call monitor
			sendError(commandVO.getIdRequest(), null, "Group is not selected");
		}
	}

}
