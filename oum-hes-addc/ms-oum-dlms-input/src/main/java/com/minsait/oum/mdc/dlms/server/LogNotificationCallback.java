package com.minsait.oum.mdc.dlms.server;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.indracompany.energy.dlms.cosem.addc.server.INotificationCallback;

import gurux.common.GXCommon;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope(value = "prototype")
public class LogNotificationCallback implements INotificationCallback {
	
	@Override
	public void onData(String senderInfo, List<Object> values) {
		System.out.println("SenderInfo: " + senderInfo);
		
		int index = 0;
		for (Object value : values) {
			if (value instanceof byte[]) {
				System.out.println(index++ + ": " + GXCommon.bytesToHex((byte[]) value));
			} else if (value instanceof Number) {
				System.out.println(index++ + ": " + value);
			} else if (value instanceof String) {
				System.out.println(index++ + ": " + value);
			} else {
				System.out.println(index++ + ": " + ReflectionToStringBuilder.toString(value));
			}
		}
	}

}