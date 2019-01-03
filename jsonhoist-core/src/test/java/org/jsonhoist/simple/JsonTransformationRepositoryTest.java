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
package org.jsonhoist.simple;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.jsonhoist.HoistMetaData;
import org.jsonhoist.exc.HoistException;
import org.jsonhoist.trans.JsonTransformation;
import org.jsonhoist.trans.JsonTransformationRepository;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Value;

public class JsonTransformationRepositoryTest {

	private JsonTransformationRepository uut = new JsonTransformationRepository();

	@Test
	public void testSimplePaths() throws Exception {
		uut.register(v(1), v(2), fakeTrans(1));
		uut.register(v(2), v(99), fakeTrans(2)); // dead end
		uut.register(v(2), v(3), fakeTrans(3));
		uut.register(v(3), v(1), fakeTrans(4)); // cycle
		uut.register(v(3), v(4), fakeTrans(5));

		List<JsonTransformation> p = uut.defaultPath(v(1), v(4));
		assertThat(p).isNotEmpty().isNotNull().hasSize(3);

		AnnotatedJsonTransformation step1 = (AnnotatedJsonTransformation) p.get(0);
		AnnotatedJsonTransformation step2 = (AnnotatedJsonTransformation) p.get(1);
		AnnotatedJsonTransformation step3 = (AnnotatedJsonTransformation) p.get(2);

		assertThat(step1.id()).isEqualTo(1);
		assertThat(step2.id()).isEqualTo(3);
		assertThat(step3.id()).isEqualTo(5);

	}

	@Test
	public void testCyclePaths() throws Exception {
		assertThrows(HoistException.class, () -> {

			uut.register(v(1), v(2), fakeTrans(1));
			uut.register(v(2), v(1), fakeTrans(2)); // cycle

			uut.defaultPath(v(1), v(3));
		});
	}

	private JsonTransformation fakeTrans(int i) {
		return new AnnotatedJsonTransformation(i);
	}

	@Value
	static class AnnotatedJsonTransformation implements JsonTransformation {
		int id;

		@Override
		public JsonNode transform(JsonNode source) {
			return source;
		}
	}

	private HoistMetaData v(int i) {
		return HoistMetaData.of("foo", i);
	}

}
