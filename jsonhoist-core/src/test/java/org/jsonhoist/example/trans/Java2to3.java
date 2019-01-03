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
package org.jsonhoist.example.trans;

import org.jsonhoist.AbstractJsonTransformation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Java2to3 extends AbstractJsonTransformation {

	@Override
	public JsonNode transform(JsonNode source) {
		ObjectNode object = (ObjectNode) source;

		object.put("name", object.get("fullName").asText());

		ObjectNode details = om.createObjectNode();
		object.set("details", details);

		details.put("first", object.get("firstName").asText());
		details.put("last", object.get("lastName").asText());
		details.put("age", object.get("age").asInt());

		return object;
	}

}
