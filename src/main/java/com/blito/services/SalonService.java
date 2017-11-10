package com.blito.services;

import com.blito.common.Seat;
import com.blito.configs.Constants;
import com.blito.enums.BlitTypeSeatState;
import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.SeatException;
import com.blito.mappers.SalonMapper;
import com.blito.models.BlitTypeSeat;
import com.blito.models.Salon;
import com.blito.models.Section;
import com.blito.repositories.EventDateRepository;
import com.blito.repositories.SalonRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.exception.ExceptionViewModel;
import com.blito.rest.viewmodels.salon.SalonViewModel;
import com.blito.rest.viewmodels.salon.SectionViewModel;
import com.blito.services.blit.SeatBlitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/*
    @author Farzam Vatanzadeh
*/
@Service
public class SalonService {
    @Autowired
    private SalonRepository salonRepository;
    @Autowired
    private SalonMapper salonMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EventDateRepository eventDateRepository;

    @Transactional
    public Page<SalonViewModel> getSalons(Pageable pageable) {
        return salonMapper.toPage(salonRepository.findAll(pageable));
    }

    @Transactional
    public Either<ExceptionViewModel,SalonViewModel> getSalonByUid(String salonUid) {
        return Option.ofOptional(salonRepository.findBySalonUid(salonUid).map(salonMapper::createFromEntityWithSchemaAndSvg))
                .toRight(new ExceptionViewModel(ResourceUtil.getMessage(Response.SALON_NOT_FOUND), 400));
    }

