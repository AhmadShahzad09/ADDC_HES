package com.minsait.oum.mdc.domain.reconnection;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReconnectionStatus {
    private String outputState;
    private String controlState;
    private String mode;
}
