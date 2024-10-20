package com.minsait.oum.mdc.mqtt.input;

import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.MdmRestClient;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.application.ProfileApplication;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.kafka.publisher.TaskStatusMessageService;
import com.minsait.oum.mdc.listener.task.PlatformTaskVO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractMessageProcessor implements MessageHandler {

    @Autowired
    @Getter
    private ProfileApplication application;

    @Autowired
    private MdmRestClient _mdmRestClient;

    @Autowired
    private TaskStatusMessageService _taskStatusService;

    protected MdmRestClient getMdmRestClient() {
        return _mdmRestClient;
    }

    protected abstract void processTelemetryCall(Request request, boolean upsertWithTaskStatus,
                                                 AbstractMessageProcessor.DeviceProcessor deviceProcessor);

    protected boolean checkDeviceFailed(Call call, Request request, Device device) throws NotifierException {
        boolean result = false;
        if (device.getErrorMessage() != null && !device.getErrorMessage().isEmpty()) {

            this.updateTaskOrderStatus(call, request, device, Status.FINISH_WITH_ERROR, device.getErrorMessage(), true);
            result = true;
        }

        return result;
    }

    protected void closeCallAsFailed(Call call, Request request) {
        this.updateCallStatus(call, Status.FINISH_WITH_ERROR, this.resolveCallErrorMessage(request,
                String.format("all devices failed for %s request", request.getRequestType())), true, true);
    }

    private String resolveCallErrorMessage(Request request, String defaultMessage) {
        return (request.getErrorMessage() != null && !request.getErrorMessage().isEmpty()) ? request.getErrorMessage()
                : defaultMessage;
    }

    protected void checkDeviceProcessor(AbstractMessageProcessor.DeviceProcessor deviceProcessor) {
        if (deviceProcessor == null)
            throw new UnsupportedOperationException(
                    "device processor cannot be null. either use MdmNotifier or PlarformNotifier or its empty values (MdmNotifier.empty or PlarformNotifier.empty) ");
    }

    protected List<EquipmentTask> resolveTasksFrom(Call call, Device device) {
        return call.getTasks().stream().filter(task -> task.getDeviceName().equals(device.getSerialNumber()))
                .collect(Collectors.toList());
    }

    protected EquipmentTask resolveTaskFrom(Call call, Device device) {
        return this.findTaskFrom(call, device).orElseThrow(
                () -> new UnsupportedOperationException(String.format("cannot find task from call id %s and device %s",
                        call.getPid() != null ? call.getPid() : call.getIdDC(), device.getSerialNumber())));

    }

    protected Order resolveOrderFrom(Call call, Request request, EquipmentTask task) {
        return this.findOrderFrom(task, request.getResponseType())
                .orElseThrow(() -> new UnsupportedOperationException(
                        String.format("cannot find order from call id %s and task %s and response type %s",
                                call.getPid() != null ? call.getPid() : call.getIdDC(), task.getDeviceName(),
                                request.getResponseType())));

    }

//    protected Order resolveOrderFrom(Call call, ResponseType responseType, Device device) {
//        return this.findOrderFrom(call, responseType, device)
//                .orElseThrow(() -> new UnsupportedOperationException(
//                        String.format("cannot find order from call id %s and device %s and response type %s",
//                                call.getPid() != null ? call.getPid() : call.getIdDC(), device.getSerialNumber(),
//                                responseType)));
//
//    }

    private Optional<EquipmentTask> findTaskFrom(Call call, Device device) {
        return call.getTasks().stream().filter(task -> task.getDeviceName().equals(device.getSerialNumber()))
                .findFirst();
    }

    private Optional<Order> findOrderFrom(Call call, ResponseType responseType, Device device) {
        Optional<EquipmentTask> taskOpt = this.findTaskFrom(call, device);
        if (!taskOpt.isPresent())
            return Optional.empty();

        return this.findOrderFrom(taskOpt.get(), responseType);
    }

    private Optional<Order> findOrderFrom(EquipmentTask task, ResponseType responseType) {

        return task.getOrder().stream().filter(order -> order.getName().equals(responseType.getCode())).findFirst();
    }

    protected void updateCallStatus(Call call, Status status, String statusMessage, boolean upsertWithTaskListener) {
        this.updateCallStatus(call, status, statusMessage, false, upsertWithTaskListener);
    }

    protected void updateCallStatus(Call call, Status status, String statusMessage, boolean applyToAllTasks,
                                  boolean upsertWithTaskListener) {
        if (upsertWithTaskListener) {
            _taskStatusService.send(this.createFrom(call, status, statusMessage, applyToAllTasks));
        } else {
            // update call status but not task status
            this.application.updateCallStatus(call, status, statusMessage, false);
        }
    }

    protected void updateTaskOrderStatus(Call call, Request request, Device device, Status status, String statusMessage,
                                       boolean upsertWithTaskListener) {

        EquipmentTask task = this.resolveTaskFrom(call, device);
        Order order = resolveOrderFrom(call, request, task);

        if (device.getIdEquipment() == null) {
            if (task.getDeviceId() != null) {
                device.setIdEquipment(task.getDeviceId().toString());
            } else {
                device.setIdEquipment(task.getDeviceName());
            }

        }

        if (upsertWithTaskListener) {
            _taskStatusService.send(this.createFrom(call, device, status, statusMessage));
        } else {
            // just update task and order on call, later call object must be persisted
            // maintaining this changes
            task.setStatus(status);
            order.setStatus(status);
            order.setPayload(request.getPayload());

            if (statusMessage != null && !statusMessage.isEmpty()) {
                if (status.equals(Status.ERROR)) {
                    order.setError(Collections.singletonMap(1, statusMessage));
                } else {
                    order.setInfo(Collections.singletonMap(1, statusMessage));
                }
            }

        }
    }

    private PlatformTaskVO createFrom(Call call, Status status, String statusMessage, boolean applyToAllTasks) {
        return this.createFrom(call, null, status, statusMessage, applyToAllTasks);
    }

    private PlatformTaskVO createFrom(Call call, Device device, Status status, String statusMessage) {
        return this.createFrom(call, device, status, statusMessage, false);
    }

    private PlatformTaskVO createFrom(Call call, Device device, Status status, String statusMessage,
                                      boolean applyToAllTasks) {
        PlatformTaskVO.PlatformTaskVOBuilder taskBuilder = PlatformTaskVO.builder()
                .pid(call.getIdDC() == null ? call.getPid() : call.getIdDC());

        taskBuilder.applyToAllTasks(applyToAllTasks);
        // find task for device
        if (device != null)
            taskBuilder = taskBuilder.taskId(this.resolveTaskFrom(call, device).getId());

        if (statusMessage != null && !statusMessage.isEmpty()) {
            Map<Integer, String> statusMessageMap = new LinkedHashMap<>();
            statusMessageMap.put(1, statusMessage);

            if (status.equals(Status.ERROR) || status.equals(Status.FINISH_WITH_ERROR)) {
                taskBuilder = taskBuilder.error(statusMessageMap);

            } else if (status.equals(Status.FINISH_OK)) {
                taskBuilder = taskBuilder.info(statusMessageMap);

            }
        }

        return taskBuilder.build();
    }

    public static interface DeviceProcessor {
    }

}
