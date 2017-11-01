package com.blito.services;

import com.blito.configs.Constants;
import com.blito.mappers.AdminReportsMapper;
import com.blito.repositories.EventDateRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.rest.viewmodels.blit.AbstractBlitViewModel;
import com.blito.rest.viewmodels.blit.SeatBlitViewModel;
import com.blito.rest.viewmodels.event.AdditionalField;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelService {

	private final Logger logger = LoggerFactory.getLogger(ExcelService.class);
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
						.collect(Collectors.toMap(UserViewModel::getUserId, v -> Arrays.asList(Long.toString(v.getUserId()),
								v.getFirstname(), v.getLastname(), v.getMobile(), v.getEmail()))));
		// NumericsColumns
		model.put("numericcolumns", Collections.singletonList("Id"));

		return model;
	}

	public <V extends AbstractBlitViewModel> Map<String, Object> getBlitsExcelMap(Set<V> blits) {

		Map<String, Object> model = new HashMap<>();
		// Sheet Name
		model.put("sheetname", "Blits");
		// Headers
		model.put("headers",
				Arrays.asList("UserId", "CustomerName", "Mobile", "Email", "BlitId", "Tracking Code", "Blit Type",
						"Created At", "Count", "Total Amount", "Event Name", "Event Date and Time", "Event Address",
						"Seat Type", "Payment Status", "Payment Error", "Saman Bank Token", "Saman Bank Ref Number",
						"Bank Gateway","Discount Code", "Primary Amount", "Seats"));
		// Results
		model.put("results", blits.stream().collect(Collectors.toMap(AbstractBlitViewModel::getBlitId, this::getValues)));
		// NumericsColumns
		model.put("numericcolumns", Arrays.asList("UserId", "BlitId", "Count", "Total Amount","Primary Amount"));
		return model;
	}

	public <V extends AbstractBlitViewModel> Map<String, Object> getBlitsExcelMap(Set<V> blits, Map<String, String> additionalFields) {

		Map<String, Object> model = new HashMap<>();
		// Sheet Name
		model.put("sheetname", "Blits");
		// Headers
		List<String> headers = new ArrayList<>(Arrays.asList("UserId", "CustomerName", "Mobile", "Email",
				"BlitId", "Tracking Code", "Blit Type", "Created At", "Count", "Total Amount", "Event Name",
				"Event Date and Time", "Event Address", "Seat Type", "Payment Status", "Payment Error",
				"Saman Bank Token", "Saman Bank Ref Number", "Bank Gateway","Discount Code", "Primary Amount", "Seats"));
		additionalFields.keySet().stream().sorted().forEachOrdered(headers::add);
		blits.stream().filter(blit ->  blit.getAdditionalFields().size() != additionalFields.size()).forEach(blit ->
			additionalFields.keySet().stream().filter(key -> !blit.getAdditionalFields().stream().map(AdditionalField::getKey).collect(Collectors.toList()).contains(key))
					.forEach(key -> blit.getAdditionalFields().add(new AdditionalField(key,"no value"))));
		model.put("headers", headers);
		// Results
		model.put("results", blits.stream().collect(Collectors.toMap(AbstractBlitViewModel::getBlitId, this::getValues)));
		// NumericsColumns
		List<String> numericColumns = new ArrayList<>(Arrays.asList("UserId", "BlitId", "Count", "Total Amount","Primary Amount"));
		numericColumns.addAll(additionalFields.entrySet().stream()
				.filter(entry -> entry.getValue().equals(Constants.FIELD_INT_TYPE)
						|| entry.getValue().equals(Constants.FIELD_DOUBLE_TYPE))
				.map(Map.Entry::getKey).collect(Collectors.toList()));
		model.put("numericcolumns", numericColumns);
		return model;
	}

	private <V extends AbstractBlitViewModel> List<String> getValues(V v) {
		List<String> values = new ArrayList<>(Arrays.asList(String.valueOf(v.getUserId()), v.getCustomerName(),
				v.getCustomerMobileNumber(), v.getCustomerEmail(), String.valueOf(v.getBlitId()), v.getTrackCode(),
				v.getBlitTypeName(), v.getCreatedAt().toString(), String.valueOf(v.getCount()),
				String.valueOf(v.getTotalAmount()), v.getEventName(), v.getEventDateAndTime(), v.getEventAddress(),
				v.getSeatType() == null ? " " : v.getSeatType().toString(),
				v.getPaymentStatus() == null ? " " : v.getPaymentStatus().toString(), v.getPaymentError(),
				v.getSamanBankToken() == null ? " " : v.getSamanBankToken(),
				v.getRefNum() == null ? " " : v.getRefNum(),
				v.getBankGateway() == null ? " " : v.getBankGateway().toString(),
				v.getDiscountCode() == null ? " " : v.getDiscountCode(),
				v.getPrimaryAmount() == null ? " " : String.valueOf(v.getPrimaryAmount())
				));
		if(v instanceof SeatBlitViewModel) {
			values.add(((SeatBlitViewModel)v).getSeats());
		}
		Optional.ofNullable(v.getAdditionalFields()).filter(additionalFieldList -> !additionalFieldList.isEmpty())
				.ifPresent(additionalFieldList ->
						additionalFieldList.stream().map(AdditionalField::getKey).sorted().forEachOrdered(key ->
							additionalFieldList.stream().filter(additionalField -> additionalField.getKey().equals(key))
							.findFirst().ifPresent(additionalField -> values.add(additionalField.getValue())))
				);
		return values;
	}

	public Map<String, Object> getEventHostsExcelMap(Set<EventHostViewModel> eventHosts) {

		Map<String, Object> model = new HashMap<String, Object>();
		// Sheet Name
		model.put("sheetname", "EventHosts");
		// Headers
		model.put("headers", Arrays.asList("EventHostId", "HostName", "Telephone"));
		// Results
		model.put("results", eventHosts.stream().collect(Collectors.toMap(EventHostViewModel::getEventHostId,
				v -> Arrays.asList(Long.toString(v.getEventHostId()), v.getHostName(), v.getTelephone()))));
		// NumericsColumns
		model.put("numericcolumns", Collections.singletonList("EventHostId"));
		return model;
	}
	
	public <V extends AbstractBlitViewModel> Map<String, Object> blitMapForPdf(V blit){
		
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
		if(blit instanceof SeatBlitViewModel)
			model.put("seats",((SeatBlitViewModel)blit).getSeats());
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
		model.put("seats", "جایگاه:A ردیف ۱، صندلی ۱/جایگاه:A ردیف ۱، صندلی ۱/جایگاه:A ردیف ۱، صندلی ۱/جایگاه:A ردیف ۱، صندلی ۱/جایگاه:A ردیف ۱، صندلی ۱/جایگاه:A ردیف ۱، صندلی ۱/جایگاه:A ردیف ۱، صندلی ۱/جایگاه:A ردیف ۱، صندلی ۱/جایگاه:A ردیف ۱، صندلی ۱/جایگاه:A ردیف ۱، صندلی ۱");
		return model;
	}
	
}
