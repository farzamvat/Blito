package com.blito.mappers;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.configs.Constants;
import com.blito.exceptions.FileNotFoundException;
import com.blito.models.Salon;
import com.blito.rest.viewmodels.salon.SalonViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class SalonMapper implements GenericMapper<Salon,SalonViewModel> {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public Salon createFromViewModel(SalonViewModel viewModel) {
        return null;
    }

    @Override
    public SalonViewModel createFromEntity(Salon entity) {
        SalonViewModel salonViewModel = new SalonViewModel();
        salonViewModel.setAddress(entity.getAddress());
        salonViewModel.setLatitude(entity.getLatitude());
        salonViewModel.setLongitude(entity.getLongitude());
        salonViewModel.setName(entity.getName());
        salonViewModel.setSalonUid(entity.getSalonUid());
        return salonViewModel;
    }

    public SalonViewModel createFromEntityWithSchemaAndSvg(Salon entity) {
        SalonViewModel salonViewModel = createFromEntity(entity);
        salonViewModel.setSchema(Try.of(() -> new File(SalonMapper.class.getResource(Constants.BASE_SALON_SCHEMAS + entity.getPlanPath()).toURI()))
                .flatMap(file -> Try.of(() -> objectMapper.readValue(file,com.blito.common.Salon.class)))
                .getOrElseThrow(throwable -> new FileNotFoundException("schema file not found")));
        salonViewModel.setSalonSvg(entity.getSalonSvg());
        return salonViewModel;
    }

    @Override
    public Salon updateEntity(SalonViewModel viewModel, Salon entity) {
        return null;
    }
}
