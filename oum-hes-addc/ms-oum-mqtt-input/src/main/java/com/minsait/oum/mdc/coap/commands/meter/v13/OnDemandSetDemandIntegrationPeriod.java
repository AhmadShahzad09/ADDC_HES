package com.minsait.oum.mdc.coap.commands.meter.v13;

import com.minsait.mdc.util.AppConstants;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("OnDemandSetDemandIntegrationPeriod_V13")
public class OnDemandSetDemandIntegrationPeriod extends AbstractOnDemand {

    @Override
    protected String getCoapPath() {
        return AppConstants.OnDemand.COAP_PATH.SET_DEMAND_INTEGRATION_PERIOD;
    }

    @Override
    protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
        String value = orders.getParameters().get("DemandPeriod(seconds)");
        commandVO.setDemandIntPeriod(AppUtils.parseLongWithZeroAsDefault(value));
    }
}
