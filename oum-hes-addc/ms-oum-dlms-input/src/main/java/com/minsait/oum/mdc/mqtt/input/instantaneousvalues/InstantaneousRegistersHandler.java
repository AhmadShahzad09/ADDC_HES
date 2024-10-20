package com.minsait.oum.mdc.mqtt.input.instantaneousvalues;

import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Measure;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.AbstractPlatformMessageHandlerProcessor;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.instantaneousvalues.InstantaneousValuesMeter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InstantaneousRegistersHandler extends AbstractPlatformMessageHandlerProcessor {

	@Autowired
	private InstantaneousRegistersAdapter adapter;

	@Override
	public AbstractMessageAdapter getAdapter() {
		return adapter;
	}

	@Override
	public String getActionServiceName() {
		return "InstValuesActionWS";
	}

	@Override
	protected List<PlatformMeter> buildMeterCollector(final Device device) {
		List<PlatformMeter> meterCollector = new ArrayList<>();
		for (Measure measure : device.getMeasures()) {
			for (Measure.Block block : measure.getBlocks()) {
				meterCollector.add(buildPlatformMeter(device, measure.getProfileStatus(), block));
			}
		}

		return meterCollector;
	}

	private PlatformMeter buildPlatformMeter(final Device device, final String quality, final Measure.Block block) {
		InstantaneousValuesMeter instantaneousValuesMeter = (InstantaneousValuesMeter) buildPlatformMeter(quality,block);
		instantaneousValuesMeter.setMeasurementIntervalCode(device.getInterval().name());
		instantaneousValuesMeter.setMeterCode(device.getSerialNumber());

		return instantaneousValuesMeter;
	}

	@Override
	protected PlatformMeter buildPlatformMeter(final String quality, final Measure.Block block) {
		return InstantaneousValuesMeter.builder()
				.quality(quality)
				.day(block.getTimestamp())
				.value(block.getValue())
				.unitM(block.getMagnitude())
				.magnitudeCode(block.getChannel())
				.obisCode(block.getCode())
				.build();
	}
}
