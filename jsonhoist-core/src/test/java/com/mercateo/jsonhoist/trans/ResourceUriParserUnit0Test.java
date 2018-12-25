package com.mercateo.jsonhoist.trans;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.Test;

public class ResourceUriParserUnit0Test {

    private final URI jarUri = URI.create(
            "jar:file:/Users/foo/bar/foobar.jar!/BOOT-INF/classes!/jsonhoist/repository/SomeType/1-2.js");

    private final URI jarUriNoParent = URI.create(
            "jar:file:/Users/foo/bar/foobar.jar!/BOOT-INF/classes!/1-2.js");

    private final URI fsUri = URI.create(
            "file:/Users/foo/bar/classes/jsonhoist/repository/SomeType/1-2.js");

    private final URI fsUriNoParent = URI.create("file:/1-2.js");

    private final URI mailformedUrl = URI.create("foobar");

    @SuppressWarnings("unused")
    @Test(expected = NullPointerException.class)
    public void testNullContract() {
        new ResourceUriParser(null);
    }

    @SuppressWarnings("unused")
    @Test
    public void testFileSystemUri() {
        new ResourceUriParser(fsUri);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testMalformedURL() {
        new ResourceUriParser(mailformedUrl);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testNoParent() {
        new ResourceUriParser(jarUriNoParent);
    }

    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public void testFileSystemUriNoParent() {
        new ResourceUriParser(fsUriNoParent);
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