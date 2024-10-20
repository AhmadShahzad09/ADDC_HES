package com.minsait.oum.mdc.coap.commands.gateway;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandGatewaySynchronization_V13")
public class OnDemandSetTimeSynchronization extends AbstractOnDemand {

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.GATEWAY_SET_TIME_SYNCHRONIZATION;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		commandVO.setDevices(null);
		String dateStr = new Date().toString();
		commandVO.setDate(AppUtils.getDateFromStringUTC(dateStr));
	}
}
