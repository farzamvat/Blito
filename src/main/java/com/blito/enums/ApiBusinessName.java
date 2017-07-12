package com.blito.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ApiBusinessName implements ApiBusinessNameInterface {
	API1, API2, API3;

	public static Set<ApiBusinessNameInterface> getValues() {
		Set<ApiBusinessNameInterface> set = new HashSet<>(Arrays.asList(values()));
		return set;
	}
}
