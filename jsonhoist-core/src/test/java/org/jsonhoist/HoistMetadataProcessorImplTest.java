package org.jsonhoist;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class HoistMetadataProcessorImplTest {

	@BeforeEach
	protected void setUp() throws Exception {
	}

	@Test
	void testNullContracts() throws Exception {
		HoistMetadataProcessorImpl uut = new HoistMetadataProcessorImpl();
		assertThrows(NullPointerException.class, () -> {
			uut.extract(null);
		});
		assertThrows(NullPointerException.class, () -> {
			uut.extract(null, HoistMetaData.of("foo", 1));
		});
		assertThrows(NullPointerException.class, () -> {
			uut.extract(new TextNode("foo"), null);
		});
		assertThrows(NullPointerException.class, () -> {
			JsonNodeFactory f = new JsonNodeFactory(false);
			ObjectNode node = new ObjectNode(f, Collections.emptyMap());
			uut.add(null, node);
		});
		assertThrows(NullPointerException.class, () -> {
			uut.add(HoistMetaData.of("foo", 1), null);
		});
	}
	HoistMetadataProcessorImpl uut = new HoistMetadataProcessorImpl();

	@Test
	public void testNoVersionInfo() {
		HoistMetaData extract = uut.extract(NoVersionInfo.class);

		assertEquals("NoVersionInfo", extract.type());
		assertEquals(0, extract.version());
	}

	@Test
	public void testVersionInfo() {
		assertEquals(21, uut.extract(WithVersionInfo.class).version());
		assertEquals(WithVersionInfo.class.getSimpleName(), uut.extract(WithVersionInfo.class).type());
	}

	@Test
	public void testTypeOverride() {
		assertEquals(31, uut.extract(WithVersionInfoAndType.class).version());
		assertEquals("foo", uut.extract(WithVersionInfoAndType.class).type());
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

		assertEquals("foo", extract.type());
		assertEquals(31, extract.version());
	}

	@Test
	public void testExtractJsonNodeWithFallbackVersion() throws Exception {
		HoistMetaData extract = uut.extract(new ObjectNode(new ObjectMapper().getNodeFactory()),
				uut.extract(WithVersionInfo.class));

		assertEquals("WithVersionInfo", extract.type());
		assertEquals(21, extract.version());
	}

	@Test
	public void testExtractJsonNodeWithFallbackType() throws Exception {
		HoistMetaData extract = uut.extract(new ObjectNode(new ObjectMapper().getNodeFactory()),
				uut.extract(NoVersionInfo.class));

		assertEquals("NoVersionInfo", extract.type());
		assertEquals(0, extract.version());
	}

	@Test
	public void testExtractJsonNodeWithVersion() throws Exception {
		ObjectNode doc = new ObjectNode(new ObjectMapper().getNodeFactory());
		ObjectNode meta = new ObjectNode(new ObjectMapper().getNodeFactory());
		meta.put("version", "2");

		doc.set("_hoistmetadata", meta);
		HoistMetaData extract = uut.extract(doc, uut.extract(WithVersionInfoAndType.class));

		assertEquals("foo", extract.type());
		assertEquals(2, extract.version());
	}

	@Test
	public void testExtractJsonNodeWithType() throws Exception {
		ObjectNode doc = new ObjectNode(new ObjectMapper().getNodeFactory());
		ObjectNode meta = new ObjectNode(new ObjectMapper().getNodeFactory());
		meta.put("type", "test");

		doc.set("_hoistmetadata", meta);
		HoistMetaData extract = uut.extract(doc, uut.extract(WithVersionInfoAndType.class));

		assertEquals("test", extract.type());
		assertEquals(31, extract.version());
	}
}
