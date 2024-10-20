package com.minsait.oum.mdc.coap.commands.meter.v13;

import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;
import com.minsait.oum.mdc.coap.commands.bean.SchedulerConfig;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class AbstractOnDemandCapturePeriod extends AbstractOnDemand {

	protected enum CapturePeriodProfile {
		LOADPROFILE1_PERIOD("0", "LoadProfile1", 
				(config, value) -> config.setLoadProfile1(value)), //
		LOADPROFILE2_PERIOD("1", "LoadProfile2", 
				(config, value) -> config.setLoadProfile2(value)), //
		POWERQUALITYPROFILE_PERIOD("2", "PowerQualityProfile",
				(config, value) -> config.setPowerQualityProfile(value)), //
		INSTRUMENTATIONPROFILE_PERIOD("3", "InstrumentationProfile",
				(config, value) -> config.setInstrumentationProfile(value));//

		private String profileKey;
		private String profileName;
		private BiConsumer<SchedulerConfig, Long> profileParser;

		private CapturePeriodProfile(String profileKey, String profileName,
				BiConsumer<SchedulerConfig, Long> profileParser) {
			this.profileKey = profileKey;
			this.profileName = profileName;
			this.profileParser = profileParser;
		}

		public static Optional<CapturePeriodProfile> from(final String profileKey) {
			return Arrays.stream(CapturePeriodProfile.values())
					.filter(profile -> profile.getProfileKey().equals(profileKey)).findFirst();
		}

		public static Optional<CapturePeriodProfile> fromName(final String profileName) {
			return Arrays.stream(CapturePeriodProfile.values())
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

	protected CapturePeriodProfile resolveCapturePeriodProfile(OnDemandOrder order) {
		String profileKey = order.getParameters().get("Profile");

		if (profileKey == null || profileKey.isEmpty())
			throw new UnsupportedOperationException(
					"cannot find parameter 'Profile' among SetLoadProfileCapturePeriod parameters");

		Optional<CapturePeriodProfile> profileOpt = CapturePeriodProfile.from(profileKey);

		if (!profileOpt.isPresent()) {
			Optional<CapturePeriodProfile> profileNameOpt = CapturePeriodProfile.fromName(profileKey);
			if (!profileNameOpt.isPresent()) {
				throw new IllegalArgumentException(
						String.format("cannot find SetLoadProfileCapturePeriod for key %s", profileKey));
			}
		}

		return profileOpt.get();
	}

	protected SchedulerConfig resolveSchedulerConfig(OnDemandOrder order) {
		String profileValue = order.getParameters().get("Value");

		if (profileValue == null || profileValue.isEmpty())
			throw new UnsupportedOperationException(
					"cannot find parameter 'Value' among SetLoadProfileCapturePeriod parameters");

		if (!StringUtils.isNumeric(profileValue))
			throw new UnsupportedOperationException("SetLoadProfileCapturePeriod value must be a number");

		return this.resolveCapturePeriodProfile(order).applyProfile(Long.parseLong(profileValue));
	}
}
