package com.minsait.oum.mdc.coap.commands.meter.v09;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandChangeMeterPassword_V09")
public class OnDemandChangeMeterPassword extends AbstractOnDemand {

//    @Override
//    protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                executeCommand(buildCommand(id, orders), AppConstants.OnDemand.COAP_PATH.CHANGE_METER_PASSWORD, ipPort);
//            }
//        }).start();
//    }
//    private OnDemandCommandVO buildCommand(String id, OnDemandOrder orders) {
//        OnDemandCommandVO commandVO = new OnDemandCommandVO();
//        commandVO.setIdRequest(id);
//        commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//        commandVO.setDevices(orders.getDevices());
//        commandVO.setGWsn("");
//        commandVO.setGWip("");
//        commandVO.setSecret(null);
//        commandVO.setName("");
//
//        return commandVO;
//    }
    
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.CHANGE_METER_PASSWORD;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
//      commandVO.setSecret(null);
	}
}
