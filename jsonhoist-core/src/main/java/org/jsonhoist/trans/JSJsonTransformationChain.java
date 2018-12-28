/*
 * Copyright © 2018 Mercateo AG (http://www.mercateo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jsonhoist.trans;

import java.util.List;
import java.util.stream.Collectors;

import org.jsonhoist.AbstractJsonTransformation;

import lombok.NonNull;

/**
 * Represents a path of transformations to get from a source version to a target
 * version via only JavaScript based transformation-steps, so that those steps
 * can be executed in one go.
 *
 * Only works for all JS Transformations up to now. This will be extended and is
 * not yet production ready and marked protceted & deprecated until usable.
 *
 * {@link JsonTransformationOptimizer}
 *
 * @author usr
 *
 */
@Deprecated
class JSJsonTransformationChain extends JSJsonTransformation implements JsonTransformationChain {

	private static final String WRAPPER_FUNCTION_PREFIX = "(function (doc) { [";

	private static final String WRAPPER_FUNCTION_SUFFIX = "].forEach( function(s) { s(doc) } ); return doc; })";

	public JSJsonTransformationChain(@NonNull List<AbstractJsonTransformation> transformations) {
		super(generateScript(transformations));
	}

	private static String generateScript(List<AbstractJsonTransformation> transformations) {
		if (transformations.isEmpty()) {
			throw new IllegalArgumentException(
					"There needs to be at least one transformation to build a transformation chain");
		}

		String fns = transformations.stream().map(t -> JSJsonTransformation.class.cast(t))
				.map(JSJsonTransformation::getScriptText).collect(Collectors.joining(","));
		return WRAPPER_FUNCTION_PREFIX + fns + WRAPPER_FUNCTION_SUFFIX;
	}
}
