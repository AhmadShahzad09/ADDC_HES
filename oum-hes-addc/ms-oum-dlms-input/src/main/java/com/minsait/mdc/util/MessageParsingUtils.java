package com.minsait.mdc.util;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.IntervalTypeConverter;
import com.minsait.oum.mdc.domain.MediumType;
import com.minsait.oum.mdc.domain.MediumTypeConverter;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;
import com.minsait.oum.mdc.mqtt.input.exception.MessageParsingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageParsingUtils {

	public static Request parseRequestJson(JsonObject jsonRequest, Request request) throws MessageParsingException {
		/*
		 * TODO Evaluar si tener uan clase utils con este metodo o meterlo en cada
		 * Adapter. Pero nunca completar el parametro request y no hacer un return. La
		 * nomenclatura deberia ser public Request parseRqiuestJson(JsonObject) REVISAR
		 * REFACTOR EN TODOS LOS ADAPTERS
		 */
		log.debug("Parsing json message into Request[id, time, statusCode]");

		if (request == null)
			request = new Request();

		String uuidRequest = jsonRequest.get("idRequest").getAsString();
		request.setIdRequest(uuidRequest);

		try {
			request.setTime(jsonRequest.get("time").getAsLong());
		} catch (Exception e) {
			throw new MessageParsingException(e, "Exception while Parsing Time");
		}

		try {

			RequestStatus status;

			// assume OK if not statusCode attribute is present
			String statusCode = jsonRequest.has("statusCode") ? jsonRequest.get("statusCode").getAsString() : "OK";

			if (statusCode != null && statusCode.equalsIgnoreCase("OK"))
				status = RequestStatus.OK;
			else if (statusCode != null && statusCode.equalsIgnoreCase("FAIL"))
				status = RequestStatus.FAIL;
			else
				status = RequestStatus.ERROR;

			request.setStatus(status);
			request.setErrorMessage(
					jsonRequest.has("errorMessage") ? jsonRequest.get("errorMessage").getAsString() : null);
		} catch (Exception e) {
			throw new MessageParsingException(e, "Exception while Parsing Request Status");
		}

		return request;
	}

	public static Device parseDeviceJson(JsonObject jsonObject) throws MessageParsingException {

		Device device = new Device();

		try {
			log.debug("Parsing json for Devices :" + jsonObject.toString());

			if (jsonObject.get("modelname") != null)
				device.setModel(jsonObject.get("modelname").getAsString());

			if (jsonObject.get("owner") != null)
				device.setOwner(jsonObject.get("owner").getAsString());

			if (jsonObject.get("devicename") != null)
				device.setName(jsonObject.get("devicename").getAsString());

			if (jsonObject.get("serialNumber") != null)
				device.setSerialNumber(jsonObject.get("serialNumber").getAsString());

			if (jsonObject.get("version") != null)
				device.setVersion(jsonObject.get("version").getAsString());

			if (jsonObject.get("ipaddr") != null)
				device.setIp(jsonObject.get("ipaddr").getAsString());

			if (jsonObject.get("deviceErrorMessage") != null
					&& !jsonObject.get("deviceErrorMessage").getAsString().isEmpty()
					&& !jsonObject.get("deviceErrorMessage").getAsString().equals("OK"))
				device.setErrorMessage(jsonObject.get("deviceErrorMessage").getAsString());

			if (jsonObject.get("maintenanceMode") != null)
				device.setInMaintenanceMode(jsonObject.get("maintenanceMode").getAsBoolean());

			if (jsonObject.get("medium") != null && jsonObject.get("medium").toString().length() > 0) {
				MediumType medium = MediumTypeConverter.convert(jsonObject.get("medium").getAsString());
				device.setMedium(medium);
			}

			if (jsonObject.get("interval") != null)
				device.setInterval(IntervalTypeConverter.convert(jsonObject.get("interval").getAsString()));

			if (jsonObject.get("profileObis") != null)
				device.setProfileObis(jsonObject.get("profileObis").getAsString());

			if (jsonObject.get("profileCode") != null)
				device.setProfileCode(jsonObject.get("profileCode").getAsString());

			if (jsonObject.get("thresholdAct") != null)
				device.setThresholdAct(jsonObject.get("thresholdAct").getAsString());

			if (jsonObject.get("threshDuration") != null)
				device.setThreshDuration(jsonObject.get("threshDuration").getAsString());

			// Added for ADDC-532
			if (jsonObject.get("LoadProfile1Interval") != null) {
				device.setLoadProfile1Interval(jsonObject.get("LoadProfile1Interval").getAsString());
			}

			if (jsonObject.get("LoadProfile2Interval") != null) {
				device.setLoadProfile2Interval(jsonObject.get("LoadProfile2Interval").getAsString());
			}

			if (jsonObject.get("PowerQualityProfileInterval") != null) {
				device.setPowerQualityProfileInterval(jsonObject.get("PowerQualityProfileInterval").getAsString());
			}

			if (jsonObject.get("InstrumentationProfile") != null) {
				device.setInstrumentationProfile(jsonObject.get("InstrumentationProfile").getAsString());
			}

			// Added for ADDC-307
			if (jsonObject.get("batteryLevel") != null) {
				device.setBatteryLevel(jsonObject.get("batteryLevel").getAsString());
			}

			if (jsonObject.get("direction_P") != null) {
				device.setDirection_P(jsonObject.get("direction_P").getAsString());
			}

			if (jsonObject.get("direction_Q") != null) {
				device.setDirection_Q(jsonObject.get("direction_Q").getAsString());
			}

			if (jsonObject.get("activeRate") != null) {
				device.setActiveRate(jsonObject.get("activeRate").getAsString());
			}

			if (jsonObject.get("relayStatus") != null) {
				device.setRelayStatus(jsonObject.get("relayStatus").getAsString());
			}

		} catch (Exception e) {
			throw new MessageParsingException(e, "Exception while Parsing Device [meterReadingList] Json");
		}

		return device;
	}
}
