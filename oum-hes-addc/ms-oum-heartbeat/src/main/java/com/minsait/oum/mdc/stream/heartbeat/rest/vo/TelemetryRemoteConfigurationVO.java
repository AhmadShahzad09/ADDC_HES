package com.minsait.oum.mdc.stream.heartbeat.rest.vo;

import java.util.function.Function;

import com.minsait.oum.mdc.domain.Device;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelemetryRemoteConfigurationVO {

	private String serialNumber;
	private String dataType;
	private String value;
	private Long registerDate;

	public static TelemetryRemoteConfigurationVO from(Device device, String dataType,
			Function<Device, String> valueSupplier) {
		return TelemetryRemoteConfigurationVO.from(device, dataType, valueSupplier.apply(device));
	}

	public static TelemetryRemoteConfigurationVO from(Device device, String dataType, String value) {

		TelemetryRemoteConfigurationVO result = TelemetryRemoteConfigurationVO.builder().build();
		result.setSerialNumber(device.getSerialNumber());
		result.setRegisterDate(System.currentTimeMillis());
		result.setDataType(dataType);
		result.setValue(value);

		return result;
	}

}
