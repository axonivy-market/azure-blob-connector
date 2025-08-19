package com.axonivy.connector.azure.blob.internal.helper;

import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class WebTargetHelper {

	public static WebTarget buildWebTarget(final WebTarget webTarget, String path, Map<String, String> queries) {
		WebTarget webTargetWithPath = webTarget;
		if (StringUtils.isNotBlank(path)) {
			webTargetWithPath = webTarget.path(path);
		}

		for (Map.Entry<String, String> query : queries.entrySet()) {
			webTargetWithPath = webTargetWithPath.queryParam(query.getKey(), query.getValue());
		}

		return webTargetWithPath;
	}

	public static String getMediaType(String httpMethod, Object body) {
		if (PUT.equals(httpMethod) || POST.equals(httpMethod)) {
			if (body instanceof Form) {
				return MediaType.APPLICATION_FORM_URLENCODED;
			}

			if (body instanceof byte[] 
					|| body instanceof InputStream 
					|| body instanceof File
					|| body instanceof String) {
				return MediaType.APPLICATION_OCTET_STREAM;
			}
			
			return MediaType.WILDCARD;
		}

		return EMPTY;
	}

	public static String getContentLength(String httpMethod, Object body) {
		if (body instanceof byte[]) {
			return Objects.toString(((byte[]) body).length);
		}

		if (body instanceof String) {
			return Objects.toString(((String) body).length());
		}

		if (body instanceof File) {
			long size = FileUtils.sizeOf((File) body);
			return Objects.toString(size);
		}

		return EMPTY;
	}
}
