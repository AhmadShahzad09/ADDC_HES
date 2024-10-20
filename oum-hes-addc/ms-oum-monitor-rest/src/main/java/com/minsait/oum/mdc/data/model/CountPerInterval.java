package com.minsait.oum.mdc.data.model;

import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public class CountPerInterval {			
		@Id
		String id;
		Long count;
}
