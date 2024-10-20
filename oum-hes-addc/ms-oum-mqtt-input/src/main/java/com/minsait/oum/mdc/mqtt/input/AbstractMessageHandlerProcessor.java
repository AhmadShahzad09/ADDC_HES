package com.minsait.oum.mdc.mqtt.input;

import java.util.List;

import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMessageHandlerProcessor extends AbstractMessageProcessor {


	protected void processTelemetryCall(Request request, boolean upsertWithTaskStatus,
			DeviceProcessor deviceProcessor) {

		checkDeviceProcessor(deviceProcessor);

		// always create MDM call: status RUNNING
		Call call = getApplication().createCallMonitorEntry(request);

		if (call == null)
			throw new UnsupportedOperationException(String.format("cannot create call from request %s", request));

		try {

			if (request.getStatus().equals(RequestStatus.OK)) {
				// iterate over all request devices and notify mdm
				List<Device> devices = request.getDevices();
				if (devices == null || devices.isEmpty()) {
					String deviceName = call.getTasks().get(0).getDeviceName();
					Device device = new Device();
					device.setIdEquipment(deviceName);
					device.setSerialNumber(deviceName);
					processMdmDeviceResponse(call, request, device, upsertWithTaskStatus, (MdmNotifier) deviceProcessor);
				} else for (Device device : request.getDevices()) {
					processMdmDeviceResponse(call, request, device, upsertWithTaskStatus, (MdmNotifier) deviceProcessor);
				}

				// we only manage call end when we are using mdm notifiers
				if (deviceProcessor instanceof MdmNotifier)
					// everything OK, update call status FINISHED_OK
					updateCallStatus(call, Status.FINISH_OK, null, upsertWithTaskStatus);

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

	protected void processTelemetryCallToTaskListener(Request request, AbstractMessageHandlerProcessor.MdmNotifier mdmNotifier) {
		this.processTelemetryCall(request, true, mdmNotifier);
	}

	protected void processMdmDeviceResponse(Call call, Request request, Device device, boolean upsertWithTaskStatus,
											AbstractMessageHandlerProcessor.MdmNotifier mdmNotifier) {
		try {

			// if device has errors, do not notify to MDM to reduce unneeded requests
			if (mdmNotifier != null && !device.hasErrors())
				mdmNotifier.notify(request, call, device);

			this.updateTaskOrderStatus(call, request, device,
					device.hasErrors() ? Status.FINISH_WITH_ERROR : Status.FINISH_OK,
					device.hasErrors() ? device.getErrorMessage() : null, upsertWithTaskStatus);

		} catch (NotifierException mnex) {
			// error notifying device, set call task as failed
			this.updateTaskOrderStatus(call, request, device, Status.FINISH_WITH_ERROR, mnex.getMessage(),
					upsertWithTaskStatus);
		}
	}

	@FunctionalInterface
	public static interface MdmNotifier extends AbstractMessageProcessor.DeviceProcessor {

		public static AbstractMessageHandlerProcessor.MdmNotifier empty = (request, call, device) -> {};

		public void notify(Request request, Call call, Device device) throws NotifierException;
	}

}
