package com.minsait.oum.mdc.mqtt.input.gateway.readintervalperiod;

import com.google.gson.JsonObject;
import com.minsait.mdc.util.MdmRestClient;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestStatus;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReadIntervalPeriodAdapter extends AbstractMessageAdapter {
	
	@Autowired
	private MdmRestClient _mdmRestClient;
	
    @Override
    public ResponseType getResponseType() {
        return ResponseType.GATEWAY_READ_INTERVAL_PERIOD;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {
    	 Device device = new Device();
         try {
 			String eqCode = _mdmRestClient.getEqCodeBySerialNumber(jsonRequest.get("serialnumber").getAsString());
 			jsonRequest.addProperty("serialnumber", eqCode);
 		} catch (Exception e) {
 			e.getMessage();
 			jsonRequest.addProperty("status", RequestStatus.ERROR.name());
 			jsonRequest.addProperty("reason", "serialNumber not found");
 		}
        if (!_mdmRestClient.isVerticalDevice(jsonRequest.get("serialnumber").getAsString())) {
            request.setStatus(RequestStatus.ERROR);
            request.setErrorMessage("Device " + jsonRequest.get("serialnumber").getAsString()
                    + " not found: probably is managed by another MQTT microservice instance");
            throw new MessageAdapterException("Device " + jsonRequest.get("serialnumber").getAsString()
                    + " not found: probably is managed by another MQTT microservice instance");
        }
         
         request.setSerialnumber(jsonRequest.get("serialnumber").getAsString());
         device.setSerialNumber(jsonRequest.get("serialnumber").getAsString());
        
        if (jsonRequest.get("loadprofile1_Interval") != null)
        	device.setLoadProfile1Interval(jsonRequest.get("loadprofile1_Interval").getAsString());
        if (jsonRequest.get("loadprofile2_Interval") != null)
        	device.setLoadProfile2Interval(jsonRequest.get("loadprofile2_Interval").getAsString());
        if (jsonRequest.get("PowerQualityProfile_Interval") != null)
            device.setPowerQualityProfileInterval(jsonRequest.get("PowerQualityProfile_Interval").getAsString());
        if (jsonRequest.get("InstrumentationProfile_Interval") != null)
            device.setInstrumentationProfileInterval(jsonRequest.get("InstrumentationProfile_Interval").getAsString());
        if (jsonRequest.get("MaxdemandProfile_Interval") != null)
        	device.setMaxDemandProfileInterval(jsonRequest.get("MaxdemandProfile_Interval").getAsString());
        if (jsonRequest.get("Waterprofile_Interval") != null)
        	device.setWaterProfileInterval(jsonRequest.get("Waterprofile_Interval").getAsString());
        if (jsonRequest.get("EnergyProfileGroup1_Interval") != null)
        	device.setEnergyProfileGroup1Interval(jsonRequest.get("EnergyProfileGroup1_Interval").getAsString());
        if (jsonRequest.get("EnergyProfileGroup2_Interval") != null)
        	device.setEnergyProfileGroup2Interval(jsonRequest.get("EnergyProfileGroup2_Interval").getAsString());
        if (jsonRequest.get("EnergyProfileGroup3_Interval") != null)
        	device.setEnergyProfileGroup3Interval(jsonRequest.get("EnergyProfileGroup3_Interval").getAsString());
        if (jsonRequest.get("InstantaneousGroup1_Interval") != null)
        	device.setInstantaneousGroup1Interval(jsonRequest.get("InstantaneousGroup1_Interval").getAsString());
        if (jsonRequest.get("InstantaneousGroup2_Interval") != null)
        	device.setInstantaneousGroup2Interval(jsonRequest.get("InstantaneousGroup2_Interval").getAsString());
        if (jsonRequest.get("InstantaneousGroup3_Interval") != null)
        	device.setInstantaneousGroup3Interval(jsonRequest.get("InstantaneousGroup3_Interval").getAsString());
        if (jsonRequest.get("InstantaneousGroup4_Interval") != null)
        	device.setInstantaneousGroup4Interval(jsonRequest.get("InstantaneousGroup4_Interval").getAsString());
        if (jsonRequest.get("heartbeat_Interval") != null)
        	device.setHeartBeatInterval(jsonRequest.get("heartbeat_Interval").getAsString());
        if (jsonRequest.get("gwEvents_Interval") != null)
        	device.setGwEventsInterval(jsonRequest.get("gwEvents_Interval").getAsString());
        if (jsonRequest.get("Events_Interval") != null)
        	device.setEventsInterval(jsonRequest.get("Events_Interval").getAsString());
        if (jsonRequest.get("eMonthlyBillingProfile") != null)
        	device.setEMonthlyBillingProfile(jsonRequest.get("eMonthlyBillingProfile").getAsString());
        if (jsonRequest.get("eDailyBillingProfile") != null)
        	device.setEDailyBillingProfile(jsonRequest.get("eDailyBillingProfile").getAsString());
        if (jsonRequest.get("wMonthlyBillingProfile") != null)
        	device.setWMonthlyBillingProfile(jsonRequest.get("wMonthlyBillingProfile").getAsString());

        request.getDevices().add(device);
        return request;
    }
}
