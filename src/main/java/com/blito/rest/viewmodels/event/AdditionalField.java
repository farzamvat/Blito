package com.blito.rest.viewmodels.event;
/*
    @author Farzam Vatanzadeh
*/

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Map;

public class AdditionalField implements Serializable {
    @NotBlank
    private String key;
    @NotBlank
    private String value;

    public AdditionalField(@JsonProperty("key") String key,@JsonProperty("value") String value) {
        this.key = key;
        this.value = value;
    }

    public AdditionalField(Map.Entry<String,String> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
