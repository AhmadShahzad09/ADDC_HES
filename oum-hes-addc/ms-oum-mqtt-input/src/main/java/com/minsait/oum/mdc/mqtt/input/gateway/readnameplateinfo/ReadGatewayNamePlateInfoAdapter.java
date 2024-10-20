package com.minsait.oum.mdc.mqtt.input.gateway.readnameplateinfo;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.GatewayInfo;
import com.minsait.oum.mdc.domain.GatewayInfo.NetworkInformation;
import com.minsait.oum.mdc.domain.GatewayInfo.SimCard;
import com.minsait.oum.mdc.domain.Request;
import com.minsait.oum.mdc.domain.RequestType;
import com.minsait.oum.mdc.domain.ResponseType;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.MessageAdapterException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReadGatewayNamePlateInfoAdapter extends AbstractMessageAdapter {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.GATEWAY_NAME_PLATE_INFORMATION;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.SCHEDULED;
    }

    @Override
    public Request innerConvert(Request request, JsonObject jsonRequest) throws MessageAdapterException {

        Device device = new Device();
        GatewayInfo gatewayInfo = new GatewayInfo();
        NetworkInformation networkInfo = gatewayInfo.new NetworkInformation();
        SimCard simCard = gatewayInfo.new SimCard();

        JsonObject jsonObject = jsonRequest.getAsJsonObject("gatewayinfo");
        gatewayInfo.setSerialNumber(jsonObject.get("serialnumber").getAsString());
        gatewayInfo.setMac_address(jsonObject.get("mac_address").getAsString());
        gatewayInfo.setModel(jsonObject.get("model").getAsString());
        gatewayInfo.setFirmwareVersion(jsonObject.get("firmwareVersion").getAsString());

        // Adding SimCard Info
        jsonObject = jsonRequest.getAsJsonObject("gatewayinfo").getAsJsonObject("simCard");
        simCard.setIccId(jsonObject.get("iccid").getAsString());
        simCard.setMsisdn(jsonObject.get("msisdn").getAsString());

        jsonObject = jsonRequest.getAsJsonObject("gatewayinfo").getAsJsonObject("networkInformation");
        networkInfo.setBand(jsonObject.get("band").getAsString());
        networkInfo.setRsrp(jsonObject.get("rsrp").getAsString());
        networkInfo.setRsrq(jsonObject.get("rsrq").getAsString());
        networkInfo.setNetworkIP(jsonObject.get("networkIP").getAsString());
        networkInfo.setRssi(jsonObject.get("rssi").getAsString());
        networkInfo.setSinr(jsonObject.get("sinr").getAsString());
        networkInfo.setTypeCommunication(jsonObject.get("typeCommunication").getAsString());

        gatewayInfo.setSimCard(simCard);
        gatewayInfo.setNetworkInformation(networkInfo);

        device.setGatewayInfo(gatewayInfo);
        device.setSerialNumber(jsonRequest.get("serialnumber").getAsString());

        request.getDevices().add(device);

        return request;
    }
}
