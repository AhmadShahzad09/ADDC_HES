package com.minsait.mdc.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MdcUtils {

	public static String getExceptioStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String sStackTrace = sw.toString();
		return sStackTrace;

	}
}
