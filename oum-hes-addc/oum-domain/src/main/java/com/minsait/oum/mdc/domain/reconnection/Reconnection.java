package com.minsait.oum.mdc.domain.reconnection;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Reconnection {

    private ReconnectionStatus status;
    private ReconnectionReading reading;

}
