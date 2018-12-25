package com.mercateo.jsonhoist;

import org.junit.Test;

import com.mercateo.jsonhoist.HoistMetaData;

public class HoistMetaData0Test {
    @Test(expected = NullPointerException.class)
    public void testNullContract() throws Exception {
        HoistMetaData.of(null, 1L);
    }
}
