package com.minsait.oum.mdc.coap.commands.meter.v09;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandSetLoadProfileCapturePeriod_V09")
public class OnDemandSetLoadProfileCapturePeriod extends AbstractOnDemand {
//    @Override
//    protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                executeCommand(buildCommand(id, orders), AppConstants.OnDemand.COAP_PATH.SET_LOAD_PROFILE_CAPTURE_PERIOD, ipPort);
//            }
//        }).start();
//    }
//    private OnDemandCommandVO buildCommand(String id, OnDemandOrder orders) {
//        OnDemandCommandVO commandVO = new OnDemandCommandVO();
//        commandVO.setIdRequest(id);
//        commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//        commandVO.setDevices(orders.getDevices());
//        commandVO.setGWip("");
//        commandVO.setGWsn("");
//        commandVO.setSchedulerConfig(new ArrayList<>());
//        return commandVO;
//    }
    
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_LOAD_PROFILE_CAPTURE_PERIOD;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
      commandVO.setSchedulerConfig(new ArrayList<>());
	}
}
