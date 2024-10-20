package com.minsait.oum.mdc.coap.commands.meter.v13;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandChangeMeterPassword_V13")
public class OnDemandChangeMeterPassword extends AbstractOnDemand {

    @Override
    protected String getCoapPath() {
        return AppConstants.OnDemand.COAP_PATH.CHANGE_METER_PASSWORD;
    }

    @Override
    protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
        commandVO.setSecret(orders.getParameters().get("Secret"));
        commandVO.setName(orders.getParameters().get("Name"));
    }
}
