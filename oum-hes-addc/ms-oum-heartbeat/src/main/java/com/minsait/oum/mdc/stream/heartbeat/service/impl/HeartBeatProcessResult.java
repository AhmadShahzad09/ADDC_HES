package com.minsait.oum.mdc.stream.heartbeat.service.impl;

import com.minsait.oum.mdc.domain.ResultVO;

import lombok.Data;

@Data
public class HeartBeatProcessResult {

	private boolean gatewayFound = true;
	private ResultVO result;
	private String gatewaySN;

	public static HeartBeatProcessResult from(boolean gatewayFound, String gatewaySN, ResultVO innerResult) {
		HeartBeatProcessResult result = new HeartBeatProcessResult();
		result.setGatewayFound(gatewayFound);
		result.setGatewaySN(gatewaySN);
		result.setResult(innerResult);

		return result;
	}

	public static HeartBeatProcessResult from(String gatewaySN, ResultVO innerResult) {
		return HeartBeatProcessResult.from(true, gatewaySN, innerResult);
	}
}
