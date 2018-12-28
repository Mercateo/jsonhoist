package org.jsonhoist;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class HoistMetadataProcessorImpl0Test {

	HoistMetadataProcessorImpl uut = new HoistMetadataProcessorImpl();

	@Test
	public void testNoVersionInfo() {
		HoistMetaData extract = uut.extract(NoVersionInfo.class);

		assertEquals("NoVersionInfo", extract.getType());
		assertEquals(0, extract.getVersion());
	}

	@Test
	public void testVersionInfo() {
		assertEquals(21, uut.extract(WithVersionInfo.class).getVersion());
		assertEquals(WithVersionInfo.class.getSimpleName(), uut.extract(WithVersionInfo.class).getType());
	}

	@Test
	public void testTypeOverride() {
		assertEquals(31, uut.extract(WithVersionInfoAndType.class).getVersion());
		assertEquals("foo", uut.extract(WithVersionInfoAndType.class).getType());
	}

	public static class NoVersionInfo {
	}

	@HoistVersion(21)
	public static class WithVersionInfo {
	}

	@HoistVersion(value = 31, type = "foo")
	public static class WithVersionInfoAndType {
	}

	@Test
	public void testExtractJsonNodeWithFallbackTypeAndVersion() throws Exception {
		HoistMetaData extract = uut.extract(new ObjectNode(new ObjectMapper().getNodeFactory()),
				uut.extract(WithVersionInfoAndType.class));

		assertEquals("foo", extract.getType());
		assertEquals(31, extract.getVersion());
	}

	@Test
	public void testExtractJsonNodeWithFallbackVersion() throws Exception {
		HoistMetaData extract = uut.extract(new ObjectNode(new ObjectMapper().getNodeFactory()),
				uut.extract(WithVersionInfo.class));

		assertEquals("WithVersionInfo", extract.getType());
		assertEquals(21, extract.getVersion());
	}

	@Test
	public void testExtractJsonNodeWithFallbackType() throws Exception {
		HoistMetaData extract = uut.extract(new ObjectNode(new ObjectMapper().getNodeFactory()),
				uut.extract(NoVersionInfo.class));

		assertEquals("NoVersionInfo", extract.getType());
		assertEquals(0, extract.getVersion());
	}

	@Test
	public void testExtractJsonNodeWithVersion() throws Exception {
		ObjectNode doc = new ObjectNode(new ObjectMapper().getNodeFactory());
		ObjectNode meta = new ObjectNode(new ObjectMapper().getNodeFactory());
		meta.put("version", "2");

		doc.set("_hoistmetadata", meta);
		HoistMetaData extract = uut.extract(doc, uut.extract(WithVersionInfoAndType.class));

		assertEquals("foo", extract.getType());
		assertEquals(2, extract.getVersion());
	}

	@Test
	public void testExtractJsonNodeWithType() throws Exception {
		ObjectNode doc = new ObjectNode(new ObjectMapper().getNodeFactory());
		ObjectNode meta = new ObjectNode(new ObjectMapper().getNodeFactory());
		meta.put("type", "test");

		doc.set("_hoistmetadata", meta);
		HoistMetaData extract = uut.extract(doc, uut.extract(WithVersionInfoAndType.class));

		assertEquals("test", extract.getType());
		assertEquals(31, extract.getVersion());
	}

}
