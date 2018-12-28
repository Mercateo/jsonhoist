package org.jsonhoist.example;

import org.jsonhoist.HoistVersion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

// there is no mapping for v3
@HoistVersion(type = "TestJson", value = 3)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestJsonV3 {
	private String fullName;
}