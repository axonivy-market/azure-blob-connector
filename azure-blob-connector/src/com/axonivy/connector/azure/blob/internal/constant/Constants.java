package com.axonivy.connector.azure.blob.internal.constant;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Constants {
	public static final DateTimeFormatter ISO_8601_UTC_DATE_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT).withZone(ZoneId.of("UTC"));

	public static final DateTimeFormatter RFC_1123_UTC_DATE_FORMATTER = DateTimeFormatter
			.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withZone(ZoneOffset.UTC).withLocale(Locale.US);

	public static final String MS_VERSION = "2025-07-05";
	
	public static final String SLASH = "/";
	public static final String COMMA = ",";
	public static final String COLON = ":";
	public static final String AND_SYMBOL = "&";
	
	private Constants() {
	}
}
