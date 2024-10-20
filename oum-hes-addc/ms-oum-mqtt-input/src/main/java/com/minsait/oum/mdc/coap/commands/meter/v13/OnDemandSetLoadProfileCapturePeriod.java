package com.minsait.oum.mdc.coap.commands.meter.v13;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import com.minsait.oum.mdc.coap.commands.bean.SchedulerConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandSetLoadProfileCapturePeriod_V13")
public class OnDemandSetLoadProfileCapturePeriod extends AbstractOnDemandCapturePeriod {
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
		commandVO.setSchedulerConfig(this.toSchedulerConfig(orders));
	}

	private List<SchedulerConfig> toSchedulerConfig(OnDemandOrder order) {
		List<SchedulerConfig> result = new ArrayList<>();
		result.add(this.resolveSchedulerConfig(order));
		return result;
	}
}
