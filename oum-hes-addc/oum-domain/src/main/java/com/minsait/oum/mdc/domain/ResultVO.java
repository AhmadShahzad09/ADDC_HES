package com.minsait.oum.mdc.domain;

import com.minsait.mdc.util.Status;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultVO {
	
	private Status status;
	
	private String message;

}
