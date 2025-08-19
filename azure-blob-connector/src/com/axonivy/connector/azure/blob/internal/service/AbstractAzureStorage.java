package com.axonivy.connector.azure.blob.internal.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.axonivy.connector.azure.blob.internal.auth.AzureNamedKeyCredential;
import com.axonivy.connector.azure.blob.internal.auth.ClientSecretCredential;
import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.internal.auth.StorageShareKeyAuthorizationManager;
import com.axonivy.connector.azure.blob.internal.auth.StorageTokenAuthorizationManager;
import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.constant.MSHeader;
import com.axonivy.connector.azure.blob.internal.enums.BlobType;

abstract class AbstractAzureStorage {

	private static final String DOWNLOAD_ERROR_MGS = "The downloading blob data %s is error. HttpStatus %s";
	private static final String UPLOAD_ERROR_MGS = "The uploading blob data %s is error. HttpStatus %s";
	private static final String GET_BLOB_ERROR_MGS = "The getting blob %s is error. HttpStatus %s";

	protected String container;

	protected String getBlobPath(String fileName) {
		return String.format("%s/%s", this.container, fileName);
	}

	protected Map<String, String> createUploadHeader(boolean isOverwrite) {
		Map<String, String> headers = new HashMap<>();
		headers.put(MSHeader.X_MS_BLOB_TYPE, BlobType.BLOCK_BLOB.getValue());

		if (!isOverwrite) {
			headers.put(HttpHeaders.IF_NONE_MATCH, MediaType.MEDIA_TYPE_WILDCARD);
		}

		return headers;
	}

	protected boolean isHttpCreated(Response response) {
		return response.getStatus() == HttpStatus.SC_CREATED;
	}

	protected boolean isHttpOk(Response response) {
		return response.getStatus() == HttpStatus.SC_OK;
	}

	protected boolean isHttpAccepted(Response response) {
		return response.getStatus() == HttpStatus.SC_ACCEPTED;
	}

	protected <T> T getBody(Response response, Class<T> type) {
		if (response.getStatus() == HttpStatus.SC_OK) {
			return response.readEntity(type);
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

	protected RuntimeException createUploadException(String blobName, Response response) {
		String message = String.format(UPLOAD_ERROR_MGS, blobName, response.getStatus());
		return new RuntimeException(message);
	}

	protected RuntimeException createGetBlobException(String blobName, Response response) {
		String message = String.format(GET_BLOB_ERROR_MGS, blobName, response.getStatus());
		return new RuntimeException(message);
	}

	protected RuntimeException createDownloadException(String blobName, Response response) {
		String message = String.format(DOWNLOAD_ERROR_MGS, blobName, response.getStatus());
		return new RuntimeException(message);
	}
}
