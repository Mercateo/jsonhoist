package org.jsonhoist.simple;

import static org.junit.jupiter.api.Assertions.*;

import org.jsonhoist.HoistMetaData;
import org.jsonhoist.trans.ClassPathJSJsonTransformationLoader;
import org.jsonhoist.trans.JsonTransformationRepository;
import org.junit.jupiter.api.Test;

/**
 * makes sure, minor handling is correct.
 *
 */
public class JsonTransformationRepositoryMinorTest {

	private JsonTransformationRepository uut = new JsonTransformationRepository();

	@Test
	public void testSimplePaths() throws Exception {

		new ClassPathJSJsonTransformationLoader(".*/loadertest/2/.*\\.js", uut).load();

		assertNotNull(uut.defaultPath(HoistMetaData.of("testtype", 1), HoistMetaData.of("testtype", 2)));
		assertNotNull(uut.defaultPath(HoistMetaData.of("testtype", 2), HoistMetaData.of("testtype", 3)));

	}

	@Test
	public void testImplicitDowncast() throws Exception {

		new ClassPathJSJsonTransformationLoader(".*/loadertest/2/.*\\.js", uut).load();

		assertNotNull(uut.defaultPath(HoistMetaData.of("testtype", 1, 3), HoistMetaData.of("testtype", 2)));

	}

}
