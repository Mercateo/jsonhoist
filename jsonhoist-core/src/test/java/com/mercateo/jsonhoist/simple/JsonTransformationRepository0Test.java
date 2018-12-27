package com.mercateo.jsonhoist.simple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.mercateo.jsonhoist.HoistMetaData;
import com.mercateo.jsonhoist.exc.HoistException;
import com.mercateo.jsonhoist.trans.JsonTransformation;
import com.mercateo.jsonhoist.trans.JsonTransformationRepository;

import lombok.Value;

public class JsonTransformationRepository0Test {

	private JsonTransformationRepository uut = new JsonTransformationRepository();

	@Test
	public void testSimplePaths() throws Exception {
		uut.register(v(1), v(2), fakeTrans(1));
		uut.register(v(2), v(99), fakeTrans(2)); // dead end
		uut.register(v(2), v(3), fakeTrans(3));
		uut.register(v(3), v(1), fakeTrans(4)); // cycle
		uut.register(v(3), v(4), fakeTrans(5));

		List<JsonTransformation> p = uut.defaultPath(v(1), v(4));
		assertThat(p).isNotEmpty().isNotNull().hasSize(3);

		AnnotatedJsonTransformation step1 = (AnnotatedJsonTransformation) p.get(0);
		AnnotatedJsonTransformation step2 = (AnnotatedJsonTransformation) p.get(1);
		AnnotatedJsonTransformation step3 = (AnnotatedJsonTransformation) p.get(2);

		assertThat(step1.getId()).isEqualTo(1);
		assertThat(step2.getId()).isEqualTo(3);
		assertThat(step3.getId()).isEqualTo(5);

	}

	@Test
	public void testCyclePaths() throws Exception {
		assertThrows(HoistException.class,()->{
			
			uut.register(v(1), v(2), fakeTrans(1));
			uut.register(v(2), v(1), fakeTrans(2)); // cycle
			
			uut.defaultPath(v(1), v(3));
		});
	}

	private JsonTransformation fakeTrans(int i) {
		return new AnnotatedJsonTransformation(i);
	}

	@Value
	static class AnnotatedJsonTransformation implements JsonTransformation {
		int id;

		@Override
		public JsonNode transform(JsonNode source) {
			return source;
		}
	}

	private HoistMetaData v(int i) {
		return HoistMetaData.of("foo", i);
	}

}
