package com.blito.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.AdminReportsMapper;
import com.blito.models.CommonBlit;
import com.blito.models.EventDate;
import com.blito.models.User;
import com.blito.repositories.EventDateRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.adminreport.BlitBuyerViewModel;

@Service
public class ExcelService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	EventDateRepository eventDateRepository;
	@Autowired
	AdminReportsMapper adminReportsMapper;

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
		model.put("numericcolumns", Arrays.asList("Id"));

		return model;
	}
	
	public Map<String, Object> getBlitBuyersMap(long eventDateId){
		EventDate eventDate = Optional.ofNullable(eventDateRepository.findOne(eventDateId)).map(ed -> ed)
				.orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));
		List<CommonBlit> blits = eventDate.getBlitTypes().stream().flatMap(bt->bt.getCommonBlits().stream()).collect(Collectors.toList());
		List<BlitBuyerViewModel> blitsVm = adminReportsMapper.toList(blits, adminReportsMapper::toBlitBuyerReport);
		Map<String, Object> model = new HashMap<String, Object>();
		//Sheetname
		model.put("sheetname", "Event Blit Buyers By Date");
		//Headers
		model.put("headers", Arrays.asList("UserId","Firstname", "Lastname", "Mobile", "BlitId", "Tracking Code", "Blit Type"));
		//Results
		model.put("results",
				blitsVm.stream()
						.collect(Collectors.toMap(k -> k.getUserId(), v -> Arrays.asList(Long.toString(v.getUserId()),
								v.getFirstname(), v.getLastname(), v.getMobile(), v.getBlitId(), v.getTrackCode(), v.getBlitTypeName()))));
		// NumericsColumns
				model.put("numericcolumns", Arrays.asList("UserId", "BlitId"));
		return model;
	}
}
