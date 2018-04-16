package com.blito.mappers;

import com.blito.models.Address;
import com.blito.rest.viewmodels.address.AddressViewModel;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper implements GenericMapper<Address,AddressViewModel> {
    @Override
    public Address createFromViewModel(AddressViewModel viewModel) {
        Address address = new Address();
        address.setAddress(viewModel.getAddress());
        address.setName(viewModel.getName());
        address.setLatitude(viewModel.getLatitude());
        address.setLongitude(viewModel.getLongitude());
        return address;
    }

    @Override
    public AddressViewModel createFromEntity(Address entity) {
        AddressViewModel viewModel = new AddressViewModel();
        viewModel.setAddress(entity.getAddress());
        viewModel.setName(entity.getName());
        viewModel.setLatitude(entity.getLatitude());
        viewModel.setLongitude(entity.getLongitude());
        viewModel.setId(entity.getId());
        viewModel.setEventHostLink(entity.getEventHost().getEventHostLink());
        viewModel.setEventHostId(entity.getEventHost().getEventHostId());
        return viewModel;
    }

    @Override
    public Address updateEntity(AddressViewModel viewModel, Address entity) {
        entity.setLongitude(viewModel.getLongitude());
        entity.setLatitude(viewModel.getLatitude());
        entity.setName(viewModel.getName());
        entity.setAddress(viewModel.getAddress());
        return entity;
    }
}
