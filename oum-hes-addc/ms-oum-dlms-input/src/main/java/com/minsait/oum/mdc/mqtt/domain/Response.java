package com.minsait.oum.mdc.mqtt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Response {
    @JsonProperty(required = true)
    private String idRequest;
    @JsonProperty(required = true)
    private long time;
    @Setter
    @JsonProperty
    private StatusCode statusCode;
    @JsonProperty
    private String errorCode;
    @Setter
    @JsonProperty
    private String errorMessage;
}
