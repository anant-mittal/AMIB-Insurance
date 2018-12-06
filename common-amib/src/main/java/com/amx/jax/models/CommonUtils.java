package com.amx.jax.models;

public class CommonUtils {

	public static boolean isNumeric(String strNum) {
		try {
			Integer d = Integer.parseInt(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

}
