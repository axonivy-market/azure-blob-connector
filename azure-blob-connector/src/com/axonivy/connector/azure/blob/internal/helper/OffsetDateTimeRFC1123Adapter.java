package com.axonivy.connector.azure.blob.internal.helper;

import java.time.OffsetDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.axonivy.connector.azure.blob.internal.constant.Constants;


public class OffsetDateTimeRFC1123Adapter extends XmlAdapter<String, OffsetDateTime> {

	@Override
	public OffsetDateTime unmarshal(String v) throws Exception {
		return OffsetDateTime.parse(v, Constants.RFC_1123_UTC_DATE_FORMATTER);
	}

	@Override
	public String marshal(OffsetDateTime v) throws Exception {
		return Constants.RFC_1123_UTC_DATE_FORMATTER.format(v);
	}
}
