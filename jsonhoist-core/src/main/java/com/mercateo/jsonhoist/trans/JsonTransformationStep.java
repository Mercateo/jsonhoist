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

import com.mercateo.jsonhoist.HoistMetaData;

import lombok.Value;

// TODO recheck if needed
/**
 * Single transformation step, defined by a target to which it transforms to,
 * and the actual transformation necessary to get to the target.
 *
 * @author usr
 *
 */
@Value
class JsonTransformationStep {
	HoistMetaData target;

	JsonTransformation transformation;
}
