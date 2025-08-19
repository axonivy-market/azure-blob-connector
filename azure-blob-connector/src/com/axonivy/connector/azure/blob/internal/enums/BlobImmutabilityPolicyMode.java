package com.axonivy.connector.azure.blob.internal.enums;

import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlEnumValue;

public enum BlobImmutabilityPolicyMode {
	@XmlEnumValue("Mutable")
	MUTABLE("Mutable"),

	@XmlEnumValue("Unlocked")
	UNLOCKED("Unlocked"),

	@XmlEnumValue("Locked")
	LOCKED("Locked");

	private final String value;

	BlobImmutabilityPolicyMode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static BlobImmutabilityPolicyMode fromString(String value) {
		return Stream.of(values()).filter(it -> it.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}
