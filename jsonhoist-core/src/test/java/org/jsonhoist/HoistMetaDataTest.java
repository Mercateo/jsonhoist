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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class HoistMetaDataTest {
	@Test
	void testNullContract() throws Exception {
		assertThrows(NullPointerException.class, () -> {
			HoistMetaData.of(null, 1L);
		});
	}

	/**
	 * Test method for
	 * {@link org.jsonhoist.HoistMetaData#of(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testOfTypeAndVersionString() throws Exception {
		assertThrows(NullPointerException.class, () -> {
			HoistMetaData.of("Foo", (String) null);
		});

		assertThrows(NumberFormatException.class, () -> {
			HoistMetaData.of("Foo", "abc");
		});

		assertEquals("Foo:2.1", HoistMetaData.of("Foo", "2.1").toString());
		assertEquals("Foo:2.0", HoistMetaData.of("Foo", "2").toString());
		assertEquals("Foo:0.1", HoistMetaData.of("Foo", "0.1").toString());

		assertThrows(IllegalArgumentException.class, () -> {
			HoistMetaData.of("Foo", "-1.2");
		});
	}

	@Test
	void testEquals() {
		assertEquals(HoistMetaData.of("Foo", "2"), HoistMetaData.of("Foo", "2.0"));
		assertNotEquals(HoistMetaData.of("Foo", "2.1"), HoistMetaData.of("Foo", "2.0"));
	}
}
