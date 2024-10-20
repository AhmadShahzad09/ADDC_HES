package com.minsait.oum.mdc.domain;

import lombok.Data;


@Data
public class MaxDemand {

		private long timestamp;
		private String value;
		private String magnitude;
		private String channel;
		private String code;
		private long maxDemandTime;
}
