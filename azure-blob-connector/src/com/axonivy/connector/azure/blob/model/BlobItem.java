package com.axonivy.connector.azure.blob.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class BlobItem implements Serializable {
	private static final long serialVersionUID = 1679254314885513274L;

	private String name;
	private BlobItemProperties properties;
	private String url;
	private boolean deleted;

	private BlobItem(String name, BlobItemProperties properties, String url, boolean deleted) {
		super();
		this.name = name;
		this.properties = properties;
		this.url = url;
		this.deleted = deleted;
	}

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public BlobItemProperties getProperties() {
		return properties;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static class Builder {
		private String name;
		private BlobItemProperties properties;
		private String url;
		private boolean isDeleted;

		public static Builder builder() {
			return new Builder();
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder properties(BlobItemProperties properties) {
			this.properties = properties;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder deleted(boolean isDeleted) {
			this.isDeleted = isDeleted;
			return this;
		}

		public BlobItem build() {
			return new BlobItem(name, properties, url, isDeleted);
		}
	}
}
