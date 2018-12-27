package com.mercateo.jsonhoist.trans;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.Test;

public class ResourceUriParserUnit0Test {

	private final URI jarUri = URI
			.create("jar:file:/Users/foo/bar/foobar.jar!/BOOT-INF/classes!/jsonhoist/repository/SomeType/1-2.js");

	private final URI jarUriNoParent = URI.create("jar:file:/Users/foo/bar/foobar.jar!/BOOT-INF/classes!/1-2.js");

	private final URI fsUri = URI.create("file:/Users/foo/bar/classes/jsonhoist/repository/SomeType/1-2.js");

	private final URI fsUriNoParent = URI.create("file:/1-2.js");

	private final URI mailformedUrl = URI.create("foobar");

	@SuppressWarnings("unused")
	@Test
	public void testNullContract() {
		assertThrows(NullPointerException.class, () -> {
			new ResourceUriParser(null);
		});
	}

	@SuppressWarnings("unused")
	@Test
	public void testFileSystemUri() {
		new ResourceUriParser(fsUri);
	}

	@SuppressWarnings("unused")
	@Test
	public void testMalformedURL() {
		assertThrows(IllegalArgumentException.class, () -> {
			new ResourceUriParser(mailformedUrl);
		});
	}

	@SuppressWarnings("unused")
	@Test
	public void testNoParent() {
		assertThrows(IllegalArgumentException.class, () -> {
			new ResourceUriParser(jarUriNoParent);
		});
	}

	@SuppressWarnings("unused")
	@Test
	public void testFileSystemUriNoParent() {
		assertThrows(IllegalArgumentException.class, () -> {
			new ResourceUriParser(fsUriNoParent);
		});
	}

	@Test
	public void testGetFileName() throws Exception {
		ResourceUriParser uut = new ResourceUriParser(jarUri);
		assertThat(uut.getFileName()).isEqualTo("1-2.js");
	}

	@Test
	public void testGetType() throws Exception {
		ResourceUriParser uut = new ResourceUriParser(jarUri);
		assertThat(uut.getType()).isEqualTo("SomeType");
	}
}