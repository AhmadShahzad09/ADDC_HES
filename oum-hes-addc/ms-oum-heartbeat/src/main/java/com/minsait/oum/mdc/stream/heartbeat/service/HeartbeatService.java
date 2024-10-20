package com.minsait.oum.mdc.stream.heartbeat.service;

import com.minsait.oum.mdc.domain.Request;

public interface HeartbeatService {

	public void handle(Request heartbeatRequest);

}
