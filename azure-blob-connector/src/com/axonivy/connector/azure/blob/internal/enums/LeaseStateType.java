package com.axonivy.connector.azure.blob.internal.enums;

import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlEnumValue;

public enum LeaseStateType {
	@XmlEnumValue("available")
	AVAILABLE("available"),

	@XmlEnumValue("leased")
	LEASED("leased"),

	@XmlEnumValue("expired")
	EXPIRED("expired"),

	@XmlEnumValue("breaking")
	BREAKING("breaking"),

	@XmlEnumValue("broken")
	BROKEN("broken");

	/** The actual serialized value for a LeaseStateType instance. */
	private final String value;

	LeaseStateType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static LeaseStateType fromString(String value) {
		return Stream.of(values()).filter(it -> it.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}
