package com.minsait.oum.mdc.driver;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class BusAbstract {
	
	private Message message;
	
	private Long taskId;
	
	public void publish() {
		log.info("Publishing task to bus...{}", taskId);
		
		handlePublish(this.message);
		
		log.info("Task published...{}", taskId);
	}
	
	public abstract void handlePublish(Message message);

}
