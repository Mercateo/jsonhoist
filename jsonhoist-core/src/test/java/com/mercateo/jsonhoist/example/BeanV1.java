package com.mercateo.jsonhoist.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mercateo.jsonhoist.HoistVersion;

import lombok.Value;

@Value
@HoistVersion(value = 1, type = "foo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeanV1 {
	String firstName;

	String lastName;
}
