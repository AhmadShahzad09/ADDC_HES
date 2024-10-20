package com.minsait.commands.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;

@ConditionalOnProperty(prefix = "mdm.etisalat.platform", name = "useBusProducers", matchIfMissing = true)
@EnableBinding(MessageStream.class)
public class StreamsConfig {

}
