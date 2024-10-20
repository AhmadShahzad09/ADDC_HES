package com.minsait.oum.mdc.mqtt.input.energyregister;

import com.minsait.mdc.util.MeasurementUnitConverterUtils;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Measure;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.AbstractPlatformMessageHandlerProcessor;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.absoluteprofile.AbsoluteProfileMeter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class EnergyRegistersHandler extends AbstractPlatformMessageHandlerProcessor {

	@Autowired
	private EnergyRegistersAdapter adapter;

	@Override
	public AbstractMessageAdapter getAdapter() {
		return adapter;
	}

	@Override
	public String getActionServiceName() {
		return "AbsoluteLoadProfileActionWS";
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
		AbsoluteProfileMeter absoluteProfileMeter = (AbsoluteProfileMeter) buildPlatformMeter(quality,block);
		if (device.getInterval() != null) {
			absoluteProfileMeter.setMeasurementIntervalCode(device.getInterval().name());
		}

		return absoluteProfileMeter;
	}

	@Override
	protected PlatformMeter buildPlatformMeter(final String quality, final Measure.Block block) {
		Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(block.getValue(),block.getMagnitude());
		return AbsoluteProfileMeter.builder()
				.quality(quality)
				.day(block.getTimestamp())
				.value(unitConverted.getFirst())
				.unitM(unitConverted.getSecond())
				.magnitudeCode(block.getChannel())
				.obisCode(block.getCode())
				.build();
	}

}
