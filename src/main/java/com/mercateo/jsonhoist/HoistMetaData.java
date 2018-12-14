package com.mercateo.jsonhoist;

import lombok.NonNull;
import lombok.Value;

/**
 * ValueClass holding the necessary MetaData for transformation source or target
 *
 * @author usr
 *
 */
@Value(staticConstructor = "of")
public class HoistMetaData {
    @NonNull
    String type;

    long version;

}
