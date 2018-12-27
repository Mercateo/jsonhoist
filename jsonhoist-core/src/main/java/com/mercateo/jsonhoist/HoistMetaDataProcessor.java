/*
 * Copyright © 2018 Mercateo AG (http://www.mercateo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	 * Tries to extract HoistMetaData from a given JSON. If nothing is serialized in
	 * the JSON, defaultMetaData is used
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
