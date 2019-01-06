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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsonhoist.HoistMetaData;
import org.jsonhoist.exc.HoistException;

import lombok.NonNull;

/**
 * Contains, or gives access to all known transformations
 *
 * @author usr
 *
 */
public class JsonTransformationRepository {

	private final Map<HoistMetaData, List<JsonTransformationStep>> graph = Collections.synchronizedMap(new HashMap<>());

	public List<JsonTransformation> defaultPath(@NonNull HoistMetaData from, @NonNull HoistMetaData to) {
		assertSameType(from, to);

		Optional<List<JsonTransformationStep>> path = path(new LinkedList<>(), from, to);
		if (!path.isPresent()) {
			throw new HoistException("Cannot hoist from " + from + " to " + to);
		}
		return path.get().stream().map(JsonTransformationStep::transformation)
				.filter(t -> !(t instanceof NOPTransformation)).collect(Collectors.toList());
	}

	private Optional<List<JsonTransformationStep>> path(@NonNull List<JsonTransformationStep> journey,
			@NonNull HoistMetaData from, @NonNull HoistMetaData to) {

		if (from.equals(to)) {
			return Optional.of(journey);
		}

		Optional<List<JsonTransformationStep>> options = calculateOptions(from);

		if (options.isPresent()) {
			for (JsonTransformationStep jsonTransformationStep : options.get()) {
				if (!alreadyVisited(journey, jsonTransformationStep)) {
					HoistMetaData transformationTarget = jsonTransformationStep.target();
					List<JsonTransformationStep> newPossibleJourney = new LinkedList<>(journey);
					newPossibleJourney.add(jsonTransformationStep);

					Optional<List<JsonTransformationStep>> possiblePath = path(newPossibleJourney, transformationTarget,
							to);
					if (possiblePath.isPresent()) {
						// TODO first wins
						return possiblePath;
					}
				}
			}
		}
		return Optional.empty();
	}

	// Get all possible transformation steps (upgrades as well as
	// downgrades,
	// possibly containing steps that transform across multiple versions),
	// starting from version "from".
	private Optional<List<JsonTransformationStep>> calculateOptions(HoistMetaData from) {

		List<JsonTransformationStep> result = new LinkedList<>();

		// edges as registered
		result.addAll(graph.getOrDefault(from, Collections.emptyList()));

		Set<HoistMetaData> targets = result.stream().map(JsonTransformationStep::target).collect(Collectors.toSet());

		List<JsonTransformationStep> casts = new LinkedList<>();
		// collect and add all possible edges within same version via up/down-casting
		graph.keySet().stream().filter(k -> k.isCompatible(from)).filter(k -> !targets.contains(k)).forEach(k -> {
			casts.add(new JsonTransformationStep(k, NOPTransformation.INSTANCE));
		});
		sortByDistanceFrom(from, casts);

		result.addAll(casts);

		if (result.isEmpty())
			return Optional.empty();
		else {
			return Optional.ofNullable(result);
		}
	}

	protected boolean alreadyVisited(List<JsonTransformationStep> journey,
			JsonTransformationStep jsonTransformationStep) {
		return journey.stream().map(JsonTransformationStep::target).filter(jsonTransformationStep.target()::equals)
				.findAny().isPresent();
	}

	private void assertSameType(HoistMetaData from, HoistMetaData to) {
		String fromType = from.type();
		String toType = to.type();

		if (!fromType.equals(toType)) {
			throw new IllegalArgumentException(
					"SimpleJsonTransformationRepository can only handle transformations within the same type.");
		}
	}

	public void register(@NonNull HoistMetaData from, @NonNull HoistMetaData to,
			@NonNull JsonTransformation transformation) {
		assertSameType(from, to);
		// as this is a operation which has to be synchronized with every other
		// structure changing methods of graph (e.g. putting, removing etc.)
		synchronized (graph) {
			List<JsonTransformationStep> list = graph.get(from);
			if (list == null) {
				list = Collections.synchronizedList(new LinkedList<>());
				graph.put(from, list);
			}
			list.add(new JsonTransformationStep(to, transformation));
			sortByDistanceFrom(from, list);
		}
	}

	private void sortByDistanceFrom(HoistMetaData from, List<JsonTransformationStep> list) {
		// sort by smallest distance
		Collections.sort(list, new Comparator<JsonTransformationStep>() {

			@Override
			public int compare(JsonTransformationStep o1, JsonTransformationStep o2) {
				long d1 = from.distance(o1.target());
				long d2 = from.distance(o2.target());
				return Long.valueOf(d1).compareTo(d2);
			}
		});
	}

}
