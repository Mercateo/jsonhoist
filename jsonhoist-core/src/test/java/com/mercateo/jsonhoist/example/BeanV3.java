package com.mercateo.jsonhoist.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mercateo.jsonhoist.HoistVersion;

import lombok.Getter;

@Getter
@HoistVersion(value = 3, type = "foo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeanV3 {

	String name;

	Details details;

}
