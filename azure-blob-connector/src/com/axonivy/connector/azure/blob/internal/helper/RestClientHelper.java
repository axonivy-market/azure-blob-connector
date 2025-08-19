package com.axonivy.connector.azure.blob.internal.helper;

import static org.apache.commons.collections4.MapUtils.emptyIfNull;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.ArrayUtils;

public class RestClientHelper {
	public static URI createURI(String path, Map<String, String> queries, Object... uriVariables) {
		UriBuilder uriBuilder = UriBuilder.fromPath(path);
		emptyIfNull(queries).entrySet().forEach(query -> {
			uriBuilder.queryParam(query.getKey(), query.getValue());
		});

		URI uri = uriBuilder.build(ArrayUtils.nullToEmpty(uriVariables));
		return uri;
	}

	public static String getMediaType(Object body) {
		if (body instanceof Form) {
			return MediaType.APPLICATION_FORM_URLENCODED;
		}

		if (body instanceof byte[] || body instanceof InputStream) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}

		return MediaType.APPLICATION_JSON;
	}
}
