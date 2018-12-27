package com.mercateo.jsonhoist;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mercateo.jsonhoist.trans.JsonTransformation;
import com.mercateo.jsonhoist.trans.JsonTransformationRepository;

import lombok.RequiredArgsConstructor;

@ExtendWith(MockitoExtension.class)
public class JsonHoistImpl0Test {
	@Mock
	JsonTransformationRepository r;

	@Mock
	HoistMetaDataProcessor e;

	@Test
	public void testUpcastStringJsonMetaData() throws Exception {
		JsonHoistImpl uut = spy(new JsonHoistImpl(e, r));
		HoistMetaData target = HoistMetaData.of("x", 1L);
		when(e.extract(any(JsonNode.class), any())).thenReturn(target);

		uut.transform("{}", target);

		verify(uut).transform(any(JsonNode.class), any());
	}

	@Test
	public void testUpcastJsonNodeJsonMetaData() throws Exception {
		ObjectMapper om = new ObjectMapper();
		JsonHoistImpl uut = spy(new JsonHoistImpl(e, r));

		HoistMetaData from = HoistMetaData.of("x", 1L);
		HoistMetaData to = HoistMetaData.of("x", 3L);

		when(e.extract(any(JsonNode.class), any())).thenReturn(from);
		JsonTransformation t1 = spy(new FakeJsonTransformation("t1"));
		JsonTransformation t2 = spy(new FakeJsonTransformation("t2"));

		when(r.defaultPath(from, to)).thenReturn(Arrays.asList(t1, t2));

		// act
		JsonNode result = uut.transform("{}", to);

		// assert
		verify(r).defaultPath(from, to);
		verify(t1).transform(eq(om.readTree("{}")));
		verify(t2).transform(eq(new TextNode("t1")));

		assertEquals(new TextNode("t2"), result);

	}

	@RequiredArgsConstructor
	class FakeJsonTransformation implements JsonTransformation {
		final String ret;

		@Override
		public JsonNode transform(JsonNode source) {
			return new TextNode(ret);
		}
	}

}
