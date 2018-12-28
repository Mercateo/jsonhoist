package org.jsonhoist.trans;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.LinkedList;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JSJsonTransformationChain0Test {

	@Test
	public void testNullContract() {
		expectNPE(() -> new JSJsonTransformationChain(null));
	}

	private void expectNPE(Runnable r) {
		try {
			r.run();
		} catch (NullPointerException e) {
		} catch (Throwable e) {
			throw new AssertionError("NPE Expected");
		}
	}

	@Test
	public void testEmptyList() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> {
			new JSJsonTransformationChain(new LinkedList<>());
		});
	}

	@Test
	public void testHappyPath() {
		JSJsonTransformation t1 = new JSJsonTransformation("function (e){ e.t1=true; } ");
		JSJsonTransformation t2 = new JSJsonTransformation("function (e){ e.t2=true; } ");
		JSJsonTransformation t3 = new JSJsonTransformation("function (e){ e.t3=true; } ");

		JSJsonTransformationChain uut = new JSJsonTransformationChain(Arrays.asList(t1, t2, t3));

		JsonNodeFactory f = new JsonNodeFactory(false);

		ObjectNode node = new ObjectNode(f, Maps.newHashMap("initial", new TextNode("value")));
		JsonNode afterTransformation = uut.transform(node);

		assertThat(afterTransformation.get("initial").asText()).isEqualTo("value");
		assertThat(afterTransformation.get("t1").asText()).isEqualTo("true");
		assertThat(afterTransformation.get("t2").asText()).isEqualTo("true");
		assertThat(afterTransformation.get("t3").asText()).isEqualTo("true");
	}

}
