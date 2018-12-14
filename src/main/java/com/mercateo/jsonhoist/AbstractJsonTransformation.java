package com.mercateo.jsonhoist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercateo.jsonhoist.trans.JsonTransformation;

/**
 * Encapsulates one transformation step. Note that {@link com.mercateo.jsonhoist.trans.JSJsonTransformation}
 * is a subclass, which can be used for optimizations
 *
 * @author usr
 *
 */
public abstract class AbstractJsonTransformation implements JsonTransformation {

    // does not have to be configured, just used for simple transformations
    static protected final ObjectMapper om = JsonHoistInternalObjectMapper.getInstance();

    /*
     * (non-Javadoc)
     *
     * @see com.mercateo.jsonupcaster.trans.JsonTransformation#transform(com.
     * fasterxml.jackson.databind.JsonNode)
     */
    @Override
    public abstract JsonNode transform(JsonNode source);

}
