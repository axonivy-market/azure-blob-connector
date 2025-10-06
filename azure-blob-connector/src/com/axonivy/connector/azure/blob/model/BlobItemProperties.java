package com.axonivy.connector.azure.blob.model;

import java.io.Serializable;
import java.time.OffsetDateTime;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public final class BlobItemProperties implements Serializable {
	private static final long serialVersionUID = -7644476540643623963L;

	private OffsetDateTime creationTime;
	private OffsetDateTime lastModified;
	private Long contentLength;
	private String contentType;

	public BlobItemProperties(OffsetDateTime creationTime, OffsetDateTime lastModified, Long contentLength,
			String contentType) {
		super();
		this.creationTime = creationTime;
		this.lastModified = lastModified;
		this.contentLength = contentLength;
		this.contentType = contentType;
	}

	public OffsetDateTime getCreationTime() {
		return creationTime;
	}

	public OffsetDateTime getLastModified() {
		return lastModified;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public String getContentType() {
		return contentType;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public static class Builder {
		private OffsetDateTime creationTime;
		private OffsetDateTime lastModified;
		private Long contentLength;
		private String contentType;

		public static Builder builder() {
			return new Builder();
		}

		public Builder creationTime(OffsetDateTime creationTime) {
			this.creationTime = creationTime;
			return this;
		}

		public Builder lastModified(OffsetDateTime lastModified) {
			this.lastModified = lastModified;
			return this;
		}

		public Builder contentLength(Long contentLength) {
			this.contentLength = contentLength;
			return this;
		}

		public Builder contentType(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public BlobItemProperties build() {
			return new BlobItemProperties(creationTime, lastModified, contentLength, contentType);
		}
	}
}
