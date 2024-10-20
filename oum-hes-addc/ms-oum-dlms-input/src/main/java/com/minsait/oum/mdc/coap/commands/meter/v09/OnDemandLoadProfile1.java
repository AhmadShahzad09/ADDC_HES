package com.minsait.oum.mdc.coap.commands.meter.v09;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.mdc.util.AppUtils;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandParameterVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandLoadProfile1_V09")
public class OnDemandLoadProfile1 extends AbstractOnDemand {

//    @Override
//    protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                executeCommand(buildCommand(id, orders), AppConstants.OnDemand.COAP_PATH.LOAD_PROFILE_ONE, ipPort);
//            }
//        }).start();
//    }
//
//    private OnDemandCommandVO buildCommand(String id, OnDemandOrder orders) {
//        OnDemandCommandVO commandVO = new OnDemandCommandVO();
//        commandVO.setDevices(orders.getDevices());
//        commandVO.setIdRequest(id);
//        commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//        commandVO.setGWsn("");
//        commandVO.setGWip("");
//        OnDemandParameterVO parameters = new OnDemandParameterVO();
//        parameters.setStartDate(AppUtils.getDateFromString(orders.getParameters().get("START_DATE")));
//        parameters.setEndDate(AppUtils.getDateFromString(orders.getParameters().get("END_DATE")));
//        commandVO.setParameters(parameters);
//        return commandVO;
//    }
    
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.LOAD_PROFILE_1;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		OnDemandParameterVO parameters = new OnDemandParameterVO();
		parameters.setStartDate(AppUtils.getDateFromString(orders.getParameters().get("START_DATE")));
		parameters.setEndDate(AppUtils.getDateFromString(orders.getParameters().get("END_DATE")));
		commandVO.setParameters(parameters);
	}
}
