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
import java.util.stream.Collectors;

@Slf4j
@Component
public class UpdateToUnreachableStatusJob extends UpdateHeartBeatStatus {

    private static final String CALL_NAME = "HEART_BEAT";
    private static final Status CALL_STATUS_TO_UNREACHABLE = Status.ERROR;
    private static final long DAYS_TO_UNREACHABLE = 2;

    private List<String> checkedDevices;

    @Scheduled(cron = "${job.scheduled.unreachable}")
    public void executeToUnreachable() {
        log.info("[HEART_BEAT TO UNREACHABLE JOB] Update device status started");
        checkedDevices = new ArrayList<>();
        long milliseconds = calcRangeInMilliseconds(DAYS_TO_UNREACHABLE, ChronoUnit.DAYS);

        checkGatewaysWithoutActivity();
        checkDevicesOnlyErrors(milliseconds);

        log.info("[HEART_BEAT TO UNREACHABLE JOB] Update device status finished");
    }

    private void checkGatewaysWithoutActivity() {
        log.info("[HEART_BEAT TO UNREACHABLE JOB]  Update inactive GWs in last hours");
        getDataService().updateEquipmentUnreachableByLastHours();
    }

    private void checkDevicesOnlyErrors(final long milliseconds) {
        List<Call> calls = getApplication().findCallByStatusFromTime(CALL_NAME, CALL_STATUS_TO_UNREACHABLE, milliseconds);
        log.info("[HEART_BEAT TO UNREACHABLE JOB]  Calls found: " + calls.size());
        calls.stream().forEach(call -> {
            call.getTasks().stream().forEach(task -> {
                String deviceName = task.getDeviceName();
                DeviceStatus status = getDeviceStatus(deviceName);
                if (DeviceStatus.ACTIVE.equals(status)
                        && !checkedDevices.contains(deviceName)
                        && checkCompleteError(calls, deviceName)
                        && getDataService().isChangeDeviceStatusWebServiceEnabled()) {
                    getDataService().setDeviceStatus(deviceName, DeviceStatus.UNREACHABLE);
                }
            });
        });
    }

    private boolean checkCompleteError(final List<Call> calls, final String deviceName) {
        List<Call> filteredCalls = getCallsPerDevice(calls, deviceName);
        checkedDevices.add(deviceName);
        return filteredCalls.stream().allMatch(filterCall ->
                filterCall.getTasks().get(0).getStatus().equals(Status.ERROR));
    }

    private List<Call> getCallsPerDevice(final List<Call> calls, final String deviceName) {
        return calls.stream().filter( call -> call.getTasks().get(0).getDeviceName().equals(deviceName))
                .collect(Collectors.toList());
    }

}
