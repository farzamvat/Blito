package com.blito.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum AdminApiBusinessName implements ApiBusinessNameInterface{
	TEST2;

	@Override
	public Set<ApiBusinessNameInterface> getValues() {
		Set<ApiBusinessNameInterface> set = new HashSet<>(Arrays.asList(values()));
		Set<ApiBusinessNameInterface> set2 = new HashSet<>(Arrays.asList(ApiBusinessName.values()));
		set.addAll(set2);
		return set;
	}
}
