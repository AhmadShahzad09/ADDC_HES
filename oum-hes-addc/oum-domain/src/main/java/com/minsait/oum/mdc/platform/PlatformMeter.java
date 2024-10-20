package com.minsait.oum.mdc.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@Jacksonized
@Data
public class PlatformMeter {
    @JsonProperty
    private String quality;
}
