package com.mercateo.jsonhoist.example.trans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mercateo.jsonhoist.AbstractJsonTransformation;

public class Java1to2 extends AbstractJsonTransformation {

	private static final int DEFAULT_AGE = 999;

	@Override
	public JsonNode transform(JsonNode source) {
		ObjectNode object = (ObjectNode) source;

		object.put("fullName", object.get("firstName").asText() + " " + object.get("lastName").asText());

		if (!object.has("age")) {
			object.put("age", DEFAULT_AGE);
		}

		return object;
	}

}
