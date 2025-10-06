package com.axonivy.connector.azure.blob.internal.enums;

import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlEnumValue;

public enum LeaseStatusType {
	@XmlEnumValue("locked")
	LOCKED("locked"),

	@XmlEnumValue("unlocked")
	UNLOCKED("unlocked");

	private final String value;

	LeaseStatusType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static LeaseStatusType fromString(String value) {
		return Stream.of(values()).filter(it -> it.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}
