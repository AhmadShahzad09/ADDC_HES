package com.minsait.oum.mdc.coap.commands.gateway;

import com.minsait.oum.mdc.coap.commands.AbstractOnDemand;
import com.minsait.oum.mdc.coap.commands.bean.OnDemandOrder;

import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractOnDemandProfileIntervalPeriod extends AbstractOnDemand {
    protected enum ProfileIntervalPeriod {
        LoadProfile1("0", "LoadProfile1"),
        LoadProfile2("1", "LoadProfile2"),
        PowerQualityProfile("2", "PowerQualityProfile"),
        InstrumentationProfile("3", "InstrumentationProfile"),
        EnergyProfile("4", "EnergyProfile"),
        MaxdemandProfile("5", "MaxdemandProfile"),
        InstantaneousValues("6", "InstantaneousValues"),
        waterprofile("7", "waterprofile"),
        gwEvents("8", "gwEvents"),
        Events("9", "Events"),
        Heartbeat("10", "Heartbeat"),
        eMonthlyBillingProfile("11", "eMonthlyBillingProfile"),
        eDailyBillingProfile("12", "eDailyBillingProfile"),
        wMonthlyBillingProfile("13", "wMonthlyBillingProfile");

        private String profileKey;
        private String profileName;

        private ProfileIntervalPeriod(String profileKey, String profileName) {
            this.profileKey = profileKey;
            this.profileName = profileName;
        }

        public static Optional<ProfileIntervalPeriod> from(final String profileKey) {
            return Arrays.stream(ProfileIntervalPeriod.values())
                    .filter(profile -> profile.getProfileKey().equals(profileKey)).findFirst();
        }

        public static Optional<ProfileIntervalPeriod> fromName(final String profileName) {
            return Arrays.stream(ProfileIntervalPeriod.values())
                    .filter(profile -> profile.getProfileName().equals(profileName)).findFirst();
        }

        public String getProfileKey() {
            return profileKey;
        }

        public String getProfileName() {
            return profileName;
        }

    }

    protected ProfileIntervalPeriod resolveIntervalProfile(OnDemandOrder order) {
        String profileKey = order.getParameters().get("Profile");

        if (profileKey == null || profileKey.isEmpty())
            throw new UnsupportedOperationException(
                    "cannot find parameter 'Profile' among readProfileIntervalPeriod parameters");

        Optional<ProfileIntervalPeriod> profileOpt = ProfileIntervalPeriod.from(profileKey);

        if (!profileOpt.isPresent()) {
            Optional<ProfileIntervalPeriod> profileNameOpt = ProfileIntervalPeriod.fromName(profileKey);
            if (!profileNameOpt.isPresent()) {
                throw new IllegalArgumentException(
                        String.format("cannot find readProfileIntervalPeriod for key %s", profileKey));
            }
        }
        return profileOpt.get();
    }
}
