package com.minsait.oum.mdc.stream.heartbeat.worker;

import com.minsait.oum.mdc.stream.heartbeat.data.ProfileApplication;
import com.minsait.oum.mdc.stream.heartbeat.domain.DeviceStatus;
import com.minsait.oum.mdc.stream.heartbeat.service.HeartbeatDataService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Component
public class UpdateHeartBeatStatus {

    @Autowired
    @Getter
    private ProfileApplication application;

    @Autowired
    @Getter
    private HeartbeatDataService dataService;

    protected long calcRangeInMilliseconds(final long gap, final ChronoUnit chronoUnit) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime startProcessTime = currentTime.minus(gap, chronoUnit);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(startProcessTime, ZoneId.systemDefault());

        return zonedDateTime.toInstant().toEpochMilli();
    }

    protected DeviceStatus getDeviceStatus(final String deviceName) {
        DeviceStatus status = null;
        Optional<DeviceStatus> statusOpt = dataService.getDeviceStatus(deviceName);
        if (statusOpt.isPresent())
            status = statusOpt.get();

        return status;
    }

}
