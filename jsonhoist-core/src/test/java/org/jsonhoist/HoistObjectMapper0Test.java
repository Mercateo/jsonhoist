package org.jsonhoist;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.jsonhoist.exc.HoistException;
import org.jsonhoist.trans.ClassPathJSJsonTransformationLoader;
import org.jsonhoist.trans.JsonTransformationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

public class HoistObjectMapper0Test {
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

	@HoistVersion(type = "TestJson", value = 1)
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TestJsonV1 {
		private String firstName;

		private String lastName;
	}

	// upcast here:
	// /content-json-upcaster/src/test/resources/jsonhoist/repository/TestJson/1-2.js
	@HoistVersion(type = "TestJson", value = 2)
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TestJsonV2 {
		private String fullName;
	}

	// there is no mapping for v3
	@HoistVersion(type = "TestJson", value = 3)
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TestJsonV3 {
		private String fullName;
	}
}
