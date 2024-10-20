package com.minsait.oum.mdc.stream.heartbeat.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.minsait.oum.mdc.stream.heartbeat.domain.DeviceStatus;
import com.minsait.oum.mdc.stream.heartbeat.rest.MdmRestClient;
import com.minsait.oum.mdc.stream.heartbeat.service.HeartbeatDataService;

@Service
public class HeartbeatDataServiceImpl implements HeartbeatDataService {

	@Autowired
	private MdmRestClient _restClient;

	@Override
	public Optional<DeviceStatus> getDeviceStatus(String deviceName) {
		return _restClient.getDeviceStatus(deviceName);
	}

	@Override
	public GatewayParamsResponse getGatewayParams(String deviceName) {
		ResponseEntity<String> response = _restClient.getGatewayParams(deviceName);
		return GatewayParamsResponse.builder().body(response.getBody())
				.failed(!response.getStatusCode().is2xxSuccessful()).build();

	}

	@Override
	public boolean isChangeDeviceStatusWebServiceEnabled() {
		return _restClient.isChangeDeviceIpWebServiceEnabled();
	}

	@Override
	public void setDeviceStatus(String deviceName, DeviceStatus status) {
		_restClient.setDeviceStatus(deviceName, status);

	}

	@Override
	public boolean isChangeDeviceIpWebServiceEnabled() {
		return _restClient.isChangeDeviceIpWebServiceEnabled();
	}

	@Override
	public void setDeviceIp(String deviceName, String ip, int port) {
		_restClient.setDeviceIp(deviceName, ip, port);

	}

	@Override
	public void updateEquipmentUnreachableByLastHours() {
		_restClient.updateEquipmentUnreachableByLastHours();
	}

}
