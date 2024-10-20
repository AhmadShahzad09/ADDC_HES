package com.minsait.oum.mdc.converters;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.model.EventLogEntry;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.event.EventMeter;

@Component
public class EventLogConverter extends AbstractProfileConverter<EventLogEntry,List<PlatformMeter>>{


    public String getActionServiceName() {
        return "EventActionWS";
    }


    @Override
    public List<PlatformMeter> convert(EventLogEntry eventLogEntry) {
    	
        long day = Long.valueOf(eventLogEntry.getClock().getTime()/1000).longValue();

        return Arrays.asList(EventMeter.builder()
        		.eventDateTime(day)
        		.eventCode(String.valueOf(eventLogEntry.getEventCode()))
                .build());
    }
}
