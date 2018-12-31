package org.jsonhoist.trans;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URISyntaxException;
import java.net.URL;

import org.junit.jupiter.api.Test;

public class ClassPathResourceTest {
	private URL url = mock(URL.class);
	private ClassPathResource uut;

	@Test
	public void translatesURISyntaxException() throws Exception {
		uut = new ClassPathResource(url);
		when(url.toURI()).thenThrow(URISyntaxException.class);

		assertThrows(IllegalStateException.class, () -> {
			uut.getURI();
		});
	}

	/**
	 * Test method for {@link org.jsonhoist.trans.ClassPathResource#getFilename()}.
	 */
	@Test
	public void testGetFilename() throws Exception {
		uut = new ClassPathResource(getClass().getResource("/loadertest/1/testtype/1-2.js"));

		assertEquals("1-2.js", uut.getFilename());

	}

}
