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
package org.jsonhoist.spring.boot;

import static org.junit.jupiter.api.Assertions.*;

import org.jsonhoist.HoistMetaData;
import org.jsonhoist.trans.JsonTransformationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = JsonHoistAutoConfiguration.class)
@DirtiesContext
public class JsonHoistAutoConfigurationLoadFromCPEnabledTest {

	@Autowired
	JsonTransformationRepository r;

	@BeforeAll
	static void setup() {
		System.setProperty("jsonhoist.repository.classpath.enabled", "true");
	}

	@Test
	void testEnabledProperty() throws Exception {

		assertNotNull(r.defaultPath(HoistMetaData.of("Foo", 1), HoistMetaData.of("Foo", 2)));
	}
}
