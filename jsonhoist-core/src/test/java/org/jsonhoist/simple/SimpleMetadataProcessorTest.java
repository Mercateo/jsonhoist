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

import static org.junit.jupiter.api.Assertions.*;

import org.jsonhoist.HoistMetadataProcessorImpl;
import org.jsonhoist.HoistVersion;
import org.junit.jupiter.api.Test;

public class SimpleMetadataProcessorTest {

	@Test
	public void testVersionOnly() throws Exception {

		HoistMetadataProcessorImpl uut = new HoistMetadataProcessorImpl();
		assertEquals(173, uut.extract(Version173.class).version());
		assertEquals(Version173.class.getSimpleName(), uut.extract(Version173.class).type());
	}

	@Test
	public void testTypeOverride() throws Exception {

		HoistMetadataProcessorImpl uut = new HoistMetadataProcessorImpl();
		assertEquals(1, uut.extract(SomePojoWithOverriddenType.class).version());
		assertEquals("Narf", uut.extract(SomePojoWithOverriddenType.class).type());

	}

	@HoistVersion(173)
	static class Version173 {
	}

	@HoistVersion(value = 1, type = "Narf")
	static class SomePojoWithOverriddenType {
	}
}
