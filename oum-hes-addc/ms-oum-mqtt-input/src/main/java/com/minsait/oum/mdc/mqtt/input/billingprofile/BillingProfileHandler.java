package com.minsait.oum.mdc.mqtt.input.billingprofile;

import com.minsait.mdc.util.MeasurementUnitConverterUtils;
import com.minsait.oum.mdc.domain.Measure;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.AbstractPlatformMessageHandlerProcessor;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.billingprofile.BillingProfileMeter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

@Slf4j
public class BillingProfileHandler extends AbstractPlatformMessageHandlerProcessor {

	@Autowired
	private BillingProfileAdapter adapter;

	@Override
	protected AbstractMessageAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected String getActionServiceName() {
		return "BillingProfileActionWS";
	}

	@Override
	protected PlatformMeter buildPlatformMeter(final String quality, final Measure.Block block) {
		Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(block.getValue(),block.getMagnitude());
		return BillingProfileMeter.builder()
				.quality(quality)
				.timestamp(block.getTimestamp())
				.value(unitConverted.getFirst())
				.unitM(unitConverted.getSecond())
				.channel(block.getChannel())
				.obisCode(block.getCode())
				.tariff(block.getTariff())
				.type(block.getType())
				.build();
	}

}
