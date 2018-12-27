package com.mercateo.jsonhoist;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class HoistMetaData0Test {
	@Test
	public void testNullContract() throws Exception {
		assertThrows(NullPointerException.class, () -> {
			HoistMetaData.of(null, 1L);
		});
	}
}
