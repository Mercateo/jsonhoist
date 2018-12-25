package com.mercateo.jsonhoist.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercateo.jsonhoist.HoistMetaDataProcessor;
import com.mercateo.jsonhoist.HoistMetadataProcessorImpl;
import com.mercateo.jsonhoist.HoistObjectMapper;
import com.mercateo.jsonhoist.JsonHoist;
import com.mercateo.jsonhoist.JsonHoistImpl;
import com.mercateo.jsonhoist.trans.JsonTransformationRepository;

/**
 * Spring configuration class for defining defaults for autoconfiguration. Might
 * be extracted out, so that jsonUpcaster is not tied to spring usage.
 *
 * @author usr
 *
 */

@Configuration
public class JsonHoistAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public HoistMetaDataProcessor hoistMetaDataProcessor() {
        return new HoistMetadataProcessorImpl();
    }

    @ConditionalOnMissingBean
    @Bean
    public JsonTransformationRepository transformationRepository() {
        return new JsonTransformationRepository();
    }

    @ConditionalOnMissingBean
    @Bean
    public HoistObjectMapper hoistObjectMapper(ObjectMapper objectMapper, JsonHoist hoist) {
        return new HoistObjectMapper(objectMapper, hoist);
    }

    @ConditionalOnMissingBean
    @Bean
    public JsonHoist jsonHoist(HoistMetaDataProcessor proc, JsonTransformationRepository repo) {
        return new JsonHoistImpl(proc, repo);
    }

}
