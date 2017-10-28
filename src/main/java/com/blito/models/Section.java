package com.blito.models;

import javax.persistence.*;

/**
 * @author Farzam Vatanzadeh
 * 10/28/17
 * Mailto : farzam.vat@gmail.com
 **/
@Entity(name="section")
public class Section {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition="TEXT")
    private String sectionSvg;
    private String name;
    private String sectionUid;

    public Section() {
    }

    public Section(String name, String sectionUid) {
        this.name = name;
        this.sectionUid = sectionUid;
    }

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
