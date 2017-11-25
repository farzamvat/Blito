package com.blito.common.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
    @author Farzam Vatanzadeh
*/
public interface SalonComponent <T extends AbstractBaseSalonEntity> {
    String toJson(ObjectMapper objectMapper) throws JsonProcessingException;
}