    @Transactional
    public Either<ExceptionViewModel,SalonViewModel> updateSalonAndItsSectionsSvg(SalonViewModel salonViewModel) {
        if(salonViewModel.getSalonSvg().isEmpty() || salonViewModel.getSections().stream().anyMatch(sectionViewModel -> sectionViewModel.getSectionSvg().isEmpty())) {
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.SALON_OR_SECTIONS_SVG_CANNOT_BE_EMPTY),400));
        }
        Optional<Salon> salonOptional = salonRepository.findBySalonUid(salonViewModel.getSalonUid());
        if(!salonOptional.isPresent()) {
            return Either.left(new ExceptionViewModel(ResourceUtil.getMessage(Response.SALON_NOT_FOUND), 400));
        } else {
            return Option.ofOptional(salonOptional).filter(salon -> salon.getSections().size() == salonViewModel.getSections().size())
                    .filter(salon ->
                            salon.getSections().stream().map(Section::getSectionUid).allMatch(uid ->
                                    salonViewModel.getSections().stream().map(SectionViewModel::getSectionUid).collect(Collectors.toList()).contains(uid)))
                    .map(salon -> salonMapper.updateSalonAndItsSectionsSvg(salonViewModel,salon))
                    .map(salonMapper::createFromEntity)
                    .toRight(new ExceptionViewModel(ResourceUtil.getMessage(Response.INCONSISTENT_SECTION_UIDS),400));
        }
    }

    public void validateNoIndividualSeat(com.blito.common.Salon salon) {
        salon.getSections().stream().flatMap(section -> section.getRows().stream())
                .forEach(row ->
                    row.getSeats().stream().sorted(Comparator.comparing(Seat::getName)).forEachOrdered(currentSeat -> {
                       Seat next = currentSeat.getNextSeat(row);
                       Seat prev = currentSeat.getPreviousSeat(row);
                       if(prev == null && next != null) {
                           if(currentSeat.getState().equals(BlitTypeSeatState.AVAILABLE) && !next.getState().equals(BlitTypeSeatState.AVAILABLE)) {
                               throw new SeatException(ResourceUtil.getMessage(Response.INDIVIDUAL_SEAT_ERROR));
                           }
                       } else if(prev != null && next == null) {
                           if(currentSeat.getState().equals(BlitTypeSeatState.AVAILABLE) && !prev.getState().equals(BlitTypeSeatState.AVAILABLE)) {
                               throw new SeatException(ResourceUtil.getMessage(Response.INDIVIDUAL_SEAT_ERROR));
                           }
                       } else if(prev != null) {
                           if(currentSeat.getState().equals(BlitTypeSeatState.AVAILABLE) &&
                                   !next.getState().equals(BlitTypeSeatState.AVAILABLE) &&
                                   !prev.getState().equals(BlitTypeSeatState.AVAILABLE)) {
                               throw new SeatException(ResourceUtil.getMessage(Response.INDIVIDUAL_SEAT_ERROR));
                           }
                       }
                    })
                );
    }

    public com.blito.common.Salon getSalonSchema(Salon salon) {
        return Try.of(() -> new File(SeatBlitService.class.getResource(Constants.BASE_SALON_SCHEMAS + "/" + salon.getPlanPath()).toURI()))
                .flatMapTry(file -> Try.of(() -> objectMapper.readValue(file, com.blito.common.Salon.class)))
                .getOrElseThrow(() -> new RuntimeException(ResourceUtil.getMessage(Response.INTERNAL_SERVER_ERROR)));
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public com.blito.common.Salon populateSeatInformationInSalonSchemaByEventDateId(Long eventDateId) {
        return Option.of(eventDateRepository.findOne(eventDateId))
                .map(eventDate -> {
                    Set<BlitTypeSeat> blitTypeSeats =
                            eventDate.getBlitTypes()
                                .stream()
                                .filter(blitType -> blitType.getBlitTypeSeats() != null &&
                                        !blitType.getBlitTypeSeats().isEmpty())
                                .flatMap(blitType -> blitType.getBlitTypeSeats().stream()).collect(Collectors.toSet());
                    blitTypeSeats.stream()
                            .filter(blitTypeSeat -> blitTypeSeat.getState().equals(BlitTypeSeatState.RESERVED.name()))
                            .forEach(blitTypeSeat ->
                                    Optional.ofNullable(blitTypeSeat.getReserveDate())
                                            .filter(reservedDate -> reservedDate.before(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusMinutes(10L).toInstant())))
                                            .ifPresent(dump -> {
                                                blitTypeSeat.setState(BlitTypeSeatState.AVAILABLE.name());
                                                blitTypeSeat.setReserveDate(null);
                                            }));
                    return Option.of(eventDate.getSalon())
                            .map(salon -> {
                                com.blito.common.Salon schema = getSalonSchema(salon);
                                schema.getSections().stream().flatMap(section -> section.getRows().stream())
                                        .flatMap(row -> row.getSeats().stream())
                                        .forEach(seat ->
                                            Option.ofOptional(blitTypeSeats.stream().filter(blitTypeSeat -> blitTypeSeat.getSeat().getSeatUid().equals(seat.getUid()))
                                                    .findAny()).peek(blitTypeSeat -> {
                                                        seat.setBlitTypeSeatId(blitTypeSeat.getBlitTypeSeatId());
                                                        seat.setReserveDate(blitTypeSeat.getReserveDate());
                                                        seat.setSoldDate(blitTypeSeat.getSoldDate());
                                                        seat.setPrice(blitTypeSeat.getBlitType().getPrice());
                                                        seat.setBlitTypeId(blitTypeSeat.getBlitType().getBlitTypeId());
                                                        seat.setState(Enum.valueOf(BlitTypeSeatState.class,blitTypeSeat.getState()));
                                                        Option.of(blitTypeSeat.getSeatBlit())
                                                                .peek(seatBlit -> seat.setCustomerName(seatBlit.getCustomerName()));
                                            }).onEmpty(() ->
                                                seat.setState(BlitTypeSeatState.NOT_AVAILABLE))
                                        );
                                return schema;
                            }).getOrElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.SALON_NOT_FOUND)));
                }).getOrElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_DATE_NOT_FOUND)));
    }
}