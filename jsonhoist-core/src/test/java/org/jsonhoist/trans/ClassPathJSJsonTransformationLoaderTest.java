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
