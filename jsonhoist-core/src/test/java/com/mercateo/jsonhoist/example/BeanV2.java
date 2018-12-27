package com.mercateo.jsonhoist.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mercateo.jsonhoist.HoistVersion;

import lombok.Getter;

@Getter
@HoistVersion(value = 2, type = "foo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeanV2 {
	String firstName;

	String lastName;

	String fullName;

	int age = -1;
}
