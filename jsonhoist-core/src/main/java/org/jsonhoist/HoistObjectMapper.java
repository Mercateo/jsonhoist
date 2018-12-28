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
package org.jsonhoist;

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

		HoistMetaData to = jsonHoist.metaDataProcessor().extract(targetClass);

		JsonNode tree = objectMapper.readTree(json);
		JsonNode hoistedTree = jsonHoist.transform(tree, to);

		return objectMapper.readerFor(targetClass).readValue(hoistedTree);
	}

	public String writeValue(@NonNull Object o) {
		HoistMetaDataProcessor proc = jsonHoist.metaDataProcessor();
		HoistMetaData md = proc.extract(o.getClass());
		ObjectNode root = objectMapper.valueToTree(o);
		proc.add(md, root);
		return root.toString();
	}
}
