package org.jsonhoist.trans;

import com.fasterxml.jackson.databind.JsonNode;

public class NOPTransformation implements JsonTransformation {

	public static NOPTransformation INSTANCE = new NOPTransformation();

	@Override
	public JsonNode transform(JsonNode source) {
		return source;
	}

}
