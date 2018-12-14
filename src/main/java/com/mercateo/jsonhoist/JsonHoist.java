package com.mercateo.jsonhoist;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Main interface that provides a means to upcast a json document from one
 * version to another
 *
 * @author usr
 */
public interface JsonHoist {

    JsonNode transform(String json, HoistMetaData target);

    JsonNode transform(JsonNode rootNode, HoistMetaData target);

    HoistMetaDataProcessor getMetaDataProcessor();
}
