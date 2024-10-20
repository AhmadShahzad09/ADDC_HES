package com.minsait.oum.mdc.mqtt.input;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minsait.mdc.util.MdmRestClient;
import com.minsait.oum.mdc.domain.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.function.BiConsumer;

public abstract class AbstractMessageAdapter {

	private static final String ERROR_CODE_ATTR = "errorCode";
	private static final String STATUS_CODE_ATTR = "statusCode";
	private static final String ERROR_MESSAGE_ATTR = "errorMessage";
	private static final String ERROR_DEVICE_MESSAGE_ATTR = "deviceErrorMessage";

	@Autowired
	private Gson gson;
	
	@Autowired
	private MdmRestClient _mdmRestClient;

	public Request convert(String jsonRequest) {
		Request result = new Request();
		result.setPayload(jsonRequest);
		result.setResponseType(this.getResponseType());
		result.setRequestType(this.getRequestType());
		JsonObject jsonRequestObject = null;
		try {
			jsonRequestObject = gson.fromJson(jsonRequest.replaceAll("\\^M", "").replaceAll("\\^", ""),
					JsonObject.class);

			result = this.innerConvert(this.loadBaseRequest(result, jsonRequestObject), jsonRequestObject);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(RequestStatus.ERROR);
			// try to find attribute 'errorCode' on request if present use it
			String errMsg = jsonRequestObject != null && jsonRequestObject.has(ERROR_CODE_ATTR) 
					? jsonRequestObject.get(ERROR_CODE_ATTR).getAsString()
					: e.getMessage();
			result.setErrorMessage(errMsg);
		}

		return result;
	}

	public abstract Request innerConvert(Request request, JsonObject requestJson) throws MessageAdapterException;

	public abstract ResponseType getResponseType();

	public abstract RequestType getRequestType();

	protected void parseDevice(Request request, String deviceAttr, JsonObject jsonRequest,
			BiConsumer<Device, JsonObject> deviceSetter) throws MessageAdapterException {

		checkDeviceAttrExists(jsonRequest, deviceAttr);

		Device device = new Device();
		try {
			jsonRequest.addProperty(deviceAttr, _mdmRestClient.getEqCodeBySerialNumber(jsonRequest.get(deviceAttr).getAsString()));
		}catch (Exception e) {
			request.setStatus(RequestStatus.ERROR);
			request.setErrorMessage(e.getMessage());
			return;
		}
		if (!_mdmRestClient.isVerticalDevice(jsonRequest.get(deviceAttr).getAsString())) {
			request.setStatus(RequestStatus.ERROR);
			request.setErrorMessage("Device " + jsonRequest.get(deviceAttr).getAsString()
					+ " not found: probably is managed by another MQTT microservice instance");
			return;
		}
		device.setSerialNumber(jsonRequest.get(deviceAttr).getAsString());
		device.setName(jsonRequest.get(deviceAttr).getAsString());
		if (deviceSetter != null)
			deviceSetter.accept(device, jsonRequest);

		request.getDevices().add(this.parseDeviceStatus(jsonRequest, device));

	}

	protected Device parseDeviceStatus(JsonObject jsonDevice, Device device) {
		device.setStatusCode(jsonDevice.has("statusCode") ? jsonDevice.get("statusCode").getAsString() : "OK");
		if (device.getSerialNumber() == null)
			device.setSerialNumber(
					jsonDevice.has("serialNumber") ? jsonDevice.get("serialNumber").getAsString() : null);
		device.setErrorMessage(jsonDevice.has("errorMessage") ? jsonDevice.get("errorMessage").getAsString() : null);

		return device;

	}

