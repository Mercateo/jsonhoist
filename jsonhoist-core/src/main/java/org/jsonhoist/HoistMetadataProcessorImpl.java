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

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Instance of {@link HoistMetadataProcessorImpl}, that can be used as default.
 *
 * For JsonDocuments, it expects "_meta.version" and "_meta.type" attributes,
 * where for classes, it expects the {@link HoistVersion} annotation.
 *
 * @author usr
 *
 */
@RequiredArgsConstructor
public class HoistMetadataProcessorImpl implements HoistMetaDataProcessor {
	private static final String TYPE = "type";

	private static final String VERSION = "version";

	private static final String _META = "_hoistmetadata";

	@NonNull
	private final ObjectMapper om;

	public HoistMetadataProcessorImpl() {
		this(JsonHoistInternalObjectMapper.getInstance());
	}

	@Override
	public HoistMetaData extract(@NonNull Class<?> clazz) {
		Optional<HoistVersion> versionAnnotation = Optional.ofNullable(clazz.getAnnotation(HoistVersion.class));

		String type = versionAnnotation.map(HoistVersion::type)
				// HoistVersion.NO_TYPE has the meaning of absent
				.filter(annotatedType -> !annotatedType.equals(HoistVersion.NO_TYPE)).orElse(clazz.getSimpleName());

		long version = versionAnnotation.map(HoistVersion::value).orElse(0L);

		return HoistMetaData.of(type, version);
	}

	@Override
	public HoistMetaData extract(@NonNull JsonNode rootNode, @NonNull HoistMetaData defaultMetaData) {
		String type = defaultMetaData.getType();
		long version = defaultMetaData.getVersion();

		if (rootNode.has(_META)) {
			ObjectNode meta = (ObjectNode) rootNode.get(_META);
			if (meta.has(VERSION)) {
				version = meta.get(VERSION).asLong();
			}
			if (meta.has(TYPE)) {
				type = meta.get(TYPE).asText();
			}
		}
		return HoistMetaData.of(type, version);
	}

	@Override
	public void add(@NonNull HoistMetaData md, @NonNull ObjectNode rootNode) {
		ObjectNode meta = om.createObjectNode();
		meta.put(TYPE, md.getType());
		meta.put(VERSION, md.getVersion());
		rootNode.set(_META, meta);
	}

}
