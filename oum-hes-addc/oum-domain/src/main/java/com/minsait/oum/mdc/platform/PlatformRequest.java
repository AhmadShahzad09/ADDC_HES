package com.minsait.oum.mdc.platform;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class PlatformRequest {
    @Getter
    @JsonProperty
    private List<PlatformDevice> device;
}
