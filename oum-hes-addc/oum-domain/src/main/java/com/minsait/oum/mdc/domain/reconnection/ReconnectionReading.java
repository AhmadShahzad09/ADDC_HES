package com.minsait.oum.mdc.domain.reconnection;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReconnectionReading {
    private String value;
    private String channel;
    private String unitM;
    private String code;
}
