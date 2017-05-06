package com.blito.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.exceptions.NotFoundException;
import com.blito.models.User;
import com.blito.repositories.UserRepository;

@Service
public class ExcelService {

	@Autowired
	UserRepository userRepository;

	public Map<String, Object> getUserExcelMap() {
		List<User> allUsers = Optional.ofNullable(userRepository.findAll()).map(au -> au)
				.orElseThrow(() -> new NotFoundException("khali"));
		Map<String, Object> model = new HashMap<String, Object>();
		// Sheetname
		model.put("sheetname", "All Users");
		// Headers
		model.put("headers", Arrays.asList("Id","Firstname","Lastname","Mobile","Email"));
		// Results
		model.put("results",
				allUsers.stream()
						.collect(Collectors.toMap(k -> k.getUserId(), v -> Arrays.asList(Long.toString(v.getUserId()),
								v.getFirstname(), v.getLastname(), v.getMobile(), v.getEmail()))));
		// NumericsColumns
		List<String> numericColumns = new ArrayList<String>();
		numericColumns.add("Id");
		model.put("numericcolumns", numericColumns);

		return model;
	}
}
