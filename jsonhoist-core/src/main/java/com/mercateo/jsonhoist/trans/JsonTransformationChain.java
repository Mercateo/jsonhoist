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
