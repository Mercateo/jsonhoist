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
/**
 * 
 */
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

public class JSJsonTransformationChainTest {

	@Test
	void testNullContracts() throws Exception {
		assertThrows(NullPointerException.class, () -> {
			new JSJsonTransformationChain(null);
		});

	}
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
