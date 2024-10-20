package com.minsait.oum.mdc.mqtt.input.exception;

public enum ErrorMessage {

    JSON_REQUEST_TIME_INVALID("Time is not valid in the Json request"),
    JSON_REQUEST_MEASURES_INVALID("Interval Block is not valid in the Json request")
    ;

    private String msg;

    ErrorMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
