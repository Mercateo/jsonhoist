package com.mercateo.jsonhoist;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Utility class for serializing/deserializing.
 * 
 * @author usr
 *
 */
@Getter
@RequiredArgsConstructor
public class HoistObjectMapper {

    @NonNull
    final ObjectMapper objectMapper;

    @NonNull
    final JsonHoist jsonHoist;

    public <T> T readValue(@NonNull String json, @NonNull Class<T> targetClass)
            throws JsonProcessingException, IOException {

        HoistMetaData to = jsonHoist.getMetaDataProcessor().extract(targetClass);

        JsonNode tree = objectMapper.readTree(json);
        JsonNode hoistedTree = jsonHoist.transform(tree, to);

        return objectMapper.readerFor(targetClass).readValue(hoistedTree);
    }

    public String writeValue(@NonNull Object o) {
        HoistMetaDataProcessor proc = jsonHoist.getMetaDataProcessor();
        HoistMetaData md = proc.extract(o.getClass());
        ObjectNode root = objectMapper.valueToTree(o);
        proc.add(md, root);
        return root.toString();
    }
}
