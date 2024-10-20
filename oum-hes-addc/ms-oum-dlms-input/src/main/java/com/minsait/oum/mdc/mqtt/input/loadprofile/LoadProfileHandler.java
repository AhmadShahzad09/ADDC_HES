package com.minsait.oum.mdc.mqtt.input.loadprofile;

import com.minsait.mdc.util.MeasurementUnitConverterUtils;
import com.minsait.oum.mdc.domain.Device;
import com.minsait.oum.mdc.domain.Measure;
import com.minsait.oum.mdc.mqtt.input.AbstractPlatformMessageHandlerProcessor;
import com.minsait.oum.mdc.platform.PlatformMeter;
import com.minsait.oum.mdc.platform.loadprofile.LoadProfileMeter;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadProfileHandler extends AbstractPlatformMessageHandlerProcessor {

    @Override
    protected List<PlatformMeter> buildMeterCollector(final Device device) {
        List<PlatformMeter> meterCollector = new ArrayList<>();
        for (Measure measure : device.getMeasures()) {
            for (Measure.Block block : measure.getBlocks()) {
                meterCollector.add(buildPlatformMeter(device, measure.getProfileStatus(), block));
            }
        }

        return meterCollector;
    }

    private PlatformMeter buildPlatformMeter(final Device device, final String quality, final Measure.Block block) {
        LoadProfileMeter loadProfileMeter = (LoadProfileMeter) buildPlatformMeter(quality,block);
        loadProfileMeter.setMeasurementIntervalCode(device.getInterval().name());

        return loadProfileMeter;
    }

    @Override
    protected PlatformMeter buildPlatformMeter(final String quality, final Measure.Block block) {
        Pair<Double, String> unitConverted = MeasurementUnitConverterUtils.convert(block.getValue(),block.getMagnitude());
        return LoadProfileMeter.builder()
                .quality(quality)
                .day(block.getTimestamp())
                .value(unitConverted.getFirst())
                .magnitudeCode(block.getChannel())
                .unitM(unitConverted.getSecond())
                .obisCode(block.getCode())
                .build();
    }
}
