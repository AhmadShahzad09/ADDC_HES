package com.minsait.oum.mdc.domain;

public class MediumTypeConverter {

	public static MediumType convert(String medium) {

		if (medium == null || medium.isEmpty())
			throw new IllegalArgumentException("The medium parameter can not be null or empty");

		medium = medium.replace(" ", "");
		switch (medium.toUpperCase()) {

		case "HOTWATER":
			return (MediumType.WATER);
		case "WATER":
			return (MediumType.WATER);
		case "GAS":
			return (MediumType.GAS);
		case "ELECTRICITY":
			return (MediumType.ELECTRICITY);

		default:
			throw new IllegalArgumentException("The medium parameter [" + medium + "] can not be converted");
		}
	}

	public static String convertToString(MediumType medium) {

		switch (medium) {

		case WATER:
			return ("WATER");
		case GAS:
			return ("GAS");
		case ELECTRICITY:
			return ("ELECTRICITY");

		default:
			throw new IllegalArgumentException("The medium parameter [" + medium + "] can not be converted to string");
		}
	}

}
