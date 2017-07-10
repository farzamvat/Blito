package com.blito.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.mappers.AdminReportsMapper;
import com.blito.repositories.EventDateRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.rest.viewmodels.blit.CommonBlitViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;

@Service
public class ExcelService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	EventDateRepository eventDateRepository;
	@Autowired
	AdminReportsMapper adminReportsMapper;

	public Map<String, Object> getUserExcelMap(Set<UserViewModel> allUsers) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		// Sheetname
		model.put("sheetname", "Users");
		// Headers
		model.put("headers", Arrays.asList("Id","Firstname","Lastname","Mobile","Email"));
		// Results
		model.put("results",
				allUsers.stream()
						.collect(Collectors.toMap(k -> k.getUserId(), v -> Arrays.asList(Long.toString(v.getUserId()),
								v.getFirstname(), v.getLastname(), v.getMobile(), v.getEmail()))));
		// NumericsColumns
		model.put("numericcolumns", Arrays.asList("Id"));

		return model;
	}
	
	public Map<String, Object> getBlitsExcelMap(Set<CommonBlitViewModel> blits) {

		Map<String, Object> model = new HashMap<String, Object>();
		// Sheetname
		model.put("sheetname", "Blits");
		// Headers
		model.put("headers",
				Arrays.asList("UserId", "CustomerName", "Mobile", "Email", "BlitId", "Tracking Code", "Blit Type",
						"Created At", "Count", "Total Amount", "Event Name", "Event Date and Time", "Event Address",
						"Seat Type", "Payment Status", "Payment Error", "Saman Bank Token", "Saman Bank Ref Number",
						"Bank Gateway"));
		// Results
		model.put("results", blits.stream()
				.collect(Collectors.toMap(k -> k.getBlitId(), v -> Arrays.asList(String.valueOf(v.getUserId()),
						v.getCustomerName(), v.getCustomerMobileNumber(), v.getCustomerEmail() , String.valueOf(v.getBlitId()), v.getTrackCode(),
						v.getBlitTypeName(), v.getCreatedAt().toString(), String.valueOf(v.getCount()), String.valueOf(v.getTotalAmount()), v.getEventName(),
						v.getEventDateAndTime(), v.getEventAddress(), v.getSeatType() == null ? " " : v.getSeatType().toString(), v.getPaymentStatus() == null ? " " : v.getPaymentStatus().toString(),
						v.getPaymentError(), v.getSamanBankToken(), v.getSamanBankRefNumber(), v.getBankGateway() == null ? " " :v.getBankGateway().toString()))));
		// NumericsColumns
		model.put("numericcolumns", Arrays.asList("UserId", "BlitId","Count", "Total Amount"));
		return model; 
	}

	
public Map<String, Object> getEventHostsExcelMap(Set<EventHostViewModel> eventHosts){
		
		Map<String, Object> model = new HashMap<String, Object>();
		//Sheetname
		model.put("sheetname", "EventHosts");
		//Headers
		model.put("headers", Arrays.asList("EventHostId","HostName", "Telephone"));
		//Results
		model.put("results",
				eventHosts.stream()
						.collect(Collectors.toMap(k -> k.getEventHostId(), v -> Arrays.asList(Long.toString(v.getEventHostId()),
								v.getHostName(), v.getTelephone()))));
		// NumericsColumns
				model.put("numericcolumns", Arrays.asList("EventHostId"));
		return model;
	}
}
