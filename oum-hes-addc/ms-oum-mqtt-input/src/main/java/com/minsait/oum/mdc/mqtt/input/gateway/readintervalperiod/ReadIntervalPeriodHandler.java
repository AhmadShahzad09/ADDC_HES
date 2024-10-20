package com.minsait.oum.mdc.mqtt.input.gateway.readintervalperiod;

import com.google.gson.GsonBuilder;
import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class ReadIntervalPeriodHandler extends AbstractMessageHandlerProcessor {
    @Autowired
    private ReadIntervalPeriodAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {
    	this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
				(request, call, device) -> {

					if(StringUtils.isNotEmpty(device.getLoadProfile1Interval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "loadprofile1_Interval", device.getLoadProfile1Interval()));

					if(StringUtils.isNotEmpty(device.getLoadProfile2Interval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "loadprofile2_Interval", device.getLoadProfile2Interval()));

					if(StringUtils.isNotEmpty(device.getPowerQualityProfileInterval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "PowerQualityProfile_Interval", device.getPowerQualityProfileInterval()));

					if(StringUtils.isNotEmpty(device.getInstrumentationProfileInterval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "InstrumentationProfile_Interval", device.getInstrumentationProfileInterval()));

					if(StringUtils.isNotEmpty(device.getMaxDemandProfileInterval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "MaxdemandProfile_Interval", device.getMaxDemandProfileInterval()));

					if(StringUtils.isNotEmpty(device.getWaterProfileInterval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "Waterprofile_Interval", device.getWaterProfileInterval()));

					if(StringUtils.isNotEmpty(device.getEnergyProfileGroup1Interval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "EnergyProfileGroup1_Interval", device.getEnergyProfileGroup1Interval()));

					if(StringUtils.isNotEmpty(device.getEnergyProfileGroup2Interval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "EnergyProfileGroup2_Interval", device.getEnergyProfileGroup2Interval()));

					if(StringUtils.isNotEmpty(device.getEnergyProfileGroup3Interval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "EnergyProfileGroup3_Interval", device.getEnergyProfileGroup3Interval()));

					if(StringUtils.isNotEmpty(device.getInstantaneousGroup1Interval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "InstantaneousGroup1_Interval", device.getInstantaneousGroup1Interval()));

					if(StringUtils.isNotEmpty(device.getInstantaneousGroup2Interval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "InstantaneousGroup2_Interval", device.getInstantaneousGroup2Interval()));

					if(StringUtils.isNotEmpty(device.getInstantaneousGroup3Interval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "InstantaneousGroup3_Interval", device.getInstantaneousGroup3Interval()));

					if(StringUtils.isNotEmpty(device.getInstantaneousGroup4Interval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "InstantaneousGroup4_Interval", device.getInstantaneousGroup4Interval()));

					if(StringUtils.isNotEmpty(device.getHeartBeatInterval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "heartbeat_Interval", device.getHeartBeatInterval()));

					if(StringUtils.isNotEmpty(device.getGwEventsInterval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "gwEvents_Interval", device.getGwEventsInterval()));

					if(StringUtils.isNotEmpty(device.getEventsInterval()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "Events_Interval", device.getEventsInterval()));
					
					if(StringUtils.isNotEmpty(device.getEMonthlyBillingProfile()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "eMonthlyBillingProfile", device.getEMonthlyBillingProfile()));
					
					if(StringUtils.isNotEmpty(device.getEDailyBillingProfile()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "eDailyBillingProfile", device.getEDailyBillingProfile()));
					
					if(StringUtils.isNotEmpty(device.getWMonthlyBillingProfile()))
						this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "wMonthlyBillingProfile", device.getWMonthlyBillingProfile()));
				}
		);
    }
}
