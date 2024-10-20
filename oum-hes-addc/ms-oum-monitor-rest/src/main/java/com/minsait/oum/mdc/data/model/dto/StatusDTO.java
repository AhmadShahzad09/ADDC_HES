package com.minsait.oum.mdc.data.model.dto;

import com.minsait.oum.mdc.data.model.Status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusDTO {

	private String code;

	private Integer id;

	public StatusDTO(Status status) {
		this.code = status.name();
		this.id = status.ordinal();
	}
	
}
