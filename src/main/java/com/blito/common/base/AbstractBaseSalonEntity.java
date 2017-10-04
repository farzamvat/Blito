package com.blito.common.base;
/*
    @author Farzam Vatanzadeh
*/

import java.io.Serializable;

public abstract class AbstractBaseSalonEntity implements Serializable {
    protected String uid;
    protected String name;

    public AbstractBaseSalonEntity(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }
    public AbstractBaseSalonEntity() {}

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
