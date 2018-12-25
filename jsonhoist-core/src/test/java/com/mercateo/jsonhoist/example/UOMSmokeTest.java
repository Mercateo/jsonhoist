package com.mercateo.jsonhoist.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercateo.jsonhoist.HoistMetaData;
import com.mercateo.jsonhoist.HoistMetaDataProcessor;
import com.mercateo.jsonhoist.HoistMetadataProcessorImpl;
import com.mercateo.jsonhoist.HoistObjectMapper;
import com.mercateo.jsonhoist.JsonHoistImpl;
import com.mercateo.jsonhoist.example.trans.JS1to2;
import com.mercateo.jsonhoist.example.trans.JS2to3;
import com.mercateo.jsonhoist.example.trans.Java1to2;
import com.mercateo.jsonhoist.example.trans.Java2to3;
import com.mercateo.jsonhoist.trans.JsonTransformationRepository;

public class UOMSmokeTest {

    JsonTransformationRepository repo = new JsonTransformationRepository();

    HoistObjectMapper uut;

    @Before
    public void setup() {
        ObjectMapper om = new ObjectMapper();
        HoistMetaDataProcessor p = new HoistMetadataProcessorImpl();

        uut = new HoistObjectMapper(om, new JsonHoistImpl(
                p, repo));

    }

    @Test
    public void testSimpleJSRoundtrip() throws JSONException, IOException {

        repo.register(HoistMetaData.of("foo", 1), HoistMetaData.of("foo", 2), new JS1to2());
        repo.register(HoistMetaData.of("foo", 2), HoistMetaData.of("foo", 3), new JS2to3());

        BeanV1 v1 = new BeanV1("uwe", "schaefer");
        String json = uut.writeValue(v1);

        System.out.println("v1=" + json);

        BeanV2 v2 = uut.readValue(json, BeanV2.class);

        assertNotNull(v2);
        assertEquals("uwe schaefer", v2.getFullName());
        assertEquals(999, v2.getAge());
        json = uut.writeValue(v2);

        System.out.println("v2=" + json);

        BeanV3 v3 = uut.readValue(json, BeanV3.class);

        assertNotNull(v3);
        assertEquals("uwe schaefer", v3.getName());
        assertEquals("uwe", v3.getDetails().getFirst());
        assertEquals("schaefer", v3.getDetails().getLast());
        assertEquals(999, v3.getDetails().getAge());

        System.out.println("v3=" + uut.writeValue(v3));
    }

    @Test
    public void testSimpleJSTwoInOne() throws JSONException, IOException {

        System.out.println("JS two in one go");

        repo.register(HoistMetaData.of("foo", 1), HoistMetaData.of("foo", 2), new JS1to2());
        repo.register(HoistMetaData.of("foo", 2), HoistMetaData.of("foo", 3), new JS2to3());

        String v1Json = "{\"firstName\":\"uwe\",\"lastName\":\"schaefer\",\"_hoistmetadata\":{\"type\":\"foo\",\"version\":1}}";
        BeanV3 v3 = uut.readValue(v1Json, BeanV3.class);

        assertNotNull(v3);
        assertEquals("uwe schaefer", v3.getName());
        assertEquals("uwe", v3.getDetails().getFirst());
        assertEquals("schaefer", v3.getDetails().getLast());
        assertEquals(999, v3.getDetails().getAge());

        System.out.println("v3=" + uut.writeValue(v3));
    }

    @Test
    public void testSimpleJavaRoundtrip() throws JSONException, IOException {

        System.out.println("Java");

        repo.register(HoistMetaData.of("foo", 1), HoistMetaData.of("foo", 2), new Java1to2());
        repo.register(HoistMetaData.of("foo", 2), HoistMetaData.of("foo", 3), new Java2to3());

        BeanV1 v1 = new BeanV1("uwe", "schaefer");
        String json = uut.writeValue(v1);

        System.out.println("v1=" + json);

        BeanV2 v2 = uut.readValue(json, BeanV2.class);

        assertNotNull(v2);
        assertEquals("uwe schaefer", v2.getFullName());
        assertEquals(999, v2.getAge());
        json = uut.writeValue(v2);

        System.out.println("v2=" + json);

        BeanV3 v3 = uut.readValue(json, BeanV3.class);

        assertNotNull(v3);
        assertEquals("uwe schaefer", v3.getName());
        assertEquals("uwe", v3.getDetails().getFirst());
        assertEquals("schaefer", v3.getDetails().getLast());
        assertEquals(999, v3.getDetails().getAge());

        System.out.println("v3=" + uut.writeValue(v3));

    }

}
