package com.minsait.oum.mdc.coap.commands.meter.v13;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandSetMeteringMode_V13")
public class OnDemandSetMeteringMode extends AbstractOnDemand {
	public static final String UNIDIRECTIONAL = "Unidirectional";
	public static final String BIDIRECTIONAL = "Bidirectional";
	public static final String DEFAULT_METERING_MODE = "0";


//    @Override
//    protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                executeCommand(buildCommand(id, orders), AppConstants.OnDemand.COAP_PATH.SET_METERING_MODE, ipPort);
//            }
//        }).start();
//    }
//
//    private OnDemandCommandVO buildCommand(String id, OnDemandOrder orders) {
//        OnDemandCommandVO commandVO = new OnDemandCommandVO();
//        commandVO.setIdRequest(id);
//        commandVO.setGWip("");  // TODO
//        commandVO.setGWsn("");
//        commandVO.setMeteringMode(""); // " Bidirectional", // 1= Bidirectional, 0= Unidirectional
//        commandVO.setDevices(orders.getDevices());
//        commandVO.setTime(AppUtils.getTimeInSeconds(new Date().getTime()));
//        return commandVO;
//    }
    
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.SET_METERING_MODE;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {

		String meteringModeValue= orders.getParameters().get("MeteringMode");
		String meteringMode="";

		if(!meteringModeValue.isEmpty()){
			meteringMode=findMeteringMode(meteringModeValue);
		}
		commandVO.setMeteringMode(meteringMode);
	}


	private String findMeteringMode(String mode){
		return mode.equals(DEFAULT_METERING_MODE) ?  BIDIRECTIONAL : UNIDIRECTIONAL;
	}
}
