package com.blito.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum ApiBusinessName implements ApiBusinessNameInterface {
	TEST;

	@Override
	public Set<ApiBusinessNameInterface> getValues() {
			return new HashSet<>(Arrays.asList(values()));
	}
}
