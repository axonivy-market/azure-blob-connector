package com.axonivy.connector.azure.blob.internal.enums;

import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlEnumValue;

public enum LeaseDurationType {
	@XmlEnumValue("infinite")
	INFINITE("infinite"),

	@XmlEnumValue("fixed")
	FIXED("fixed");

	private final String value;

	LeaseDurationType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static LeaseDurationType fromString(String value) {
		return Stream.of(values()).filter(it -> it.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}
