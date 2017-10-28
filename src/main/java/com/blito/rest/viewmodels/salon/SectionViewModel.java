package com.blito.rest.viewmodels.salon;

import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author Farzam Vatanzadeh
 * 10/28/17
 * Mailto : farzam.vat@gmail.com
 **/
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SectionViewModel implements Serializable {
    @JsonView(View.SalonSchema.class)
    private Long id;
    @JsonView(View.SalonSchema.class)
    @NotBlank
    private String sectionSvg;
    @JsonView(View.SalonSchema.class)
    private String name;
    @JsonView(View.SalonSchema.class)
    @NotBlank
    private String sectionUid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionSvg() {
        return sectionSvg;
    }

    public void setSectionSvg(String sectionSvg) {
        this.sectionSvg = sectionSvg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSectionUid() {
        return sectionUid;
    }

    public void setSectionUid(String sectionUid) {
        this.sectionUid = sectionUid;
    }
}
