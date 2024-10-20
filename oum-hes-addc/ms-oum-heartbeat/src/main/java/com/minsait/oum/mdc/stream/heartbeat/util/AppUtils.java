package com.minsait.oum.mdc.stream.heartbeat.util;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import com.google.gson.JsonElement;

public class AppUtils {

	// Parses String to relevant Long value and Sets 0l for invalid string as
	// default.
	public static Long parseLongWithZeroAsDefault(String value) {
		Long period = 0l;
		if (value != null && !value.isEmpty()) {
			try {
				period = Long.parseLong(value);
			} catch (NumberFormatException nfe) {
				period = 0l;
			}
		}
		return period;
	}

	public static Long getTimeInSeconds(long timeInMillis) {
		Instant instant = Instant.ofEpochMilli(timeInMillis);
		return instant.getEpochSecond();
	}

	public static Long getDateFromString(String date) {
		try {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			return AppUtils.getTimeInSeconds(date1.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}

	public static String getStringValue(JsonElement element) {
		String value = "";
		if (element != null) {
			value = element.getAsString();
		}
		return value;
	}
}
