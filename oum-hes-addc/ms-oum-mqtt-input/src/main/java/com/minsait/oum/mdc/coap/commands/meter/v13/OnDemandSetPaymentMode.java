package com.minsait.oum.mdc.coap.commands.meter.v13;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandSetPaymentMode_V13")
public class OnDemandSetPaymentMode extends AbstractOnDemand {

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_PAYMENT_MODE;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		String payMode = orders.getParameters().get("PaymentMode");
		if(payMode.equals("0")){
			payMode="Postpaid";
		}else if(payMode.equals("1")){
			payMode="Prepaid";
		}
		commandVO.setPaymentMode(payMode);     // 1= Prepaid, 0= Postpaid
	}
}
