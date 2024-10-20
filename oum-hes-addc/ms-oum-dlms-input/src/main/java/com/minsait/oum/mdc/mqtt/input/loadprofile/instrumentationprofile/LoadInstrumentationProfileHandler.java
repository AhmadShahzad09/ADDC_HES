package com.minsait.oum.mdc.mqtt.input.loadprofile.instrumentationprofile;

import com.minsait.oum.mdc.mqtt.input.AbstractMessageAdapter;
import com.minsait.oum.mdc.mqtt.input.loadprofile.LoadProfileAdapter;
import com.minsait.oum.mdc.mqtt.input.loadprofile.LoadProfileHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class LoadInstrumentationProfileHandler extends LoadProfileHandler {

    @Autowired
    private LoadProfileAdapter adapter;

    @Override
    protected AbstractMessageAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected String getActionServiceName() {
        return "InstValuesActionWS";
    }

}
