package com.minsait.oum.mdc.coap.commands;

import com.minsait.mdc.data.model.Call;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class OnDemmandCommandTest {

	@Autowired
	private OnDemandCommand _onDemmandCommand;

	@Test
	public void execute_call() throws Exception {

		_onDemmandCommand.handleOnDemandRequest(this.createFixtureCall());
	}

	private Call createFixtureCall() {
		Call result = new Call();
		return result;

	}
}
