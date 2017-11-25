package com.blito.common.base;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.enums.BlitTypeSeatState;
import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class AbstractBaseSalonEntity implements Serializable {
    @JsonView(View.SalonSchema.class)
    protected String uid;
    @JsonView(View.SalonSchema.class)
    protected String name;
    @JsonView(View.SalonSchema.class)
    protected BlitTypeSeatState state;

    public AbstractBaseSalonEntity(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }
    public AbstractBaseSalonEntity() {}

    public BlitTypeSeatState getState() {
        return state;
    }

    public void setState(BlitTypeSeatState state) {
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
