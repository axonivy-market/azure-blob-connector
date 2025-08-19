package com.axonivy.connector.azure.blob.internal.enums;

import java.util.stream.Stream;

public enum DeleteSnapshotsOptionType {
	INCLUDE("include"),

	ONLY("only");

	private final String value;

	DeleteSnapshotsOptionType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static DeleteSnapshotsOptionType fromString(String value) {
		return Stream.of(values()).filter(it -> it.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}
