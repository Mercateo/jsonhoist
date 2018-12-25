package com.mercateo.jsonhoist.trans;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonTransformation {

    JsonNode transform(JsonNode source);

}