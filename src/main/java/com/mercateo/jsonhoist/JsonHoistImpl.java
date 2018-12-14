package com.mercateo.jsonhoist;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.mercateo.jsonhoist.exc.HoistException;
import com.mercateo.jsonhoist.trans.JsonTransformation;
import com.mercateo.jsonhoist.trans.JsonTransformationChain;
import com.mercateo.jsonhoist.trans.JsonTransformationRepository;

import lombok.Getter;
import lombok.NonNull;

/**
 * Trivial impl of a JsonHoist.
 *
 * Extensions should be threadsafe.
 *
 * @author usr
 *
 */
@Component

public class JsonHoistImpl implements JsonHoist {

    @Getter
    private final HoistMetaDataProcessor metaDataProcessor;

    private final JsonTransformationRepository repo;

    public JsonHoistImpl(@NonNull JsonTransformationRepository repo) {
        this(new HoistMetadataProcessorImpl(), repo);
    }

    public JsonHoistImpl(@NonNull HoistMetaDataProcessor p,
            @NonNull JsonTransformationRepository repo) {
        metaDataProcessor = p;
        this.repo = repo;
    }

    @Override
    public JsonNode transform(@NonNull String json, @NonNull HoistMetaData target) {
        try {
            return transform(JsonHoistInternalObjectMapper.getInstance().readTree(json), target);
        } catch (IOException e) {
            throw new HoistException(e);
        }
    }

    @Override
    public JsonNode transform(@NonNull JsonNode rootNode, @NonNull HoistMetaData target) {
        HoistMetaData from = metaDataProcessor.extract(rootNode, target);
        HoistMetaData to = target;

        List<JsonTransformation> defaultPath = repo.defaultPath(from, to);
        if (defaultPath == null) {
            throw new HoistException("Cannot find a transformation path from " + from + " to "
                    + to);
        }

        JsonTransformationChain chain = JsonTransformationChain.wrap(defaultPath);
        return chain.transform(rootNode);
    }
}
