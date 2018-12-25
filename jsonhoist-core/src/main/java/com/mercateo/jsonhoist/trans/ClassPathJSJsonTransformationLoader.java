package com.mercateo.jsonhoist.trans;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.mercateo.jsonhoist.HoistMetaData;

import lombok.NonNull;

/**
 * Impl of TransformationRepository, that loads transformation scripts from a
 * well-known location of the classpath:
 * <p>
 * <code>classpath:/jsonupcaster/repository/TYPE_NAME/FROM-TO.js</code> <br>
 * <p>
 * e.g.:
 * <p>
 * <code>classpath:/jsonupcaster/repository/FooBar/0-1.js</code>
 *
 * @author usr
 */
@Component
public class ClassPathJSJsonTransformationLoader {

    @NonNull
    private String pattern;

    @NonNull
    private JsonTransformationRepository repo;

    public ClassPathJSJsonTransformationLoader(@NonNull JsonTransformationRepository repo) {
        this("classpath:/jsonhoist/repository/**/*.js", repo);
    }

    public ClassPathJSJsonTransformationLoader(@NonNull String pattern,
            @NonNull JsonTransformationRepository repo) {
        this.pattern = pattern;
        this.repo = repo;
    }

    public JsonTransformationRepository load() throws IOException {
        PathMatchingResourcePatternResolver r = new PathMatchingResourcePatternResolver();
        Arrays.stream(r.getResources(pattern))
                .forEach(res -> addResource(res, repo));
        return repo;
    }

    void addResource(Resource r, JsonTransformationRepository repo) {
        try {

            if (r != null) {
                ResourceUriParser p = new ResourceUriParser(r.getURI());
                String type = p.getType();
                String name = p.getFileName();
                name = name.substring(0, name.indexOf("."));
                String[] split = name.split("-");
                if (split.length != 2) {
                    throw new IllegalStateException("Cannot parse filename " + r.getFilename());
                }

                HoistMetaData from = HoistMetaData.of(type, Long.valueOf(split[0]));
                HoistMetaData to = HoistMetaData.of(type, Long.valueOf(split[1]));

                JSJsonTransformation transformation = new JSJsonTransformation(r.getURL());
                repo.register(from, to, transformation);

            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot get file " + r.getFilename(), e);
        }
    }
}
