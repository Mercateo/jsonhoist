package com.mercateo.jsonhoist.example.trans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mercateo.jsonhoist.AbstractJsonTransformation;

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
