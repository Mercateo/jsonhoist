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
package org.jsonhoist.trans;

import java.io.IOException;
import java.util.List;

import org.jsonhoist.HoistMetaData;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Impl of TransformationRepository, that loads transformation scripts from a
 * well-known location of the classpath:
 * <p>
 * <code>classpath:/jsonupcaster/repository/TYPE_NAME/FROM-TO.js</code> <br>
 * <p>
 * e.g.:
 * <p>
 * <code>classpath:/jsonupcaster/repository/FooBar/0-1.js</code>
 *
 * @author usr
 */
@Slf4j
public class ClassPathJSJsonTransformationLoader {

	public static final String DEFAULT_RESOURCE_PATTERN = ".*/jsonhoist/repository/.*\\.js";

	@NonNull
	private String pattern;

	@NonNull
	private JsonTransformationRepository repo;

	private boolean loaded = false;

	public ClassPathJSJsonTransformationLoader(@NonNull JsonTransformationRepository repo) {
		this(DEFAULT_RESOURCE_PATTERN, repo);
	}

	public ClassPathJSJsonTransformationLoader(@NonNull String pattern, @NonNull JsonTransformationRepository repo) {
		this.pattern = pattern;
		this.repo = repo;
	}

	public synchronized JsonTransformationRepository load() throws IOException {
		if (loaded)
			log.warn("Already loaded");
		else {
			log.info("Loading transformations from Classpath");
			List<ClassPathResource> listOfMatchingResources = new ClassPathFilter(pattern).listRecursive();
			listOfMatchingResources.forEach(res -> addResource(res, repo));
			loaded = true;
		}
		return repo;
	}

	void addResource(ClassPathResource res, JsonTransformationRepository repo) {
		try {
			if (res != null) {
				ResourceUriParser p = new ResourceUriParser(res.getURI());
				String type = p.type();
				String name = p.fileName();
				name = name.substring(0, name.lastIndexOf("."));
				String[] split = name.split("-");
				if (split.length != 2) {
					throw new IllegalStateException("Cannot parse filename " + res.getFilename());
				}

				HoistMetaData from = HoistMetaData.of(type, split[0]);
				HoistMetaData to = HoistMetaData.of(type, split[1]);

				log.info("adding transformation '{}' as {}->{}", res.getFilename(), from, to);

				JSJsonTransformation transformation = new JSJsonTransformation(res.getURI().toURL());
				repo.register(from, to, transformation);

			}
		} catch (IOException e) {
			throw new IllegalStateException("Cannot get file " + res.getFilename(), e);
		}
	}

}
