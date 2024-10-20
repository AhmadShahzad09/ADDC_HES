package com.minsait.oum.mdc.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minsait.oum.mdc.driver.BusAbstract;
import com.minsait.oum.mdc.driver.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("Minsait_Etisalat_1")
public class EtisalatDriver extends BusAbstract {

	@Autowired
	EtisalatMessageService messageService;

	@Override
	public void handlePublish(Message message) {
		log.info("[Minsait_Etisalat_1]-> enviando: {}", message);
		messageService.send(message);
		log.info("[Minsait_Etisalat_1]-> enviado: {}", message);
	}
}
