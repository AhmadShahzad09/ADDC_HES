package com.minsait.oum.mdc.mqtt.input.setprofilecaptureperiod;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

public class SetLoadProfileCapturePeriodHandler extends AbstractMessageHandlerProcessor {

    @Autowired
    private SetLoadProfileCapturePeriodAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {

        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> {
                    String loadProfileCapturePeriodStatus = "";
                    if (device.getLoadProfile1CapturePeriod() != null && !device.getLoadProfile1CapturePeriod().isEmpty())
                        loadProfileCapturePeriodStatus += "LoadProfile1: " + device.getLoadProfile1CapturePeriod() + ";";
                    if (device.getLoadProfile2CapturePeriod() != null && !device.getLoadProfile2CapturePeriod().isEmpty())
                        loadProfileCapturePeriodStatus += "LoadProfile2: " + device.getLoadProfile2CapturePeriod() + ";";
                    if (device.getPowerQualityProfileCapturePeriod() != null && !device.getPowerQualityProfileCapturePeriod().isEmpty())
                        loadProfileCapturePeriodStatus += "PowerQualityProfile: " + device.getPowerQualityProfileCapturePeriod() + ";";
                    if (device.getInstrumentationProfileCapturePeriod() != null && !device.getInstrumentationProfileCapturePeriod().isEmpty())
                        loadProfileCapturePeriodStatus += "InstrumentationProfile: " + device.getInstrumentationProfileCapturePeriod() + ";";
                    this.getMdmRestClient().setTelemetryRemoteConfig(
                            TelemetryRemoteConfigurationVO.from(device, "LoadProfileIntervalStatus", loadProfileCapturePeriodStatus));
                });

    }

}
