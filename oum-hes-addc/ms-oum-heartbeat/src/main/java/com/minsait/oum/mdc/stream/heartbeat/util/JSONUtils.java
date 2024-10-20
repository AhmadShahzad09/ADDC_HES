package com.minsait.oum.mdc.stream.heartbeat.util;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;

public class JSONUtils {

	private static final TypeAdapter<JsonElement> strictAdapter = new Gson().getAdapter(JsonElement.class);

	private JSONUtils() {
		// hidden constructor
	}

	public static boolean isValid(String json) {
		try {
			strictAdapter.fromJson(json);
		} catch (JsonSyntaxException | IOException e) {
			return false;
		}
		return true;
	}
}
