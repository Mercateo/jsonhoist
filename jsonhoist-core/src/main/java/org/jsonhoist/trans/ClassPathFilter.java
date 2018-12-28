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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class ClassPathFilter {
	@NonNull
	final String pattern;

	public List<ClassPathResource> listRecursive() throws IOException {
		return ResourceList.getResources(Pattern.compile(pattern)).stream().map(this::toClassPathResource)
				.collect(Collectors.toList());

	}

	public ClassPathResource toClassPathResource(String s) {
		try {
			return new ClassPathResource(new File(s).toURL());
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

}
