package com.minsait.oum.mdc.listener.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformTaskVO {

	private String pid;

	private boolean applyToAllTasks;

	private Long taskId;

	private Long orderId;

	private Object error;

	private Object debug;

	private Object info;

	private Long requestId;

	private String meterId;

}
