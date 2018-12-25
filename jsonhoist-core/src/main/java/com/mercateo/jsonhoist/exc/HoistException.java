package com.mercateo.jsonhoist.exc;

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
