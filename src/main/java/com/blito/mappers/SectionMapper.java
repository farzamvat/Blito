package com.blito.mappers;

import com.blito.models.Section;
import com.blito.rest.viewmodels.salon.SectionViewModel;
import org.springframework.stereotype.Component;

/**
 * @author Farzam Vatanzadeh
 * 10/28/17
 * Mailto : farzam.vat@gmail.com
 **/
@Component
public class SectionMapper implements GenericMapper<Section,SectionViewModel> {
    @Override
    public Section createFromViewModel(SectionViewModel viewModel) {
        return null;
    }

    @Override
    public SectionViewModel createFromEntity(Section entity) {
        SectionViewModel viewModel = new SectionViewModel();
        viewModel.setId(entity.getId());
        viewModel.setName(entity.getName());
        viewModel.setSectionSvg(entity.getSectionSvg());
        viewModel.setSectionUid(entity.getSectionUid());
        return viewModel;
    }

    @Override
    public Section updateEntity(SectionViewModel viewModel, Section entity) {
        return null;
    }
}
