package com.minsait.oum.mdc.stream.heartbeat.service;

import java.util.Optional;

import com.minsait.oum.mdc.stream.heartbeat.domain.DeviceStatus;

import lombok.Builder;
import lombok.Data;

public interface HeartbeatDataService {

	public Optional<DeviceStatus> getDeviceStatus(String deviceName);

	public GatewayParamsResponse getGatewayParams(String deviceName);

	public boolean isChangeDeviceStatusWebServiceEnabled();

	public void setDeviceStatus(String deviceName, DeviceStatus status);

	public boolean isChangeDeviceIpWebServiceEnabled();

	public void setDeviceIp(String deviceName, String ip, int port);

	public void updateEquipmentUnreachableByLastHours();

	@Data
	@Builder
	public static final class GatewayParamsResponse {
		private String body;
		private boolean failed;
	}
}
