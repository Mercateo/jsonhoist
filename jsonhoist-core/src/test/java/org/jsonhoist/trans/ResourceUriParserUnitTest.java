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
package org.jsonhoist.trans;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

import org.junit.jupiter.api.Test;

public class ResourceUriParserUnitTest {

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
		assertThat(uut.fileName()).isEqualTo("1-2.js");
	}

	@Test
	public void testGetType() throws Exception {
		ResourceUriParser uut = new ResourceUriParser(jarUri);
		assertThat(uut.type()).isEqualTo("SomeType");
	}
}