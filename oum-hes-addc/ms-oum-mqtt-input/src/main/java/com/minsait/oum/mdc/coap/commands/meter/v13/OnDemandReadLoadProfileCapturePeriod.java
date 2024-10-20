package com.minsait.oum.mdc.coap.commands.meter.v13;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.minsait.mdc.util.AppConstants;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandCommandVO;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("OnDemandReadLoadProfileCapturePeriod_V13")
public class OnDemandReadLoadProfileCapturePeriod extends AbstractOnDemandCapturePeriod {

	@Override
	protected String getCoapPath() {
		return AppConstants.OnDemand.COAP_PATH.READ_LOAD_PROFILE_CAPTURE_PERIOD;
	}

	@Override
	protected void customizeCommand(OnDemandCommandVO commandVO, OnDemandOrder order) {
		// if the profile parameters are not selected, send all
		if (order.getParameters().get("Profile").equals("LoadProfile1")) {
			ArrayList<String> profilesName = new ArrayList<>();
			for (CapturePeriodProfile capturePeriodProfile : CapturePeriodProfile.values()) {
				profilesName.add(capturePeriodProfile.getProfileName());
			}
			commandVO.setProfile(profilesName);
		} else {
			CapturePeriodProfile periodProfile = this.resolveCapturePeriodProfile(order);
			commandVO.setProfile(Arrays.asList(periodProfile.getProfileName()));
		}
	}
}
