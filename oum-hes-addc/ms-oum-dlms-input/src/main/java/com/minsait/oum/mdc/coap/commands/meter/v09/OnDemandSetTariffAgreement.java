package com.minsait.oum.mdc.coap.commands.meter.v09;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandSetTariffAgreement_V09")
public class OnDemandSetTariffAgreement extends AbstractOnDemand {

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_TARIFF_AGREEMENT;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setCalendarName(orders.getParameters().get("CalendarName"));
		commandVO.setActivateCalendarTime(orders.getParameters().get("ActivateCalendarTime"));
	}
}
