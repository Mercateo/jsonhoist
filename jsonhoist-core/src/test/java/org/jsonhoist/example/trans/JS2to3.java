package org.jsonhoist.example.trans;

import org.jsonhoist.trans.JSJsonTransformation;

public class JS2to3 extends JSJsonTransformation {

	public JS2to3() {
		super(JS2to3.class.getResource("/v2_to_v3.js"));
	}
}
