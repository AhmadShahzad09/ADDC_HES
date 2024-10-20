package com.minsait.oum.mdc.coap.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minsait.mdc.data.model.Call;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RequestProcessApplication {

	@Autowired
	private OnDemandCommand onDemandCommand;

	public void process(Call call) throws Exception {

		if (call == null) {
			log.info("RequestProcessApplication >> Warning: Call can not be null!");
			return;
		}

		onDemandCommand.handleOnDemandRequest(call);

	}

}
