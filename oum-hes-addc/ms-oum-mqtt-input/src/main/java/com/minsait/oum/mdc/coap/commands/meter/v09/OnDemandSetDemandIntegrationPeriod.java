package com.minsait.oum.mdc.coap.commands.meter.v09;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandSetDemandIntegrationPeriod_V09")
public class OnDemandSetDemandIntegrationPeriod extends AbstractOnDemand {

//    @Override
//    protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                executeCommand(buildCommand(id, orders), AppConstants.OnDemand.COAP_PATH.SET_DEMAND_INTEGRATION_PERIOD, ipPort);
//            }
//        }).start();
//    }
//
//    private OnDemandCommandVO buildCommand(String id, OnDemandOrder orders) {
//        OnDemandCommandVO commandVO = new OnDemandCommandVO();
//        commandVO.setIdRequest(id);
//        commandVO.setDevices(orders.getDevices());
//        commandVO.setGWip("");  // TODO to discuss
//        commandVO.setGWsn("");
//        commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//        commandVO.setDemandIntPeriod(0l);
//        return commandVO;
//    }
    
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_DEMAND_INTEGRATION_PERIOD;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
      commandVO.setDemandIntPeriod(0l);
	}
}
