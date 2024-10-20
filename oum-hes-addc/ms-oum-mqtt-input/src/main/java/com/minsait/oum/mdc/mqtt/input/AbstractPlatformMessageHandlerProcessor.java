package com.minsait.oum.mdc.mqtt.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.minsait.commands.CommandOutput;
import com.minsait.commands.impl.action.AbstractAction;
import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.domain.*;
import com.minsait.oum.mdc.platform.PlatformDevice;
import com.minsait.oum.mdc.platform.PlatformMessage;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.PlatformRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class AbstractPlatformMessageHandlerProcessor extends AbstractMessageProcessor {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationContext context;

    protected abstract AbstractMessageAdapter getAdapter();

    protected abstract String getActionServiceName();

    protected abstract PlatformMeter buildPlatformMeter(final String quality, final Measure.Block block);

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
		log.debug("==> Message: " + message.getHeaders() + "->" + message.getPayload());
        AtomicInteger failedDevices = new AtomicInteger(0);
        try {
        	this.processTelemetryCallToPlatform(this.getAdapter().convert(message.getPayload().toString()),
                (request, call, device, size, index) -> {
                    // check for failed device on profile, if true skip device
                    if (checkDeviceFailed(call, request, device)) {
                        // we are in the last device and all of them have failed? close call and return
                        if (failedDevices.incrementAndGet() == size)
                            this.closeCallAsFailed(call, request);
                        // device failed, skip device processing
                        return;
                    }
                    // if we reach here, at least one device has not failed
                    if (index == size)
                        this.executeAction(buildPlatformMessage(call, device));
                });
        } catch (Exception e) {
        	// solo deberia llegar aqui si el mensaje recibido no tiene formato json
        	e.printStackTrace();
        	log.error(InvalidFormatException.class.getName() + ": " + message.getPayload().toString());
        }
    }

    @Override
    protected void processTelemetryCall(Request request, boolean upsertWithTaskStatus,
                                        AbstractMessageProcessor.DeviceProcessor deviceProcessor) {
        checkDeviceProcessor(deviceProcessor);
        // always create MDM call: status RUNNING
        Call call = getApplication().createCallMonitorEntry(request);
        if (call == null) {
            throw new UnsupportedOperationException(String.format("cannot create call from request %s", request));
        }
        try {
            if (request.getStatus().equals(RequestStatus.OK)) {
                // iterate over all request devices and notify mdm
                int deviceIndex = 1;
                for (Device device : request.getDevices()) {
                    // update platform with received data
                    processPlatformDeviceResponse(call, request, device, upsertWithTaskStatus,
                            (AbstractPlatformMessageHandlerProcessor.PlatformNotifier) deviceProcessor, deviceIndex++);
                }
            } else {
                // if request is failed finish call and all its tasks immediately, changing its
                // status to ERROR
                updateCallStatus(call, Status.from(request.getStatus()), request.getErrorMessage(), true,
                        upsertWithTaskStatus);
            }

        } catch (UnsupportedOperationException uopex) {
            if (call != null)
                // something wrong (updating mdm for example), update call status
                updateCallStatus(call, Status.ERROR, uopex.getMessage(), true, upsertWithTaskStatus);
            throw uopex;

        } catch (Exception ex) {
            // something wrong (updating mdm for example), update call status
            updateCallStatus(call, Status.ERROR, ex.getMessage(), true, upsertWithTaskStatus);

        }
    }

    protected PlatformMessage buildPlatformMessage(final Call call, final Device device) {
        return PlatformMessage.builder()
                .requests(buildPlatformRequests(call, device))
                .build();
    }

    private List<PlatformRequest> buildPlatformRequests(final Call call, final Device device) {
        List<PlatformRequest> platformRequests = new ArrayList<>();
        // TODO Se mete solamente un requests en el array porque solamente tenemos un Call,
        //  aclarar con plataforma cuando puede haber mas de un elemento en este array
        platformRequests.add(buildPlatformRequest(call, device));

        return platformRequests;
    }

    private PlatformRequest buildPlatformRequest(final Call call, final Device device) {
        return PlatformRequest.builder()
                .device(buildPlatformDevices(call,device))
                .build();
    }

    private List<PlatformDevice> buildPlatformDevices(final Call call, final Device device) {
        List<PlatformDevice> platformDevices = new ArrayList<>();
        String callID = call.getPid() != null ? call.getPid() : call.getIdDC();
        for (EquipmentTask task : this.resolveTasksFrom(call, device)) {
            for (Order order : task.getOrder()) {
                platformDevices.add(buildPlatformDevice(device, order, callID, task.getId()));
            }
        }

        return platformDevices;
    }

    protected PlatformDevice buildPlatformDevice(final Device device, final Order order, final String pid, final long taskId) {
        return PlatformDevice.builder()
                .meterId(device.getSerialNumber())
                .orderId(order.getId())
                .requestId(order.getRequestId())
                .pid(pid)
                .taskId(taskId)
                .meterCollector(buildMeterCollector(device))
                .build();
    }

    protected List<PlatformMeter> buildMeterCollector(final Device device) {
        List<PlatformMeter> meterCollector = new ArrayList<>();
        for (Measure measure : device.getMeasures()) {
            for (Measure.Block block : measure.getBlocks()) {
                meterCollector.add(buildPlatformMeter(measure.getProfileStatus(), block));
            }
        }

        return meterCollector;
    }

    protected void executeAction(final PlatformMessage platformMessage) {
        String platformMessageString = "";
        try {
            platformMessageString = objectMapper.writeValueAsString(platformMessage.getRequests());
        } catch (JsonProcessingException jpe) {
            throw new UnsupportedOperationException(String.format(
                    "error building load profile platform request:  %s ", platformMessage.toString()));
        }
        log.info("Platform Message Sent: " + platformMessageString);
        sendThroughActionService(getActionServiceName(), platformMessageString);
    }

    private void sendThroughActionService(String actionServiceName, String data) {
        final CommandOutput outputResult = new CommandOutput();
        outputResult.setResult(data);
        // Se envía a kafka - MDM
        log.debug("about to send new message to kafka using action'{}': {}", actionServiceName, data);
        final AbstractAction actionService = (AbstractAction) this.context.getBean(actionServiceName);
        actionService.executeActions(outputResult);
        log.debug("new message sent to kafka succesfully using action'{}'", actionServiceName, data);
    }

    protected void processTelemetryCallToPlatform(Request request, AbstractPlatformMessageHandlerProcessor.PlatformNotifier platformNotifier) {
        this.processTelemetryCall(request, true, platformNotifier);
    }

    protected void processPlatformDeviceResponse(Call call, Request request, Device device, boolean upsertWithTaskStatus,
                                                 AbstractPlatformMessageHandlerProcessor.PlatformNotifier platformNotifier, int index) {
        try {

            if (platformNotifier != null)
                platformNotifier.notify(request, call, device, request.getDevices().size(), index);

            // if every thing is ok, do not update task status, platform should take care of
            // that

        } catch (NotifierException mnex) {
            // error notifying device to platform, set call task as failed
            this.updateTaskOrderStatus(call, request, device, Status.FINISH_WITH_ERROR, mnex.getMessage(),
                    upsertWithTaskStatus);
        }
    }

    @FunctionalInterface
    public static interface PlatformNotifier extends AbstractMessageProcessor.DeviceProcessor {

        public static AbstractPlatformMessageHandlerProcessor.PlatformNotifier empty = (request, call, device, size, index) -> {};

        public void notify(Request request, Call call, Device device, int size, int index) throws NotifierException;
    }

}
