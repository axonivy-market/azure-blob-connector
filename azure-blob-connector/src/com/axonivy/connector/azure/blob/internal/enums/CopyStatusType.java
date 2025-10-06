package com.axonivy.connector.azure.blob.internal.enums;

import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlEnumValue;

public enum CopyStatusType {
	@XmlEnumValue("pending")
	PENDING("pending"),

	@XmlEnumValue("success")
	SUCCESS("success"),

	@XmlEnumValue("aborted")
	ABORTED("aborted"),

	@XmlEnumValue("failed")
	FAILED("failed");

	private final String value;

	CopyStatusType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static CopyStatusType fromString(String value) {
		return Stream.of(values()).filter(it -> it.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}
