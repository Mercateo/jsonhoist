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
package org.jsonhoist;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * ValueClass holding the necessary MetaData for transformation source or target
 *
 * @author usr
 *
 */
@Value(staticConstructor = "of")
@Slf4j
public class HoistMetaData {
	@NonNull
	String type;

	long version;

	long minor;

	public static HoistMetaData of(@NonNull String type, long version) {
		return of(type, version, 0);
	}

	public static HoistMetaData of(@NonNull String type, @NonNull String versionString) {
		String[] split = versionString.split("\\.");
		long major = Long.valueOf(split[0]);
		long minor = 0;
		if (split.length == 2)
			minor = Long.valueOf(split[1]);
		else
			log.debug("Interpreting version string {} as {}.{} ", versionString, major,
					minor + " - Please be explicit next time");

		if (major < 0 || minor < 0)
			throw new IllegalArgumentException("Negative version component found");

		return HoistMetaData.of(type, major, minor);
	}

	public String toString() {
		return type + ":" + version + "." + minor;
	}

}
