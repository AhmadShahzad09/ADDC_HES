package com.minsait.oum.mdc.mqtt.input.commission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.web.client.RestClientException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.mdc.data.model.Call;
import com.minsait.mdc.util.MdmRestClient;
import com.minsait.mdc.util.Status;
import com.minsait.oum.mdc.application.ProfileApplication;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommissionHandler implements MessageHandler {

	@Autowired
	private Gson gson;

	@Autowired
	private ProfileApplication application;

	@Autowired
	private CommissionAdapter adapter;

	@Autowired
	private MdmRestClient _restClient;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		try {
			log.debug("==> Message: " + message.getHeaders() + "->" + message.getPayload());

			CommissionResult commissionResult = CommissionResult.from(Status.FINISH_OK, null);
			
			// Cambiamos el numero de serie por el codigo del equipo
			JsonObject finishComissionJsonObject = gson.fromJson(message.getPayload().toString(), JsonObject.class);
			JsonArray jsonDevices = finishComissionJsonObject.get("meters").getAsJsonArray();
			for (JsonElement jsonDevice : jsonDevices) {
				try {
					String eqCode = _restClient.getEqCodeBySerialNumber(jsonDevice.getAsJsonObject().get("serialNumber").getAsString());
					jsonDevice.getAsJsonObject().addProperty("serialNumber", eqCode);
					if (!_restClient.isVerticalDevice(jsonDevice.getAsJsonObject().get("serialNumber").getAsString())) {
						jsonDevice.getAsJsonObject().addProperty("status", RequestStatus.ERROR.name());
						jsonDevice.getAsJsonObject().addProperty("reason","Device " + jsonDevice.getAsJsonObject().get("serialNumber").getAsString() + " not found: probably is managed by another MQTT microservice instance");
					}
				} catch (Exception e) {
					log.error(e.getMessage());
					jsonDevice.getAsJsonObject().addProperty("status", RequestStatus.ERROR.name());
					jsonDevice.getAsJsonObject().addProperty("reason", "serialNumber not found");
				}
			}

			
			try {

				commissionResult.setRequest(adapter.convert(finishComissionJsonObject.toString()));

			} catch (CommissionException cex) {
				commissionResult.setRequest(cex.getRequest());
				commissionResult.getRequest().getDevices().stream()
						.forEach(device -> device.setErrorMessage(cex.getMessage() + "; " + device.getErrorMessage()));

			}

			// set finish commission in Mdm: change device status and gateway status
			// according to commission message device statuses
			commissionResult = this.finishComission(commissionResult, finishComissionJsonObject.toString());

			// if finish commission has been properly executed
			if (!commissionResult.isFailed()) {
				// Count the number of meters with error
				long failedMeters = commissionResult.getRequest().getDevices().stream()
						.filter(d -> d.getMeterStatus().equals(Status.ERROR.name())).count();

				// If there are failed meters, set request and call status to ERROR
				if (failedMeters > 0) {
					commissionResult.setCallStatus(Status.ERROR);
					commissionResult.getRequest()
							.setErrorMessage((commissionResult.getRequest().getErrorMessage() == null ? ""
									: commissionResult.getRequest().getErrorMessage()) + "There are " + failedMeters
									+ " meters with status ERROR");
				}

			}

			// create call from request
			Call call = this.application.createCallMonitorEntry(commissionResult.getRequest());

			// if commission result is not failed call is created with running state, it
			// must be closed here
			// setting FINISH_OK status
			if (call != null && !commissionResult.isFailed())
				this.application.updateCallAndTaskStatus(call, commissionResult.getCallStatus(),
						commissionResult.getRequest().getErrorMessage(), commissionResult.getRequest());

		} catch (Exception ex) {
			log.error("ERROR: " + ex.getMessage());
		}

	}

	private CommissionResult finishComission(CommissionResult result, String finishComissionMessage) {

		try {

			ResponseEntity<String> finishComissionResponse = _restClient.setFinishComission(finishComissionMessage);

			// If the update operation was not successful set all devices as error
			if (!finishComissionResponse.getStatusCode().is2xxSuccessful())
				setAllDeviceFailed(result, "response code " + finishComissionResponse.getStatusCode());

		} catch (RestClientException rcex) {
			setAllDeviceFailed(result, rcex.getMessage());
		}

		return result;
	}

	private void setAllDeviceFailed(CommissionResult result, String errorReason) {

		CommissionErrorMsgBuilder errorBuilderFn = (idRequest, reasonMsg) -> String
				.format("error updating devices commission with request id %s: %s", idRequest, reasonMsg);

		result.setCallStatus(Status.ERROR);
		result.setFailed(true);

		String errorMsg = errorBuilderFn.create(result.getRequest().getIdRequest(), errorReason);

		log.error(errorMsg);

		result.getRequest().getDevices().stream().forEach(device -> {
			device.setErrorMessage(errorMsg);
			device.setMeterStatus(Status.ERROR.name());
		});
		result.getRequest().setErrorMessage(
				(result.getRequest().getErrorMessage() == null ? "" : result.getRequest().getErrorMessage())
						+ errorMsg);
	}

	@FunctionalInterface
	private static interface CommissionErrorMsgBuilder {

		public String create(String requestId, String errorReason);
	}

	@Data
	private static final class CommissionResult {
		private Status callStatus;
		private Request request;
		private boolean failed;

		public static final CommissionResult from(Status callStatus, Request request) {
			CommissionResult result = new CommissionResult();
			result.setCallStatus(callStatus);
			result.setRequest(request);

			return result;
		}
	}
}
