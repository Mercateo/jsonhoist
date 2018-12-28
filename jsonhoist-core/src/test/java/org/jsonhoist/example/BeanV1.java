package org.jsonhoist.example;

import org.jsonhoist.HoistVersion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@HoistVersion(value = 1, type = "foo")
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(fluent=false)
public class BeanV1 {
	String firstName;

	String lastName;
}
