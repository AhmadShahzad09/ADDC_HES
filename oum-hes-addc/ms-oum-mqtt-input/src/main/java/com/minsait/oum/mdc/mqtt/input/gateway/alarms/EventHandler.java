package com.minsait.oum.mdc.mqtt.input.gateway.alarms;

import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Measure;
import com.minsait.oum.mdc.domain.Order;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.AbstractPlatformMessageHandlerProcessor;
import com.minsait.oum.mdc.platform.PlatformDevice;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.event.EventGatewayMeter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class EventHandler extends AbstractPlatformMessageHandlerProcessor {

    @Autowired
    private EventAdapter adapter;

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
                .deviceType("GATEWAY")
                .meterCollector(super.buildMeterCollector(device))
                .build();
    }

    @Override
    protected PlatformMeter buildPlatformMeter(String quality, Measure.Block block) {
        return EventGatewayMeter.builder()
                .eventDateTime(block.getTimestamp())
                .eventCode(block.getEventCode())
                .description(block.getDescription())
                .build();
    }
}
