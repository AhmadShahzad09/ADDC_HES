package com.minsait.oum.mdc.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Request {

	public Request() {
		devices = new ArrayList<Device>();
	}

	private String idRequest;
	private String originalIdRequest;
	private ResponseType responseType;
	private RequestType requestType;
	private long time;
	private RequestStatus status;
	private String errorMessage;
	private List<Device> devices;
	private String payload;
	private String serialnumber;
	private String result;
	private String description;
	private String version;
	private Long clock;
	private String gwPasswordStatus;

	public boolean hasStatus() {
		return this.getStatus() != null;
	}
}
