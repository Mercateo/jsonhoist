package org.jsonhoist.example;

import org.jsonhoist.HoistVersion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@HoistVersion(value = 3, type = "foo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeanV3 {

	String name;

	Details details;

}
