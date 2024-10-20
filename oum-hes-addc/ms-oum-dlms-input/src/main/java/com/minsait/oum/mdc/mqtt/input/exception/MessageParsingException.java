package com.minsait.oum.mdc.mqtt.input.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class MessageParsingException extends Exception {

    private static final long serialVersionUID = 6201539795511411857L;

    public MessageParsingException(Exception e, String message) {
        super(message);
        log.error("-->> " + message + " : " + e.toString());
    }
}
