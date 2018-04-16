package com.blito.services;

import com.blito.enums.Response;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.mappers.AddressMapper;
import com.blito.models.Address;
import com.blito.models.EventHost;
import com.blito.models.User;
import com.blito.repositories.AddressRepository;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.address.AddressViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private EventHostRepository eventHostRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AddressViewModel create(AddressViewModel viewModel,User user) {
        EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(viewModel.getEventHostId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
        if(eventHost.getUser().getUserId() != user.getUserId()) {
            throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
        }
        Address address = addressMapper.createFromViewModel(viewModel);
        address.setEventHost(eventHost);
        return addressMapper.createFromEntity(addressRepository.save(address));
    }

    @Transactional
    public AddressViewModel update(AddressViewModel viewModel,User user) {
        EventHost eventHost = Optional.ofNullable(eventHostRepository.findOne(viewModel.getEventHostId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.EVENT_HOST_NOT_FOUND)));
        if(eventHost.getUser().getUserId() != user.getUserId()) {
            throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
        }
        Address address = Optional.ofNullable(addressRepository.findOne(viewModel.getId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.ADDRESS_NOT_FOUND)));
        address = addressMapper.updateEntity(viewModel,address);
        return addressMapper.createFromEntity(addressRepository.save(address));
    }

    @Transactional
    public void delete(Long addressId,User user) {
        Address address = Optional.ofNullable(addressRepository.findOne(addressId))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.ADDRESS_NOT_FOUND)));
        if(address.getEventHost().getUser().getUserId() != user.getUserId()) {
            throw new NotAllowedException(ResourceUtil.getMessage(Response.NOT_ALLOWED));
        }
        address.getEventHost().getAddresses().removeIf(adr -> adr.getId() == addressId);
        addressRepository.delete(addressId);
    }

    @Transactional
    public Set<AddressViewModel> getAllEventHostsAddresses(User currentUser) {
        User user = Optional.ofNullable(userRepository.findOne(currentUser.getUserId()))
                .orElseThrow(() -> new NotFoundException(ResourceUtil.getMessage(Response.USER_NOT_FOUND)));
        return user.getEventHosts().stream().flatMap(eventHost -> eventHost.getAddresses().stream())
                .map(addressMapper::createFromEntity).collect(Collectors.toSet());
    }
}
