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
package org.jsonhoist.trans;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.jsonhoist.HoistMetaData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClassPathJSJsonTransformationLoaderTest {

	@Mock
	private JsonTransformationRepository mockRepo;

	@Test
	public void testRoundTrip() throws Exception {
		ClassPathJSJsonTransformationLoader uut = new ClassPathJSJsonTransformationLoader(".*/loadertest/1/.*/.*\\.js",
				mockRepo);

		uut.load();

		verify(mockRepo).register(eq(HoistMetaData.of("testtype", 1)), eq(HoistMetaData.of("testtype", 2)), any());
		verify(mockRepo).register(eq(HoistMetaData.of("testtype", 2)), eq(HoistMetaData.of("testtype", 3)), any());
		verifyNoMoreInteractions(mockRepo);

	}

	@Test
	public void testLoad() throws Exception {
		JsonTransformationRepository emptyRepo = new JsonTransformationRepository();
		ClassPathJSJsonTransformationLoader uut = new ClassPathJSJsonTransformationLoader("/xyz.*", emptyRepo);

		JsonTransformationRepository repo = uut.load();
		// must be the same reference
		assertThat(repo).isSameAs(emptyRepo);
	}

}
