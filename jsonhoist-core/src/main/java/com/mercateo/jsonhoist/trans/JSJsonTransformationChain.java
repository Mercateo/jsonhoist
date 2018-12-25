package com.mercateo.jsonhoist.trans;

import java.util.List;
import java.util.stream.Collectors;

import com.mercateo.jsonhoist.AbstractJsonTransformation;

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

    public JSJsonTransformationChain(
            @NonNull List<AbstractJsonTransformation> transformations) {
        super(generateScript(transformations));
    }

    private static String generateScript(List<AbstractJsonTransformation> transformations) {
        if (transformations.isEmpty()) {
            throw new IllegalArgumentException(
                    "There needs to be at least one transformation to build a transformation chain");
        }

        String fns = transformations.stream()
                .map(t -> JSJsonTransformation.class.cast(t))
                .map(JSJsonTransformation::getScriptText)
                .collect(Collectors.joining(","));
        return WRAPPER_FUNCTION_PREFIX + fns + WRAPPER_FUNCTION_SUFFIX;
    }
}
