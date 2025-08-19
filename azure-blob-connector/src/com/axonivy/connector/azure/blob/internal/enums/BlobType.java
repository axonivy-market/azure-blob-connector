package com.axonivy.connector.azure.blob.internal.enums;

import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlEnumValue;

public enum BlobType {
	@XmlEnumValue("BlockBlob")
	BLOCK_BLOB("BlockBlob"),

	@XmlEnumValue("PageBlob")
	PAGE_BLOB("PageBlob"),

	@XmlEnumValue("AppendBlob")
	APPEND_BLOB("AppendBlob");

	private final String value;

	BlobType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static BlobType fromString(String value) {
		return Stream.of(values()).filter(it -> it.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}
