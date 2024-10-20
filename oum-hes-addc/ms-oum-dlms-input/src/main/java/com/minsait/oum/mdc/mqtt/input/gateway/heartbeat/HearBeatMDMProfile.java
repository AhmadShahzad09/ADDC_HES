package com.minsait.oum.mdc.mqtt.input.gateway.heartbeat;

import java.util.List;

import com.minsait.common.enu.ProfileCommon;

import lombok.Data;

@Data
public class HearBeatMDMProfile extends ProfileCommon{

	List<HeartBeatMDM> meterCollector;
}
