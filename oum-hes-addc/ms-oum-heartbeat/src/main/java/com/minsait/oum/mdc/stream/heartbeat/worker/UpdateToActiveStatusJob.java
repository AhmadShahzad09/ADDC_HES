package com.minsait.oum.mdc.stream.heartbeat.worker;

import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.stream.heartbeat.domain.DeviceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UpdateToActiveStatusJob extends UpdateHeartBeatStatus {

    private static final Status CALL_STATUS_TO_ACTIVE = Status.FINISH_OK;
    private static final long HOURS_TO_ACTIVE = 2;

    private List<String> checkedDevices;

    @Scheduled(cron = "${job.scheduled.active}")
    public void executeToActive() {
        log.info("[HEART_BEAT TO ACTIVE JOB] Update device status started");
        checkedDevices = new ArrayList<>();
        long milliseconds = calcRangeInMilliseconds(HOURS_TO_ACTIVE, ChronoUnit.HOURS);

        List<Call> calls = getApplication().findCallByStatusFromTime(CALL_STATUS_TO_ACTIVE, milliseconds);
        log.info("[HEART_BEAT TO ACTIVE JOB] Calls found: " + calls.size());
        calls.stream().forEach(call -> {
            call.getTasks().stream().forEach(task -> {
                String deviceName = task.getDeviceName();
                DeviceStatus status = getDeviceStatus(deviceName);
                if (DeviceStatus.UNREACHABLE.equals(status)
                        && !checkedDevices.contains(deviceName)
                        && getDataService().isChangeDeviceStatusWebServiceEnabled()) {
                    getDataService().setDeviceStatus(deviceName, DeviceStatus.ACTIVE);
                }
            });
        });
        log.info("[HEART_BEAT TO ACTIVE JOB] Update device status finished");
    }
}
