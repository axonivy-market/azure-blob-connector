package com.axonivy.connector.azure.blob.internal.helper;


import static org.apache.commons.collections4.MapUtils.emptyIfNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.file.Paths;
import java.util.Map;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.ArrayUtils;

public class HttpRequestBuilder {

	private String uri;
	private String httpMethod;
	private Object body;	
	private Map<String, String> headers;
	private Map<String, String> queries;
	private Object[] uriVariables;

	public static HttpRequestBuilder builder() {
		return new HttpRequestBuilder();
	}

	public HttpRequestBuilder uri(String uri) {
		this.uri = uri;
		return this;
	}

	public HttpRequestBuilder httpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
		return this;
	}
	
	public HttpRequestBuilder body(Object body) {
		this.body = body;
		return this;
	}

	public HttpRequestBuilder headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}
	
	public HttpRequestBuilder queries(Map<String, String> queries) {
		this.queries = queries;
		return this;
	}

	public HttpRequestBuilder uriVariables(Object[] uriVariables) {
		this.uriVariables = uriVariables;
		return this;
	}
	
	public HttpRequest build() throws FileNotFoundException {
		HttpRequest.Builder builder = buildHttpRequestBuilder();
		
		HttpRequest request = null;
		switch (httpMethod) {
		case HttpMethod.POST:
		case HttpMethod.PUT:
			request = builder.method(httpMethod, ofBody(body)).build();
			break;
			
		case HttpMethod.GET:		
			request = builder.GET().build();
			break;
			
		case HttpMethod.DELETE:		
			request = builder.DELETE().build();
			break;
		}

		return request;
	}

	private BodyPublisher ofBody(Object body) throws FileNotFoundException {
		if (body == null) {
			return BodyPublishers.noBody();
		}
		
		if (body instanceof byte[]) {
			return BodyPublishers.ofByteArray((byte[]) body);
		}
		
		if (body instanceof File) {
			File content = (File) body;
			return BodyPublishers.ofFile(Paths.get(content.getAbsolutePath()));
		}
		
		if (body instanceof String) {
			return BodyPublishers.ofString((String) body);
		}
		
		throw new RuntimeException("Not support body type " + body.getClass().getName());
	}

	private HttpRequest.Builder buildHttpRequestBuilder() {
		UriBuilder uriBuilder = UriBuilder.fromUri(this.uri);
		
		emptyIfNull(this.queries).entrySet().forEach(query -> {
			uriBuilder.queryParam(query.getKey(),  query.getValue());
		});
				 
		HttpRequest.Builder builder = HttpRequest.newBuilder();
		emptyIfNull(this.headers).entrySet().stream()
		.filter(it -> !HttpHeaders.CONTENT_LENGTH.equals(it.getKey()))
		.forEach(header -> {			
			builder.header(header.getKey(), header.getValue());
		});
		
		URI uri = uriBuilder.build(ArrayUtils.nullToEmpty(this.uriVariables));
		builder.uri(uri).version(HttpClient.Version.HTTP_1_1);
		return builder;
	}
}
