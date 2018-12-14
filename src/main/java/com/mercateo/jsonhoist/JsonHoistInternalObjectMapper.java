package com.mercateo.jsonhoist;

import com.fasterxml.jackson.databind.ObjectMapper;

class JsonHoistInternalObjectMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

}
