package com.mercateo.jsonhoist.trans;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mercateo.jsonhoist.HoistMetaData;

@RunWith(MockitoJUnitRunner.class)
public class ClassPathJSJsonTransformationLoader0Test {

    @Mock
    private JsonTransformationRepository mockRepo;

    @Test
    public void testRoundTrip() throws Exception {
        ClassPathJSJsonTransformationLoader uut = new ClassPathJSJsonTransformationLoader(
                "classpath:loadertest/1/**/*.js", mockRepo);

        uut.load();

        verify(mockRepo).register(eq(HoistMetaData.of("testtype", 1)), eq(HoistMetaData.of(
                "testtype", 2)), any());
        verify(mockRepo).register(eq(HoistMetaData.of("testtype", 2)), eq(HoistMetaData.of(
                "testtype", 3)), any());
        verifyNoMoreInteractions(mockRepo);

    }

    @Test
    public void testLoad() throws Exception {
        JsonTransformationRepository emptyRepo = new JsonTransformationRepository();
        ClassPathJSJsonTransformationLoader uut = new ClassPathJSJsonTransformationLoader(
                "classpath:loadertest/1/**/*.js", emptyRepo);

        JsonTransformationRepository repo = uut.load();

        assertThat(repo).isSameAs(emptyRepo);
    }

}
