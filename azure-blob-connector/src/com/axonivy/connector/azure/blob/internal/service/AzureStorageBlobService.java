package com.axonivy.connector.azure.blob.internal.service;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.client.AzureStorageClient;
import com.axonivy.connector.azure.blob.internal.constant.BlobQuery;
import com.axonivy.connector.azure.blob.internal.constant.MSHeader;
import com.axonivy.connector.azure.blob.internal.enums.BlobType;
import com.axonivy.connector.azure.blob.internal.enums.DeleteSnapshotsOptionType;
import com.axonivy.connector.azure.blob.model.BlobItem;

public class AzureStorageBlobService extends AbstractAzureStorage {

	private AzureStorageClient httpClient;
	
	public AzureStorageBlobService(Credential credential, String endpoint, String container) {
		this.endpoint = endpoint;
		this.container = container;

		AuthorizationManager manager = getAuthorizationManager(credential);
		this.httpClient = new AzureStorageClient(manager);	
	}
	
	public String upload(String blobName, String contentBody) {
		Map<String, String> headers = createUploadHeader(contentBody.length(), false);

		String blobPath = getBlobPath(blobName);
		var response = this.httpClient.put(blobPath, contentBody, headers);
		return isHttpCreated(response) ? blobName : EMPTY;
	}

	public String uploadFromUrl(String blobName, String url, boolean isOverwrite) {
		Map<String, String> headers = new HashMap<>();
		headers.put(MSHeader.X_MS_COPY_SOURCE, url);
		headers.put(MSHeader.X_MS_BLOB_TYPE, BlobType.BLOCK_BLOB.getValue());
		if (!isOverwrite) {
			headers.put(HttpHeaders.IF_NONE_MATCH, MediaType.MEDIA_TYPE_WILDCARD);
		}

		String blobPath = getBlobPath(blobName);
		var response = this.httpClient.put(blobPath, headers);
		if (isHttpCreated(response)) {
			return blobName;
		}

		throw createUploadException(blobName, response);
	}

	public String uploadFromFile(String blobName, byte[] content, boolean isOverwrite) throws Exception {
		Objects.requireNonNull(content, "Upload content must be not null");
		Map<String, String> headers = createUploadHeader(content.length, isOverwrite);
		
		String blobPath = getBlobPath(blobName);
		var response = this.httpClient.put(blobPath, content, headers);
		if (isHttpCreated(response)) {
			return blobName;
		}

		throw createUploadException(blobName, response);
	}

	public String uploadFromPath(String blobName, String pathFile, boolean isOverwrite) throws Exception {
		File fileTemp = new File(pathFile);		
		Map<String, String> headers = createUploadHeader(fileTemp.length(), isOverwrite);
		
		String blobPath = getBlobPath(blobName);		
		var response = this.httpClient.put(blobPath, fileTemp, headers);		
		if (isHttpCreated(response)) {
			return blobName;
		}

		throw createUploadException(blobName, response);
	}

	public boolean delete(String blobName) {
		Map<String, String> headers = new HashMap<>();
		headers.put(MSHeader.X_MS_DELETE_SNAPSHOTS, DeleteSnapshotsOptionType.INCLUDE.getValue());

		String blobPath = getBlobPath(blobName);
		var response = this.httpClient.delete(blobPath, headers);
		return isHttpAccepted(response);
	}

	public boolean undelete(String blobName) {
		Map<String, String> queries = new HashMap<>();
		queries.put(BlobQuery.Parameter.COMP, BlobQuery.Value.UNDELETE);

		String blobPath = getBlobPath(blobName);
		var response = this.httpClient.delete(blobPath, emptyMap(), queries);
		return isHttpOk(response);
	}

	public BlobItem getBlob(String blobName) {
		String blobPath = getBlobPath(blobName);
		var response = this.httpClient.get(blobPath);
		if (isHttpOk(response)) {
			BlobItem blobItem = BlobItem.Builder.builder().name(blobName).url(blobPath).build();
			return blobItem;
		}
		return null;
	}

	public byte[] downloadContent(String blobName) {
		String blobPath = getBlobPath(blobName);
		HttpResponse<InputStream> response = this.httpClient.downloadStream(blobPath);
		if (!isHttpOk(response)) {
			throw createDownloadException(blobName, response);	
		}
		
		byte[] data;
		try {
			data = response.body().readAllBytes();
			return data;
		} catch (IOException e) {
			throw new RuntimeException("Error while reading data from " + blobPath, e);
		}		
	}

	public String downloadToFile(String blobName, String filePath) {
		String blobPath = getBlobPath(blobName);
		HttpResponse<Path> response = this.httpClient.downloadToFile(blobPath, filePath);
		if (isHttpOk(response)) {
			return blobName;
		}

		throw createDownloadException(blobName, response);
	}

	public ByteArrayOutputStream downloadStream(String blobName) {
		String blobPath = getBlobPath(blobName);
		HttpResponse<InputStream> response = this.httpClient.downloadStream(blobPath);
		
		if (!isHttpOk(response)) {
			throw createDownloadException(blobName, response);	
		}
		
		try {
			ByteArrayOutputStream targetStream = new ByteArrayOutputStream();
			response.body().transferTo(targetStream);
			return targetStream;
		} catch (Exception e) {
			throw new RuntimeException("Error while stream data from " + blobPath, e);
		}
	}

}
