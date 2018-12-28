package org.jsonhoist.example;

import org.jsonhoist.HoistVersion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

// upcast here:
// /content-json-upcaster/src/test/resources/jsonhoist/repository/TestJson/1-2.js
@HoistVersion(type = "TestJson", value = 2)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestJsonV2 {
	private String fullName;
}