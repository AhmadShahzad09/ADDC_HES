package com.minsait.oum.mdc.domain.billing;

import com.minsait.common.enu.ProfileCommon;
import com.minsait.oum.mdc.platform.billingprofile.BillingProfileMeter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BillingProfile extends ProfileCommon {

	public BillingProfile() {
		this.meterCollector = new ArrayList<>();
	}

	private Double profileStatus;
	private List<BillingProfileMeter> meterCollector;

}
