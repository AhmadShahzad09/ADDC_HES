package com.minsait.oum.mdc.coap.commands.meter.v13;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import com.minsait.oum.mdc.coap.commands.bean.SchedulerConfig;

public abstract class AbstractOnDemandIntervalPeriod extends AbstractOnDemand {

	@Autowired
	private Gson gson;

	protected enum IntervalPeriodProfile {
		LOADPROFILE1_PERIOD("0", "LoadProfile1", (config, value) -> config.setLoadProfile1(value)),
		LOADPROFILE2_PERIOD("1", "LoadProfile2", (config, value) -> config.setLoadProfile2(value)),
		POWERQUALITYPROFILE_PERIOD("2", "PowerQualityProfile", (config, value) -> config.setPowerQualityProfile(value)),
		INSTRUMENTATIONPROFILE_PERIOD("3", "InstrumentationProfile", (config, value) -> config.setInstrumentationProfile(value)),
		ENERGY_PROFILE("4", "EnergyProfile", (config, value) -> config.setEnergyProfile(value)),
		MAX_DEMAND_PROFILE("5", "MaxDemandProfile", (config, value) -> config.setMaxDemandProfile(value)),
		INSTANTANEOUS_VALUES("6", "InstantaneousValues", (config, value) -> config.setInstantaneousValues(value)),
		WATER_PROFILE("7", "waterProfile", (config, value) -> config.setWaterProfile(value)),
		GW_EVENTS("8", "gwEvents", (config, value) -> config.setGwEvents(value)),
		EVENTS("9", "Events", (config, value) -> config.setEvents(value)),
		HEART_BEAT("10", "Heartbeat", (config, value) -> config.setHeartbeat(value)),
		E_MONTHLY_BILLING_PROFILE("11", "eMonthlyBillingProfile", (config, value) -> config.setEMonthlyBillingProfile(value)),
        E_DAILY_BILLING_PROFILE("12", "eDailyBillingProfile", (config, value) -> config.setEDailyBillingProfile(value)),
        W_MONTHLY_BILLING_PROFILE("13", "wMonthlyBillingProfile", (config, value) -> config.setWMonthlyBillingProfile(value));

		private String profileKey;
		private String profileName;
		private BiConsumer<SchedulerConfig, Long> profileParser;

		private IntervalPeriodProfile(String profileKey, String profileName,
				BiConsumer<SchedulerConfig, Long> profileParser) {
			this.profileKey = profileKey;
			this.profileName = profileName;
			this.profileParser = profileParser;
		}

		public static Optional<IntervalPeriodProfile> from(final String profileKey) {
			return Arrays.stream(IntervalPeriodProfile.values())
					.filter(profile -> profile.getProfileKey().equals(profileKey)).findFirst();
		}

		public static Optional<IntervalPeriodProfile> fromName(final String profileName) {
			return Arrays.stream(IntervalPeriodProfile.values())
					.filter(profile -> profile.getProfileName().equals(profileName)).findFirst();
		}

		public SchedulerConfig applyProfile(Long value) {
			SchedulerConfig result = new SchedulerConfig();
			profileParser.accept(result, value);

			return result;
		}

		public String getProfileKey() {
			return profileKey;
		}

		public String getProfileName() {
			return profileName;
		}

	}

	protected IntervalPeriodProfile resolveCapturePeriodProfile(OnDemandOrder order) {
		String profileKey = order.getParameters().get("Profile");

		if (profileKey == null || profileKey.isEmpty())
			throw new UnsupportedOperationException(
					"cannot find parameter 'Profile' among IntervalPeriodProfile parameters");

		Optional<IntervalPeriodProfile> profileOpt = IntervalPeriodProfile.from(profileKey);

		if (!profileOpt.isPresent()) {
			Optional<IntervalPeriodProfile> profileNameOpt = IntervalPeriodProfile.fromName(profileKey);
			if (!profileNameOpt.isPresent()) {
				throw new IllegalArgumentException(
						String.format("cannot find IntervalPeriodProfile for key %s", profileKey));
			}
		}

		return profileOpt.get();
	}

	protected SchedulerConfig resolveSchedulerConfig(OnDemandOrder order) {
		String profileValue = order.getParameters().get("Period(S)");

		if (profileValue == null || profileValue.isEmpty())
			throw new UnsupportedOperationException(
					"cannot find parameter 'Value' among IntervalPeriodProfile parameters");

		if (!StringUtils.isNumeric(profileValue))
			throw new UnsupportedOperationException("IntervalPeriodProfile value must be a number");

		IntervalPeriodProfile periodProfile = this.resolveCapturePeriodProfile(order);
		SchedulerConfig schedulerConfig = periodProfile.applyProfile(Long.parseLong(profileValue));
		List<String> groups = new ArrayList<>();
		
		if (periodProfile.getProfileName().equals("EnergyProfile")) {
			groups.addAll(Arrays.asList("group1", "group2", "group3"));
			schedulerConfig.setGroups(groups);
		} else if (periodProfile.getProfileName().equals("InstantaneousValues")) {
			groups.addAll(Arrays.asList("group1", "group2", "group3", "group4"));
			schedulerConfig.setGroups(groups);
		}

		return schedulerConfig;
	}
}
