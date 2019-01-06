package org.jsonhoist.simple;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.jsonhoist.HoistMetaData;
import org.jsonhoist.exc.HoistException;
import org.jsonhoist.trans.ClassPathJSJsonTransformationLoader;
import org.jsonhoist.trans.JsonTransformation;
import org.jsonhoist.trans.JsonTransformationRepository;
import org.junit.jupiter.api.Test;

import lombok.NonNull;

/**
 * makes sure, minor handling is correct.
 *
 */
public class JsonTransformationRepositoryMinorTest {

	private static final @NonNull String TYPE = "foo";
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

	@Test
	public void testAvoidUnnecessaryImplicitDowncast() throws Exception {

		JsonTransformation a = mock(JsonTransformation.class);
		JsonTransformation b = mock(JsonTransformation.class);
		JsonTransformation c = mock(JsonTransformation.class);

		uut.register(HoistMetaData.of(TYPE, 1, 1), HoistMetaData.of(TYPE, 2, 0), a);
		uut.register(HoistMetaData.of(TYPE, 1, 2), HoistMetaData.of(TYPE, 2, 0), b);
		uut.register(HoistMetaData.of(TYPE, 1, 3), HoistMetaData.of(TYPE, 3, 0), c);

		List<JsonTransformation> path = uut.defaultPath(HoistMetaData.of(TYPE, 1, 4), HoistMetaData.of(TYPE, 2));
		assertNotNull(path);
		assertTrue(path.contains(b));
		assertFalse(path.contains(a));
		assertFalse(path.contains(c));
		assertEquals(1, path.size());
	}

	@Test
	public void testIssue9ScenarioA() throws Exception {

		JsonTransformation a = mock(JsonTransformation.class);

		uut.register(HoistMetaData.of(TYPE, 3, 1), HoistMetaData.of(TYPE, 4, 0), a);

		List<JsonTransformation> path = uut.defaultPath(HoistMetaData.of(TYPE, 3, 2), HoistMetaData.of(TYPE, 4));
		assertNotNull(path);
		assertEquals(1, path.size());
		assertTrue(path.contains(a));
	}

	@Test
	public void testIssue9ScenarioB() throws Exception {

		JsonTransformation a = mock(JsonTransformation.class);

		uut.register(HoistMetaData.of(TYPE, 3, 1), HoistMetaData.of(TYPE, 4, 0), a);

		List<JsonTransformation> path = uut.defaultPath(HoistMetaData.of(TYPE, 3, 0), HoistMetaData.of(TYPE, 4));
		assertNotNull(path);
		assertEquals(1, path.size());
		assertTrue(path.contains(a));
	}

	@Test
	public void testIssue9ScenarioC() throws Exception {

		JsonTransformation a = mock(JsonTransformation.class);

		uut.register(HoistMetaData.of(TYPE, 3, 1), HoistMetaData.of(TYPE, 4, 0), a);

		assertThrows(HoistException.class, () -> {
			uut.defaultPath(HoistMetaData.of(TYPE, 3, 0), HoistMetaData.of(TYPE, 5));
		});
	}
}
