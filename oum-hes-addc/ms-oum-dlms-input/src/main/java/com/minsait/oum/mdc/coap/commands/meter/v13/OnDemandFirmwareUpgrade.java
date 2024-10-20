package com.minsait.oum.mdc.coap.commands.meter.v13;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.FirmWare;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandFirmwareUpgrade_V13")
public class OnDemandFirmwareUpgrade extends AbstractOnDemand {
	
	@Autowired
	private Gson gson;
	
//    @Override
//    protected void processOnDemandCommand(String id, String command, OnDemandOrder orders, String ipPort) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                executeCommand(buildCommand(id, orders), AppConstants.OnDemand.COAP_PATH.FIRM_WARE_UPGRADE, ipPort);
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
//        commandVO.setFirmWares(new ArrayList<>());
//
//        return commandVO;
//    }
    
	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.FIRM_WARE_UPGRADE;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder orders) {
		
		FirmWare firmWare = new FirmWare();
		firmWare.setUrl(orders.getParameters().get("URL").toString());
        firmWare.setImageActivationTime(orders.getParameters().get("IMAGE_ACTIVATION_TIME").toString());
		
		commandVO.setFirmWare(Arrays.asList(firmWare)); 
	}
}
