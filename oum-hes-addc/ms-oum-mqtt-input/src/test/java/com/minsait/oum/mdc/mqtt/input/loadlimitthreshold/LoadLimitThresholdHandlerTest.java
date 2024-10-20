package com.minsait.oum.mdc.mqtt.input.loadlimitthreshold;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({com.minsait.oum.mdc.mqtt.input.loadlimitthreshold.LoadLimitThresholdHandler.class})
public class LoadLimitThresholdHandlerTest {



// COAP
// {"idRequest":"e0252d26-d73e-4dcd-a1eb-1f99789c445f","time":1657809150,"devices":["I11D18D000471"],"gWsn":"109017135","gWip":""}
//
// MQTT
//    { "idRequest": "e0252d26-d73e-4dcd-a1eb-1f99789c445f",
// "time": 1657809206,
// "devices": [
// {
// "serialNumber": "I11D18D000471",
// "thresholdAct": "30",
// "threshDuration": "300"
// }
//]}




}
