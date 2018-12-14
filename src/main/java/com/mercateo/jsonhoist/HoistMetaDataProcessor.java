package com.mercateo.jsonhoist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Encapsulates the extraction and addition of MetaData from Json Documents, as
 * well as Classes
 *
 * @author usr
 */
public interface HoistMetaDataProcessor {
    /**
     * This class extracts the HoistMetaData given in the java class clazz (e.g.
     * given with the halp of annotations like {@link HoistVersion})
     * 
     * @param clazz
     *            the class from which the HoistMetaData is extracted
     * @return the HoistMetaData
     */
    HoistMetaData extract(Class<?> clazz);

    /**
     * Tries to extract HoistMetaData from a given JSON. If nothing is
     * serialized in the JSON, defaultMetaData is used
     * 
     * @param rootNode
     *            the JSON from which the HoistMetaData is extracted
     * @param defaultMetaData
     *            the default HoistMetaData
     * @return the HoistMetaData
     */
    HoistMetaData extract(JsonNode rootNode, HoistMetaData defaultMetaData);

    /**
     * Adds the HoistMetaData to a given JSON document.
     * 
     * @param md
     *            the meta data
     * @param rootNode
     *            the JSON document
     */
    void add(HoistMetaData md, ObjectNode rootNode);

}
