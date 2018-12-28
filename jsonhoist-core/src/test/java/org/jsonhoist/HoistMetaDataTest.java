package org.jsonhoist;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class HoistMetaDataTest {
	@Test
	public void testNullContract() throws Exception {
		assertThrows(NullPointerException.class, () -> {
			HoistMetaData.of(null, 1L);
		});
	}
}
