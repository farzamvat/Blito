package com.blito.services;

import com.blito.enums.Response;
import com.blito.mappers.SalonMapper;
import com.blito.repositories.SalonRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.rest.viewmodels.salon.SalonViewModel;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
    @author Farzam Vatanzadeh
*/
@Service
public class SalonService {
    @Autowired
    private SalonRepository salonRepository;
    @Autowired
    private SalonMapper salonMapper;

    @Transactional
    public Page<SalonViewModel> getSalons(Pageable pageable) {
        return salonMapper.toPage(salonRepository.findAll(pageable));
    }

    @Transactional
    public Either<ExceptionViewModel,SalonViewModel> getSalonByUid(String salonUid) {
        return Option.ofOptional(salonRepository.findBySalonUid(salonUid).map(salonMapper::createFromEntityWithSchemaAndSvg))
                .toRight(new ExceptionViewModel(ResourceUtil.getMessage(Response.SALON_NOT_FOUND), 400));
    }
}
