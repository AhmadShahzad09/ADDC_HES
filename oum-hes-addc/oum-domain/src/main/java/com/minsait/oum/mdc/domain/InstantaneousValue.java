package com.minsait.oum.mdc.domain;

import lombok.Data;


@Data
public class InstantaneousValue {

		private long timestamp;
		private Double value;
		private String magnitude;
		private String channel;
		private String code;
}
