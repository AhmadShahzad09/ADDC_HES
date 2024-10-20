package com.minsait.oum.mdc.mqtt.input.alarms;

import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Measure;
import com.minsait.oum.mdc.domain.Order;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.AbstractPlatformMessageHandlerProcessor;
import com.minsait.oum.mdc.platform.PlatformDevice;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.event.EventMeter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class EventsHandler extends AbstractPlatformMessageHandlerProcessor {

	@Autowired
	private EventsAdapter adapter;

	@Override
	protected AbstractMessageAdapter getAdapter() {
		return adapter;
	}

	@Override
	protected String getActionServiceName() {
		return "EventActionWS";
	}

	@Override
	protected PlatformDevice buildPlatformDevice(final Device device, final Order order, final String pid, final long taskId) {
		return PlatformDevice.builder()
				.orderId(order.getId())
				.requestId(order.getRequestId())
				.pid(pid)
				.taskId(taskId)
				.deviceId(device.getSerialNumber())
				.deviceType("METER")
				.meterCollector(super.buildMeterCollector(device))
				.build();
	}

	@Override
	protected PlatformMeter buildPlatformMeter(final String quality, final Measure.Block block) {
		return EventMeter.builder()
				.quality(quality)
				.eventDateTime(block.getTimestamp())
				.eventCode(block.getEventCode())
				.device(block.getDevice())
				.build();
	}
}
