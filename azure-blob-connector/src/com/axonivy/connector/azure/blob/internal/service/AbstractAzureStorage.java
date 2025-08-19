package com.axonivy.connector.azure.blob.internal.service;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;

import com.axonivy.connector.azure.blob.internal.auth.AzureNamedKeyCredential;
import com.axonivy.connector.azure.blob.internal.auth.ClientSecretCredential;
import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.internal.auth.StorageShareKeyAuthorizationManager;
import com.axonivy.connector.azure.blob.internal.auth.StorageTokenAuthorizationManager;
import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.constant.MSHeader;
import com.axonivy.connector.azure.blob.internal.enums.BlobType;
import com.axonivy.connector.azure.blob.internal.helper.JAXBHelper;

abstract class AbstractAzureStorage {

	private static final String DOWNLOAD_ERROR_MGS = "The downloading blob data %s is error. HttpStatus %s";
	private static final String UPLOAD_ERROR_MGS = "The uploading blob data %s is error. HttpStatus %s";
	private static final String GET_BLOB_ERROR_MGS = "The getting blob %s is error. HttpStatus %s";

	protected String endpoint;
	protected String container;

	protected String getBlobPath(String fileName) {		
		return String.format("%s/%s/%s", this.endpoint, this.container, fileName);
	}
	
	protected Map<String, String> createUploadHeader(long length, boolean isOverwrite) {
		Map<String, String> headers = new HashMap<>();
		headers.put(MSHeader.X_MS_BLOB_TYPE, BlobType.BLOCK_BLOB.getValue());
		headers.put(HttpHeaders.CONTENT_LENGTH, Long.toString(length));

		if (!isOverwrite) {
			headers.put(HttpHeaders.IF_NONE_MATCH, MediaType.MEDIA_TYPE_WILDCARD);
		}

		return headers;
	}

	protected boolean isHttpCreated(HttpResponse<?> response) {
		return response.statusCode() == HttpStatus.SC_CREATED;
	}

	protected boolean isHttpOk(HttpResponse<?> response) {
		return response.statusCode() == HttpStatus.SC_OK;
	}

	protected boolean isHttpAccepted(HttpResponse<?> response) {
		return response.statusCode() == HttpStatus.SC_ACCEPTED;
	}

	protected <T> T getBody(HttpResponse<?> response, Class<T> type) {
		if (response.statusCode() == HttpStatus.SC_OK) {
			Object body = response.body();
			if (body instanceof String) {
				T result = JAXBHelper.unmarshal((String) body, type);
				return result;
			}
		}
		return null;
	}

	protected AuthorizationManager getAuthorizationManager(Credential credential) {
		if (credential instanceof ClientSecretCredential) {
			return new StorageTokenAuthorizationManager((ClientSecretCredential) credential);
		} else if (credential instanceof AzureNamedKeyCredential) {
			return new StorageShareKeyAuthorizationManager((AzureNamedKeyCredential) credential);
		}

		throw new RuntimeException("Only support 'Client Secret Credential' or 'Named Key Credential'");
	}

	protected RuntimeException createUploadException(String blobName, HttpResponse<?> response) {
		String message = String.format(UPLOAD_ERROR_MGS, blobName, response.statusCode());
		return new RuntimeException(message);
	}

	protected RuntimeException createGetBlobException(String blobName, HttpResponse<?> response) {
		String message = String.format(GET_BLOB_ERROR_MGS, blobName, response.statusCode());
		return new RuntimeException(message);
	}

	protected RuntimeException createDownloadException(String blobName, HttpResponse<?> response) {
		String message = String.format(DOWNLOAD_ERROR_MGS, blobName, response.statusCode());
		return new RuntimeException(message);
	}
}
