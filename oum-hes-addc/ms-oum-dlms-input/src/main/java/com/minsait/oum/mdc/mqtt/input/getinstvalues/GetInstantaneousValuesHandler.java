package com.minsait.oum.mdc.mqtt.input.getinstvalues;

import com.minsait.oum.mdc.domain.Measure;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.AbstractPlatformMessageHandlerProcessor;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.instantaneousvalues.InstantaneousValuesMeter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class GetInstantaneousValuesHandler extends AbstractPlatformMessageHandlerProcessor {

	@Autowired
	private GetInstantaneousValuesAdapter adapter;

	@Override
	public AbstractMessageAdapter getAdapter() {
		return adapter;
	}

	@Override
	public String getActionServiceName() {
		return "InstValuesActionWS";
	}

	@Override
	protected PlatformMeter buildPlatformMeter(final String quality, final Measure.Block block) {
		return InstantaneousValuesMeter.builder()
				.quality(quality)
				.day(block.getTimestamp())
				.value(block.getValue())
				.magnitudeCode(block.getChannel())
				.build();
	}

}
