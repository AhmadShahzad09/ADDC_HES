package com.minsait.oum.mdc.mqtt.input.getprofileinterval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetProfileIntervalHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private GetProfileIntervalAdapter adapter;

	@Override
	public void handleMessage(Message<?> message) {

		this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
				(request, call, device) -> {

					String[] capturePeriodConfig = this.getCapturePeriodConfig(device);

					this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device,
							capturePeriodConfig[0], capturePeriodConfig[1]));
				});

	}

	private String[] getCapturePeriodConfig(Device device) {

		String[] result = new String[2];

		if (device.getLoadProfile1Interval() != null && !device.getLoadProfile1Interval().isEmpty()) {
			result[0] = "LoadProfile1CapturePeriod";
			result[1] = device.getLoadProfile1Interval();

		} else if (device.getLoadProfile2Interval() != null && !device.getLoadProfile2Interval().isEmpty()) {
			result[0] = "LoadProfile2CapturePeriod";
			result[1] = device.getLoadProfile2Interval();
		} else if (device.getPowerQualityProfileInterval() != null
				&& !device.getPowerQualityProfileInterval().isEmpty()) {
			result[0] = "PowerQualityProfileCapturePeriod";
			result[1] = device.getPowerQualityProfileInterval();

		} else if (device.getInstrumentationProfile() != null && !device.getInstrumentationProfile().isEmpty()) {
			result[0] = "InstrumentationProfileCapturePeriod";
			result[1] = device.getInstrumentationProfile();

		} else {
			throw new UnsupportedOperationException(String
					.format("no profile capture period returned from gateway for device %s", device.getSerialNumber()));
		}

		return result;
	}
}
