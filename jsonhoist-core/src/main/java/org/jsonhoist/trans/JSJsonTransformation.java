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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.jsonhoist.AbstractJsonTransformation;
import org.jsonhoist.exc.HoistException;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * A JavaScript based Transformation, that exposes its script for optimization
 * purposes.
 *
 * This class is Threadsafe.
 *
 * @author usr
 *
 */
public class JSJsonTransformation extends AbstractJsonTransformation {

	public static final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

	private final CompiledScript script;

	private Invocable invocable;

	@NonNull
	@Getter(AccessLevel.PROTECTED)
	private final String scriptText;

	protected JSJsonTransformation(@NonNull String scriptText) {

		this.scriptText = scriptText;

		// on special request:
		ScriptEngine engine = null;
		synchronized (scriptEngineManager) {
			// no guarantee is found anywhere, that creating a scriptEngine was
			// supposed to be threadsafe, so...
			engine = scriptEngineManager.getEngineByName("nashorn");
		}
		Compilable compilable = (Compilable) engine;
		invocable = (Invocable) engine;

		try {
			String wrapperScript = wrapperScript(scriptText);
			script = compilable.compile(wrapperScript);
			script.eval();
		} catch (ScriptException e) {
			throw new HoistException(e);
		}

	}

	public JSJsonTransformation(URL url) {
		this(fetchContent(url));
	}

	private static String fetchContent(URL url) {
		try {
			return copyToString(url.openStream(), Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new HoistException("While fetching js from " + url, e);
		}
	}

	private String wrapperScript(String script) {
		return "exec = " + script;
	}

	@Override
	public JsonNode transform(@NonNull JsonNode source) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonAsMap = om.convertValue(source, Map.class);
			invocable.invokeFunction("exec", jsonAsMap);
			return om.convertValue(jsonAsMap, JsonNode.class);
		} catch (NoSuchMethodException | ScriptException e) {
			throw new HoistException(e);
		}
	}

	public static String copyToString(@NonNull InputStream in, Charset charset) throws IOException {
		StringBuilder out = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(in, charset);
		char[] buffer = new char[1024];
		int bytesRead = -1;
		while ((bytesRead = reader.read(buffer)) != -1) {
			out.append(buffer, 0, bytesRead);
		}
		return out.toString();
	}

}
