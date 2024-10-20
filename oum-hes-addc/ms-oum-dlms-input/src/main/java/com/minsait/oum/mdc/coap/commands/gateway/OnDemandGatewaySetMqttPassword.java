package com.minsait.oum.mdc.coap.commands.gateway;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("OnDemandGatewaySetMqttPassword_V13")
public class OnDemandGatewaySetMqttPassword extends AbstractOnDemand {
    @Override
    protected String getCoapPath() {
        return AppConstants.OnDemand.COAP_PATH.GATEWAY_SET_MQTT_PASSWORD;
    }

    @Override
    protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
        commandVO.setDevices(null);
        commandVO.setSecret(orders.getParameters().get("Password"));
    }
}
