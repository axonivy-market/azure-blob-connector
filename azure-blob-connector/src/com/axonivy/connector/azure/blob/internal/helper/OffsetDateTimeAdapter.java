package com.axonivy.connector.azure.blob.internal.helper;

import java.time.OffsetDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.axonivy.connector.azure.blob.internal.constant.Constants;


public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

	@Override
	public OffsetDateTime unmarshal(String v) throws Exception {
		return OffsetDateTime.parse(v);
	}

	@Override
	public String marshal(OffsetDateTime v) throws Exception {
		return Constants.ISO_8601_UTC_DATE_FORMATTER.format(v);
	}
}
