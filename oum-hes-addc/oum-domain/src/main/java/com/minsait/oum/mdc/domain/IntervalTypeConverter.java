package com.minsait.oum.mdc.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class IntervalTypeConverter {

	public static IntervalType convert(String interval) {

		if (interval == null || interval.isEmpty())
			throw new IllegalArgumentException("The interval parameter can not be null or empty");

		switch (interval.toUpperCase()) {
		case "0":
		case "MIN01":
		case "1":
			return (IntervalType.MIN01);
		case "MIN10":
		case "10":
			return (IntervalType.MIN10);
		case "MIN15":
		case "15":
			return (IntervalType.MIN15);
		case "MIN30":
		case "30":
			return (IntervalType.MIN30);
		case "HOURLY":
		case "60":
			return (IntervalType.H);
		case "D":
		case "1440":
			return (IntervalType.D);

		default:
			throw new IllegalArgumentException("The interval parameter [" + interval + "] can not be converted");
		}
	}

	public static String convertToString(IntervalType interval) {

		switch (interval) {

		case MIN01:
			return ("MIN01");
		case MIN10:
			return ("MIN10");
		case MIN15:
			return ("MIN15");
		case MIN30:
			return ("MIN30");
		case H:
			return ("H");
		case D:
			return "D";

		default:
			throw new IllegalArgumentException(
					"The interval parameter [" + interval + "] can not be converted to string");
		}
	}

	public static int convertToMinutes(IntervalType interval) {

		switch (interval) {

		case MIN01:
			return (1);
		case MIN10:
			return (10);
		case MIN15:
			return (15);
		case MIN30:
			return (30);
		case H:
			return (60);
		case D:
			return 1440;

		default:
			throw new IllegalArgumentException(
					"The interval parameter [" + interval + "] can not be converted to minutes");
		}
	}

	public static LocalDateTime getLocalDateTime(long time, IntervalType intervalType, int period) throws Exception {

		LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), TimeZone.getDefault().toZoneId());

		LocalDateTime day = LocalDateTime.of(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), 0, 0);

		LocalDateTime result = day.plusMinutes(getMinutesFromIntervalType(intervalType) * period);

		return (result);
	}

	public static int getMinutesFromIntervalType(IntervalType intervalType) throws Exception {

		switch (intervalType) {

		case MIN01:
			return (1);
		case MIN10:
			return (10);
		case MIN15:
			return (15);
		case MIN30:
			return (30);
		case H:
			return (60);

		default:
			throw new Exception("IntervalTypeConverter - The intervalType [" + intervalType + "] is not implemented");
		}
	}

	public static int getDailyPeriods(IntervalType intervalType) throws Exception {

		switch (intervalType) {

		case MIN01:
			return (60 * 24);
		case MIN10:
			return (6 * 24);
		case MIN15:
			return (4 * 24);
		case MIN30:
			return (2 * 24);
		case H:
			return (24);

		default:
			throw new Exception("IntervalTypeConverter - The intervalType [" + intervalType + "] is not implemented");
		}
	}
}
