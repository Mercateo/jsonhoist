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
package com.mercateo.jsonhoist.trans;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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

	private List<String> getResourceFiles(String path) throws IOException {
		Collection<String> r = ResourceList.getResources(Pattern.compile(pattern));
		return new ArrayList<>(r);
	}

	public static void main(String[] args) throws IOException {
		//
		new ClassPathFilter(".*/jsonhoist/repository/.*/.*\\.js").listRecursive().forEach(System.out::println);

	}
}
