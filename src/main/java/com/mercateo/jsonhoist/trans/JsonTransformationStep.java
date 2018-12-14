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
