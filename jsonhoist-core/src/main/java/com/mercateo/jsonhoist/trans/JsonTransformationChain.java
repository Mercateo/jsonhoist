package com.mercateo.jsonhoist.trans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Marker interface for an ordered list of JsonTransformations that can be
 * executed as one.
 *
 * @author usr
 *
 */

public interface JsonTransformationChain extends JsonTransformation {

    static JsonTransformationChain wrap(List<JsonTransformation> trans) {
        // defensive copy
        List<JsonTransformation> transformations = new ArrayList<>(trans);

        return new JsonTransformationChain() {

            @Override
            public JsonNode transform(JsonNode source) {
                JsonNode s = source;
                for (JsonTransformation t : transformations) {
                    if (s != null) {
                        s = t.transform(s);
                    }
                }
                return s;
            }
        };
    }

}
