package com.minsait.oum.mdc.coap.commands.meter.v13;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Service("OnDemandSynchronization_V13")
public class OnDemandSetMeterSynchronization extends AbstractOnDemand {

   
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_SYNCHRONIZATION;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		String dateStr = new Date().toString();
		commandVO.setDate(AppUtils.getDateFromStringUTC(dateStr));
	}
}
