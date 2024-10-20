package com.minsait.oum.mdc.platform;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class PlatformMessage {

    @Getter
    @JsonProperty
    private List<PlatformRequest> requests;
}
