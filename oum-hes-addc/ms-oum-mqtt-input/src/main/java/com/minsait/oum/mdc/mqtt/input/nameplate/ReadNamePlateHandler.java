package com.minsait.oum.mdc.mqtt.input.nameplate;

import com.minsait.mdc.util.domain.TelemetryRemoteConfigurationVO;
import com.minsait.oum.mdc.mqtt.input.AbstractMessageHandlerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

@Slf4j
public class ReadNamePlateHandler extends AbstractMessageHandlerProcessor {

    @Autowired
    private ReadNamePlateAdapter adapter;

    @Override
    public void handleMessage(Message<?> message) {

        this.processTelemetryCallToTaskListener(adapter.convert(message.getPayload().toString()),
                (request, call, device) -> {
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "device-id", device.getDeviceID()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "manufacturer-name", device.getManufacturerName()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "ct-numerator", device.getCtNumerator()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "ct-denumerator", device.getCtDenominator()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "vt-numerator", device.getVtNumerator()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "vt-denumerator", device.getVtDenominator()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "ct-ratio", device.getCtRatio()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "vt-transfer-ratio", device.getVtTransferRatio()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "current-rating", device.getCurrentRating()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "year-of-manufacture", device.getYearOfManufacture()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "firmware-version", device.getFirmwareVersion()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "address", device.getAddress()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "total-serial-number", device.getTotaliserSerialNumber()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "tranducer-serial-number", device.getTranducerSerialNumber()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "manufacturer-code", device.getManufacturerCode()));
            this.getMdmRestClient().setTelemetryRemoteConfig(TelemetryRemoteConfigurationVO.from(device, "type", device.getType()));

                });


    }
}
