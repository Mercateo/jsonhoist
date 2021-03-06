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
package org.jsonhoist.exc;

import java.io.IOException;

import lombok.NonNull;

/**
 * Generic Exception thrown in the process of upcasting
 *
 * @author usr
 *
 */
public class HoistException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public HoistException(@NonNull Throwable e) {
		super(e);
	}

	public HoistException(@NonNull String string, @NonNull IOException e) {
		super(string, e);
	}

	public HoistException(@NonNull String string) {
		super(string);
	}
}