	protected void parseDevices(Request request, JsonObject jsonRequest, String devicesAttr,
			DeviceParser deviceParserFn) throws MessageAdapterException {

		if (deviceParserFn == null)
			throw new IllegalArgumentException("deviceParserFn cannot be null");

		checkDeviceAttrExists(jsonRequest, devicesAttr);
		
		JsonArray devices = jsonRequest.get(devicesAttr).getAsJsonArray();
		// use iterator instead forEach to allow use checked exceptions
		for (Iterator<JsonElement> iter = devices.iterator(); iter.hasNext();) {
			JsonElement deviceElement = iter.next();
			String deviceId = getDeviceId(deviceElement);
			if (!deviceId.isEmpty()) {
				deviceElement.getAsJsonObject()
						.addProperty(deviceId,
								_mdmRestClient.getEqCodeBySerialNumber(deviceElement.getAsJsonObject().get(deviceId).getAsString()));
				if (!_mdmRestClient.isVerticalDevice(deviceElement.getAsJsonObject().get(deviceId).getAsString())) {
					request.setStatus(RequestStatus.ERROR);
					request.setErrorMessage("Device " + deviceElement.getAsJsonObject().get(deviceId).getAsString()
							+ " not found: probably is managed by another MQTT microservice instance");
					return;
				}
			} else {
				request.setStatus(RequestStatus.ERROR);
				request.setErrorMessage("Device id not match");
				return;
			}

			request.getDevices()
					.add(this.parseDeviceStatus(deviceElement.getAsJsonObject(), deviceParserFn.parse(deviceElement)));
		}
	}

	private void checkDeviceAttrExists(JsonObject jsonRequest, String devicesAttr) throws MessageAdapterException {
		if (!jsonRequest.has(devicesAttr))
			throw new MessageAdapterException(
					String.format("cannot find attribute '%s' in request %s", devicesAttr, jsonRequest));
	}

	private Request loadBaseRequest(Request result, JsonObject jsonRequest) {

		String uuidRequest = jsonRequest.get("idRequest").getAsString();
		result.setIdRequest(uuidRequest);
		if (jsonRequest.get("time") != null)
			result.setTime(jsonRequest.get("time").getAsLong()*1000); // Transform seconds to milliseconds

		String statusCode = jsonRequest.has(STATUS_CODE_ATTR) ? jsonRequest.get(STATUS_CODE_ATTR).getAsString()
				: null;

		// no status send, try to resolve status
		if (statusCode == null) {
			result.setStatus(jsonRequest.has(ERROR_MESSAGE_ATTR) ? RequestStatus.ERROR : RequestStatus.OK);
			result.setErrorMessage(
					jsonRequest.has(ERROR_MESSAGE_ATTR) ? jsonRequest.get(ERROR_MESSAGE_ATTR).getAsString() : null);
		} else {
			if (statusCode.equalsIgnoreCase("OK")) {
				result.setStatus(RequestStatus.OK);

			} else if (statusCode.equalsIgnoreCase("FAIL")) {
				result.setStatus(RequestStatus.FAIL);
				if (jsonRequest.get("meterReadingList") != null) {
					JsonArray element = gson.fromJson(jsonRequest.get("meterReadingList"), JsonArray.class);
					for (JsonElement jsonElement : element) {
						result.setErrorMessage(jsonElement.getAsJsonObject().get(ERROR_DEVICE_MESSAGE_ATTR).getAsString());
					}
					
				}
				else {
					result.setErrorMessage(jsonRequest.get(ERROR_MESSAGE_ATTR).getAsString());
				}
			} else {
				result.setStatus(RequestStatus.ERROR);
				result.setErrorMessage(jsonRequest.get(ERROR_MESSAGE_ATTR).getAsString());
			}
		}

		return result;

	}

	private String getDeviceId(final JsonElement deviceElement) {
		String deviceId = "";
		if (deviceElement.getAsJsonObject().get("devicename") != null) {
			deviceId = "devicename";
		} else if (deviceElement.getAsJsonObject().get("serialNumber") != null) {
			deviceId = "serialNumber";
		} else if (deviceElement.getAsJsonObject().get("device") != null) {
			deviceId = "device";
		} else if (deviceElement.getAsJsonObject().get("serialnumber") != null) {
			deviceId ="serialnumber";
		}

		return deviceId;
	}

	@FunctionalInterface
	public static interface DeviceParser {
		public Device parse(JsonElement deviceJson) throws MessageAdapterException;
	}
}
