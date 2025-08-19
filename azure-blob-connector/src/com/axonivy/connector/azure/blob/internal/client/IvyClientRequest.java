package com.axonivy.connector.azure.blob.internal.client;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.WebTarget;

import com.axonivy.connector.azure.blob.internal.helper.WebTargetHelper;

public class IvyClientRequest {
	private String path;
	private String httpMethod;
	private Map<String, String> headers;
	private Map<String, String> queries;
	private Object body;

	private IvyClientRequest(Builder builder) {
		this.path = builder.path;
		this.httpMethod = builder.httpMethod;
		this.headers = builder.headers;
		this.queries = builder.queries;
		this.body = builder.body;
	}

	public String getPath() {
		return path;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public Map<String, String> getQueries() {
		return queries;
	}

	public Object getBody() {
		return body;
	}

	public WebTarget getWebTarget(WebTarget root) {
		return WebTargetHelper.buildWebTarget(root, path, queries);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	// Builder
	public static class Builder {
		private String path;
		private String httpMethod;
		private Map<String, String> headers = new HashMap<>();
		private Map<String, String> queries = new HashMap<>();
		private Object body;

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Builder httpMethod(String httpMethod) {
			this.httpMethod = httpMethod;
			return this;
		}

		public Builder header(String key, String value) {
			this.headers.put(key, value);
			return this;
		}

		public Builder headers(Map<String, String> headers) {
			this.headers.putAll(headers);
			return this;
		}

		public Builder query(String key, String value) {
			this.queries.put(key, value);
			return this;
		}

		public Builder queries(Map<String, String> queries) {
			this.queries.putAll(queries);
			return this;
		}

		public Builder body(Object body) {
			this.body = body;
			return this;
		}

		public IvyClientRequest build() {
			return new IvyClientRequest(this);
		}
	}
}
