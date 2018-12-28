/**
 * 
 */
package org.jsonhoist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.jsonhoist.trans.JsonTransformation;
import org.jsonhoist.trans.JsonTransformationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import lombok.RequiredArgsConstructor;

/**
 * @author usr
 *
 */
@ExtendWith(MockitoExtension.class)
public class JsonHoistImplTest {
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

	@Test
	void testNullContracts() throws Exception {

		assertThrows(NullPointerException.class, () -> {
			new JsonHoistImpl(null);
		});
		assertThrows(NullPointerException.class, () -> {
			new JsonHoistImpl(mock(HoistMetaDataProcessor.class), null);
		});

		assertThrows(NullPointerException.class, () -> {
			new JsonHoistImpl(null, mock(JsonTransformationRepository.class));
		});

		JsonHoistImpl uut = new JsonHoistImpl(mock(JsonTransformationRepository.class));

		assertThrows(NullPointerException.class, () -> {
			uut.transform((String) null, HoistMetaData.of("foo", 1));
		});
		assertThrows(NullPointerException.class, () -> {
			uut.transform((JsonNode) null, HoistMetaData.of("foo", 1));
		});
		assertThrows(NullPointerException.class, () -> {
			uut.transform("{}", null);
		});
		assertThrows(NullPointerException.class, () -> {
			uut.transform(new TextNode(""), null);
		});
	}
}
