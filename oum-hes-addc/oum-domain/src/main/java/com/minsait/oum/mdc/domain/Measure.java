package com.minsait.oum.mdc.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Measure {

	public Measure() {
		this.blocks = new ArrayList<>();
	}
	private String profileStatus;
	private String captureTime;
	private List<Block> blocks;
	@Data
	public static class Block {
		private long timestamp;
		private Double value;
		private String magnitude;
		private String channel;
		private String code;
		private long maxDemandTime;
		private String tariff;
		private String type;
		private String eventCode;
		private Long time;
		private String device;
		private String description;
	}

}
