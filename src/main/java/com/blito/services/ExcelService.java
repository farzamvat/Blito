package com.blito.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.configs.Constants;
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
		// Sheet Name
		model.put("sheetname", "Users");
		// Headers
		model.put("headers", Arrays.asList("Id", "Firstname", "Lastname", "Mobile", "Email"));
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
		// Sheet Name
		model.put("sheetname", "Blits");
		// Headers
		model.put("headers",
				Arrays.asList("UserId", "CustomerName", "Mobile", "Email", "BlitId", "Tracking Code", "Blit Type",
						"Created At", "Count", "Total Amount", "Event Name", "Event Date and Time", "Event Address",
						"Seat Type", "Payment Status", "Payment Error", "Saman Bank Token", "Saman Bank Ref Number",
						"Bank Gateway"));
		// Results
		model.put("results", blits.stream().collect(Collectors.toMap(k -> k.getBlitId(),
				v -> Arrays.asList(String.valueOf(v.getUserId()), v.getCustomerName(), v.getCustomerMobileNumber(),
						v.getCustomerEmail(), String.valueOf(v.getBlitId()), v.getTrackCode(), v.getBlitTypeName(),
						v.getCreatedAt().toString(), String.valueOf(v.getCount()), String.valueOf(v.getTotalAmount()),
						v.getEventName(), v.getEventDateAndTime(), v.getEventAddress(),
						v.getSeatType() == null ? " " : v.getSeatType().toString(),
						v.getPaymentStatus() == null ? " " : v.getPaymentStatus().toString(), v.getPaymentError(),
						v.getSamanBankToken(), v.getRefNum(),
						v.getBankGateway() == null ? " " : v.getBankGateway().toString()))));
		// NumericsColumns
		model.put("numericcolumns", Arrays.asList("UserId", "BlitId", "Count", "Total Amount"));
		return model;
	}

	public Map<String, Object> getBlitsExcelMap(Set<CommonBlitViewModel> blits, Map<String, String> additionalFields) {

		Map<String, Object> model = new HashMap<String, Object>();
		// Sheet Name
		model.put("sheetname", "Blits");
		// Headers
		List<String> headers = new ArrayList<String>(Arrays.asList("UserId", "CustomerName", "Mobile", "Email",
				"BlitId", "Tracking Code", "Blit Type", "Created At", "Count", "Total Amount", "Event Name",
				"Event Date and Time", "Event Address", "Seat Type", "Payment Status", "Payment Error",
				"Saman Bank Token", "Saman Bank Ref Number", "Bank Gateway"));
		headers.addAll(additionalFields.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList()));
		model.put("headers", headers);
		// Results
		Map<Object, Object> results = blits.stream().collect(Collectors.toMap(k -> k.getBlitId(), v -> getValues(v)));
		model.put("results", results);
		// NumericsColumns
		List<String> numericColumns = new ArrayList<String>(Arrays.asList("UserId", "BlitId", "Count", "Total Amount"));
		numericColumns.addAll(additionalFields.entrySet().stream()
				.filter(entry -> entry.getValue().equals(Constants.FIELD_INT_TYPE)
						|| entry.getValue().equals(Constants.FIELD_DOUBLE_TYPE))
				.map(entry -> entry.getKey()).collect(Collectors.toList()));
		model.put("numericcolumns", numericColumns);
		return model;
	}

	public List<Object> getValues(CommonBlitViewModel v) {
		List<Object> values = new ArrayList<Object>(Arrays.asList(String.valueOf(v.getUserId()), v.getCustomerName(),
				v.getCustomerMobileNumber(), v.getCustomerEmail(), String.valueOf(v.getBlitId()), v.getTrackCode(),
				v.getBlitTypeName(), v.getCreatedAt().toString(), String.valueOf(v.getCount()),
				String.valueOf(v.getTotalAmount()), v.getEventName(), v.getEventDateAndTime(), v.getEventAddress(),
				v.getSeatType() == null ? " " : v.getSeatType().toString(),
				v.getPaymentStatus() == null ? " " : v.getPaymentStatus().toString(), v.getPaymentError(),
				v.getSamanBankToken(), v.getRefNum(),
				v.getBankGateway() == null ? " " : v.getBankGateway().toString()));
		values.addAll(v.getAdditionalFields().entrySet().stream().map(entry -> entry.getValue())
				.collect(Collectors.toList()));
		return values;
	}

	public Map<String, Object> getEventHostsExcelMap(Set<EventHostViewModel> eventHosts) {

		Map<String, Object> model = new HashMap<String, Object>();
		// Sheet Name
		model.put("sheetname", "EventHosts");
		// Headers
		model.put("headers", Arrays.asList("EventHostId", "HostName", "Telephone"));
		// Results
		model.put("results", eventHosts.stream().collect(Collectors.toMap(k -> k.getEventHostId(),
				v -> Arrays.asList(Long.toString(v.getEventHostId()), v.getHostName(), v.getTelephone()))));
		// NumericsColumns
		model.put("numericcolumns", Arrays.asList("EventHostId"));
		return model;
	}
	
	public Map<String, Object> blitMapForPdf(CommonBlitViewModel blit){
		
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("event name", blit.getEventName());
		model.put("customer name", blit.getCustomerName() );
		model.put("customer mobile",blit.getCustomerMobileNumber());
		model.put("customer email", blit.getCustomerEmail());
		model.put("event date", blit.getEventDateAndTime());
		model.put("track code", blit.getTrackCode());
		model.put("blit type", blit.getBlitTypeName());
		model.put("count", blit.getCount());
		model.put("event address", blit.getEventAddress());
		model.put("event photo", blit.getEventPhotoId());
		return model;
	}
	
	public Map<String, Object> testPdfData(){
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("event name", "کنسرت نیلز");
		model.put("customer name", "هستی سحابی");
		model.put("customer mobile", "۰۹۱۲۷۹۷۶۸۳۷");
		model.put("customer email", "hasti.sahabi@gmail.com");
		model.put("event date", "شنبه ۱۱ اردیبهشت، ساعت ۲۱:۰۰");
		model.put("track code", "92322178");
		model.put("blit type", "VIP");
		model.put("count", 5);
		model.put("event address", "میدان الف، خیابان چهارم، موسسه‌ی ققنوس");
		model.put("event photo", "17e9678e-168a-4dd1-840f-11111d900b94");
		return model;
	}
	
}
