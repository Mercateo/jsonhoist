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
