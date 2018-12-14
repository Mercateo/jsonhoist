package com.mercateo.jsonhoist.trans;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import lombok.Getter;
import lombok.NonNull;

/**
 * @author nilslindemann
 *
 */
@Getter
public class ResourceUriParser {

    private final String fileName;

    private final String type;

    public ResourceUriParser(@NonNull URI uri) {
        try {
            Path ressourcePath = Paths.get(validateUri(uri));
            fileName = ressourcePath.getFileName().toString();
            type = ressourcePath.getParent().getFileName().toString();

        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String validateUri(URI uri) throws MalformedURLException {
        String externalForm = uri.toURL().toExternalForm();

        int lastIndexOf = externalForm.lastIndexOf('!');
        if (lastIndexOf != -1) {
            externalForm = externalForm.substring(lastIndexOf);
        }

        if (externalForm.chars()
                .mapToObj(i -> (char) i)
                .filter(c -> c.equals('/'))
                .count() < 2) {
            throw new IllegalArgumentException("Cannot determine type from given URI : "
                    + uri.toString());
        }
        return externalForm;
    }
}