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
package org.jsonhoist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.jsonhoist.example.TestJsonV1;
import org.jsonhoist.example.TestJsonV2;
import org.jsonhoist.example.TestJsonV3;
import org.jsonhoist.exc.HoistException;
import org.jsonhoist.trans.ClassPathJSJsonTransformationLoader;
import org.jsonhoist.trans.JsonTransformationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author usr
 *
 */
public class HoistObjectMapperTest {

	@Test
	void testNullContracts() throws Exception {
		HoistObjectMapper uut = new HoistObjectMapper(mock(ObjectMapper.class), mock(JsonHoist.class));

		assertThrows(NullPointerException.class, () -> {
			uut.writeValue(null);
		});

	}

	private HoistObjectMapper uut;

	private JsonTransformationRepository repo;

	@BeforeEach
	public void setup() throws Exception {
		repo = new JsonTransformationRepository();

		uut = new HoistObjectMapper(new ObjectMapper(), new JsonHoistImpl(repo));
	}

	@Test()
	public void testWithoutAnyMapping() throws JsonProcessingException, IOException {
		assertThrows(HoistException.class, () -> {
			TestJsonV1 v1 = new TestJsonV1();
			v1.setFirstName("tim");
			v1.setLastName("tester");
			String json = uut.writeValue(v1);
			uut.readValue(json, TestJsonV2.class);
		});
	}

	@Test
	public void testSuccessfulMapping() throws JsonProcessingException, IOException {
		new ClassPathJSJsonTransformationLoader(repo).load();
		TestJsonV1 v1 = new TestJsonV1();
		v1.setFirstName("tim");
		v1.setLastName("tester");
		String json = uut.writeValue(v1);
		TestJsonV2 v2 = uut.readValue(json, TestJsonV2.class);
		assertEquals("tim tester", v2.getFullName());
	}

	@Test
	public void testNoMappingToV3() throws JsonProcessingException, IOException {
		assertThrows(HoistException.class, () -> {
			new ClassPathJSJsonTransformationLoader(repo).load();
			TestJsonV1 v1 = new TestJsonV1();
			v1.setFirstName("tim");
			v1.setLastName("tester");
			String json = uut.writeValue(v1);
			uut.readValue(json, TestJsonV3.class);
		});
	}
}
