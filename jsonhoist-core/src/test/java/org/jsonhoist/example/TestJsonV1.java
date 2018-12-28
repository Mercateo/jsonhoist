package org.jsonhoist.example;

import org.jsonhoist.HoistVersion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@HoistVersion(type = "TestJson", value = 1)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestJsonV1 {
	private String firstName;

	private String lastName;
}