package com.minsait.oum.mdc.mqtt.input.setcurrentrangelow;

import com.minsait.mdc.util.domain.*;
import com.minsait.oum.mdc.mqtt.input.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.*;

@Slf4j
public class SetCurrentRangeLowHandler extends AbstractMessageHandlerProcessor {

	@Autowired
	private SetCurrentRangeLowAdapter adapter;

	@Override
	public void handleMessage(Message<?> message) {

		this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
				(request, call, device) -> this.getMdmRestClient()
						.setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "range_low_status",
								device.getCurrentRangeLowStatus())));

	}

}
