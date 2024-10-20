package com.minsait.oum.mdc.mqtt.input.reconnection;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Measure;
import com.minsait.oum.mdc.domain.reconnection.ReconnectionReading;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.AbstractPlatformMessageHandlerProcessor;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.absoluteprofile.AbsoluteProfileMeter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ReconnectionHandler extends AbstractPlatformMessageHandlerProcessor {

	@Autowired
	private ReconnectionAdapter adapter;

	@Override
	protected AbstractMessageAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected String getActionServiceName() {
		return "AbsoluteLoadProfileActionWS";
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		log.debug("==> Message: " + message.getHeaders() + "->" + message.getPayload());
		AtomicInteger failedDevices = new AtomicInteger(0);
		try {
			this.processTelemetryCallToPlatform(this.getAdapter().convert(message.getPayload().toString()),
					(request, call, device, size, index) -> {
						// check for failed device on profile, if true skip device
						if (checkDeviceFailed(call, request, device)) {
							// we are in the last device and all of them have failed? close call and return
							if (failedDevices.incrementAndGet() == size)
								this.closeCallAsFailed(call, request);
							// device failed, skip device processing
							return;
						}
						// if we reach here, at least one device has not failed
						if (index == size) {
							this.getMdmRestClient().setTelemetryRemoteConfig(
									TelemetryRemoteConfigurationVO.from(device, "ReconnectionStatus", device.getReconnection().toString()));
							super.executeAction(super.buildPlatformMessage(call, device));
						}
					});
		} catch (Exception e) {
			// solo deberia llegar aqui si el mensaje recibido no tiene formato json
			e.printStackTrace();
			log.error(InvalidFormatException.class.getName() + ": " + message.getPayload().toString());
		}
	}

	@Override
	protected List<PlatformMeter> buildMeterCollector(final Device device) {
		List<PlatformMeter> meterCollector = new ArrayList<>();
		meterCollector.add(buildPlatformMeter(device.getReconnection().getReading()));

		return meterCollector;
	}

	protected PlatformMeter buildPlatformMeter(final ReconnectionReading reading) {
		return AbsoluteProfileMeter.builder()
				.value(Double.parseDouble(reading.getValue()))
				.unitM(reading.getUnitM())
				.magnitudeCode(reading.getChannel())
				.obisCode(reading.getCode())
				.build();
	}

	@Override
	protected PlatformMeter buildPlatformMeter(final String quality, final Measure.Block block) {
		return AbsoluteProfileMeter.builder()
				.quality(quality)
				.day(block.getTimestamp())
				.value(block.getValue())
				.unitM(block.getMagnitude())
				.magnitudeCode(block.getChannel())
				.obisCode(block.getCode())
				.build();
	}
}
