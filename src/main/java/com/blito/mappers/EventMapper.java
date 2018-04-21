package com.blito.mappers;

import com.blito.configs.Constants;
import com.blito.enums.*;
import com.blito.exceptions.FileNotFoundException;
import com.blito.models.Event;
import com.blito.models.EventDate;
import com.blito.models.Salon;
import com.blito.repositories.SalonRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.event.AdditionalField;
import com.blito.rest.viewmodels.event.EventViewModel;
import com.blito.rest.viewmodels.eventdate.EventDateViewModel;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventMapper implements GenericMapper<Event, EventViewModel> {

    @Autowired
    private EventDateMapper eventDateMapper;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private SalonRepository salonRepository;

    @Override
    public Event createFromViewModel(EventViewModel vmodel) {
        Event event = new Event();
        event.setEventName(vmodel.getEventName());
        event.setAddress(vmodel.getAddress());
        event.setAparatDisplayCode(vmodel.getAparatDisplayCode());
        event.setBlitSaleEndDate(vmodel.getBlitSaleEndDate());
        event.setBlitSaleStartDate(vmodel.getBlitSaleStartDate());
        event.setDescription(vmodel.getDescription());
        event.setEventType(vmodel.getEventType().name());
        event.setLatitude(vmodel.getLatitude());
        event.setLongitude(vmodel.getLongitude());
        event.setOperatorState(OperatorState.PENDING.name());
        vmodel.getEventDates().forEach(ed -> event.addEventDate(eventDateMapper.createFromViewModel(ed)));
        event.setAdditionalFields(vmodel.getAdditionalFields().stream().collect(Collectors.toMap(AdditionalField::getKey, AdditionalField::getValue)));
        event.setMembers(vmodel.getMembers());
        event.setCreatedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).toInstant()));
        event.setEndDate(Timestamp.from(ZonedDateTime.of(2200, 1, 1, 0,0,0,0, ZoneId.of("Asia/Tehran")).toInstant()));
        event.setEventState(State.CLOSED.name());
        event.setEvento(false);
        event.setPrivate(vmodel.isPrivate());
        Optional.ofNullable(vmodel.getSalonUid()).filter(salonUid -> !salonUid.isEmpty())
                .ifPresent(salonUid -> {
                    Salon salon = salonRepository.findBySalonUid(salonUid).orElseThrow(() -> new FileNotFoundException(ResourceUtil.getMessage(Response.SALON_NOT_FOUND)));
                    event.getEventDates().forEach(eventDate -> eventDate.setSalon(salon));
                });
        return event;
    }

    @Override
    public EventViewModel createFromEntity(Event event) {
        EventViewModel vmodel = new EventViewModel();
        vmodel.setAddress(event.getAddress());
        vmodel.setAparatDisplayCode(event.getAparatDisplayCode());
        vmodel.setBlitSaleEndDate(event.getBlitSaleEndDate());
        vmodel.setBlitSaleStartDate(event.getBlitSaleStartDate());
        vmodel.setDescription(event.getDescription());
        vmodel.setEventHostId(event.getEventHost().getEventHostId());
        vmodel.setEventHostName(event.getEventHost().getHostName());
        vmodel.setEventId(event.getEventId());
        Option.of(event.getEventLink())
                .filter(link -> link.startsWith(Constants.EVENT_UPDATE_EDITED_LINK))
                .peek(link -> vmodel.setEventLink(link.replaceFirst(Constants.EVENT_UPDATE_EDITED_LINK,"")))
                .onEmpty(() -> vmodel.setEventLink(event.getEventLink()));
        vmodel.setEventName(event.getEventName());
        vmodel.setEventType(Enum.valueOf(EventType.class, event.getEventType()));
        vmodel.setOrderNumber(event.getOrderNumber());
        vmodel.setOperatorState(Enum.valueOf(OperatorState.class, event.getOperatorState()));
        vmodel.setEventState(Enum.valueOf(State.class, event.getEventState()));
        vmodel.setEventDates(eventDateMapper.createFromEntities(event.getEventDates()));
        vmodel.setOffers(event.getOffers().stream().map(offer -> Enum.valueOf(OfferTypeEnum.class, offer)).collect(Collectors.toSet()));
        vmodel.setLatitude(event.getLatitude());
        vmodel.setLongitude(event.getLongitude());
        vmodel.setEvento(event.isEvento());
        if (!event.getImages().isEmpty())
            vmodel.setImages(imageMapper.createFromEntities(event.getImages()));
        vmodel.setCreatedAt(event.getCreatedAt());
        vmodel.setMembers(event.getMembers());
        vmodel.setEventSoldDate(event.getEventSoldDate());
        vmodel.setDeleted(event.isDeleted());
        vmodel.setViews(event.getViews());
        Optional.ofNullable(event.getAdditionalFields())
                .ifPresent(additionalFields -> {
                    vmodel.setAdditionalFields(additionalFields
                            .entrySet()
                            .stream()
                            .map(AdditionalField::new)
                            .collect(Collectors.toList()));
                });
        vmodel.setPrivate(event.isPrivate());
        vmodel.setEndDate(event.getEndDate());
        event.getEventDates().stream().filter(eventDate -> eventDate.getSalon() != null).findAny().ifPresent(eventDate -> vmodel.setSalonUid(eventDate.getSalon().getSalonUid()));
        if(event.getEditedVersion() != null) {
            vmodel.setEditedVersion(createFromEntity(event.getEditedVersion()));
        }
        return vmodel;
    }

    @Override
    public Event updateEntity(EventViewModel vmodel, Event event) {
        event.setAddress(vmodel.getAddress());
        event.setAparatDisplayCode(vmodel.getAparatDisplayCode());
        event.setBlitSaleStartDate(vmodel.getBlitSaleStartDate());
        event.setBlitSaleEndDate(vmodel.getBlitSaleEndDate());
        event.setDescription(vmodel.getDescription());
        event.setEventName(vmodel.getEventName());
        event.setLongitude(vmodel.getLongitude());
        event.setLatitude(vmodel.getLatitude());
        event.setEventType(vmodel.getEventType().name());
        event.setMembers(vmodel.getMembers());
        Option.of(event.getAdditionalFields())
                .filter(additionalFieldList -> !additionalFieldList.isEmpty())
                .peek(additionalFieldList -> {
                    event.setAdditionalFields(vmodel.getAdditionalFields().stream().collect(Collectors.toMap(AdditionalField::getKey, AdditionalField::getValue)));
                })
                .onEmpty(() -> {
                    event.setAdditionalFields(null);
                });

        List<String> oldOnes = vmodel.getEventDates().stream().map(EventDateViewModel::getUid).filter(uid -> Objects.nonNull(uid) && !uid.isEmpty()).collect(Collectors.toList());
        List<String> shouldDelete = new ArrayList<>();
        event.getEventDates().forEach(eventDate -> {
            if (!oldOnes.contains(eventDate.getUid())) {
                shouldDelete.add(eventDate.getUid());
            }
        });
        shouldDelete.forEach(event::removeEventDateByUid);

        vmodel.getEventDates().forEach(edvm ->
            Option.ofOptional(event.getEventDates()
                    .stream()
                    .filter(eventDate -> Objects.nonNull(edvm.getUid()) && !edvm.getUid().isEmpty() && eventDate.getUid().equals(edvm.getUid()))
                    .findFirst())
                    .peek(eventDate -> eventDateMapper.updateEntity(edvm, eventDate))
                    .onEmpty(() -> {
                        EventDate eventDate = eventDateMapper.createFromViewModel(edvm);
                        event.addEventDate(eventDate);
                    })
        );
        event.setPrivate(vmodel.isPrivate());
        Optional.ofNullable(vmodel.getSalonUid()).filter(salonUid -> !salonUid.isEmpty())
                .ifPresent(salonUid -> {
                    Salon salon = salonRepository.findBySalonUid(salonUid).orElseThrow(() -> new FileNotFoundException(ResourceUtil.getMessage(Response.SALON_NOT_FOUND)));
                    event.getEventDates().forEach(eventDate -> eventDate.setSalon(salon));
                });
        return event;
    }
}
