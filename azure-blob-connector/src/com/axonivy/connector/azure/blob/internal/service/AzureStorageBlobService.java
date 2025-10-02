package com.axonivy.connector.azure.blob.internal.service;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;

import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.client.AzureStorageBlobClient;
import com.axonivy.connector.azure.blob.internal.constant.BlobQuery;
import com.axonivy.connector.azure.blob.internal.constant.MSHeader;
import com.axonivy.connector.azure.blob.internal.enums.DeleteSnapshotsOptionType;
import com.axonivy.connector.azure.blob.model.BlobItem;

import ch.ivyteam.ivy.environment.Ivy;

public class AzureStorageBlobService extends AbstractAzureStorage {

	private AzureStorageBlobClient ivyClient;

	public AzureStorageBlobService(Credential credential, String account, String container) {
		this.container = container;

		AuthorizationManager manager = getAuthorizationManager(credential);
		this.ivyClient = new AzureStorageBlobClient(account, manager);
	}

	public String upload(String blobName, String contentBody) {
		Map<String, String> headers = createUploadHeader(false);

		String blobPath = getBlobPath(blobName);
		var response = this.ivyClient.put(blobPath, contentBody, headers);
		return isHttpCreated(response) ? blobName : EMPTY;
	}

	public String uploadFromUrl(String blobName, String url, boolean isOverwrite) {
		Map<String, String> headers = createUploadHeader(isOverwrite);
		headers.put(MSHeader.X_MS_COPY_SOURCE, url);

		String blobPath = getBlobPath(blobName);
		var response = this.ivyClient.put(blobPath, headers);
		if (isHttpCreated(response)) {
			return blobName;
		}

		throw createUploadException(blobName, response);
	}

	public String uploadFromFile(String blobName, byte[] content, boolean isOverwrite) throws Exception {
		Objects.requireNonNull(content, "Upload content must be not null");
		Map<String, String> headers = createUploadHeader(isOverwrite);

		String blobPath = getBlobPath(blobName);
		var response = this.ivyClient.put(blobPath, content, headers);
		if (isHttpCreated(response)) {
			return blobName;
		}

		throw createUploadException(blobName, response);
	}

	public String uploadFromPath(String blobName, String pathFile, boolean isOverwrite) throws Exception {
		File fileTemp = new File(pathFile);
		Map<String, String> headers = createUploadHeader(isOverwrite);
		String blobPath = getBlobPath(blobName);
		var response = this.ivyClient.put(blobPath, fileTemp, headers);
		if (isHttpCreated(response)) {
			return blobName;
		}
		throw createUploadException(blobName, response);
	}

	public boolean delete(String blobName) {
		Map<String, String> headers = new HashMap<>();
		headers.put(MSHeader.X_MS_DELETE_SNAPSHOTS, DeleteSnapshotsOptionType.INCLUDE.getValue());

		String blobPath = getBlobPath(blobName);
		var response = this.ivyClient.delete(blobPath, headers);
		return isHttpAccepted(response);
	}

	public boolean undelete(String blobName) {
		Map<String, String> queries = new HashMap<>();
		queries.put(BlobQuery.Parameter.COMP, BlobQuery.Value.UNDELETE);

		String blobPath = getBlobPath(blobName);
		var response = this.ivyClient.delete(blobPath, emptyMap(), queries);
		return isHttpOk(response);
	}

	public BlobItem getBlob(String blobName) {
		String blobPath = getBlobPath(blobName);
		var response = this.ivyClient.get(blobPath);

		if (isHttpOk(response)) {
			String endpoint = getEndpoint(ivyClient);
			String url = String.format("%s/%s", endpoint, blobPath);

			BlobItem blobItem = BlobItem.Builder.builder().name(blobName).url(url).build();
			return blobItem;
		}
		return null;
	}

	public byte[] downloadContent(String blobName) {
		String blobPath = getBlobPath(blobName);
		Response response = this.ivyClient.get(blobPath);
		if (!isHttpOk(response)) {
			throw createDownloadException(blobName, response);
		}

		return response.readEntity(byte[].class);
	}

	public String downloadToFile(String blobName, String filePath) throws IOException {
		String blobPath = getBlobPath(blobName);
		Response response = this.ivyClient.get(blobPath);
		if (isHttpOk(response)) {
			InputStream in = response.readEntity(InputStream.class);
			File targetFile = new File(filePath);
			FileUtils.copyInputStreamToFile(in, targetFile);
			return blobName;
		}

		throw createDownloadException(blobName, response);
	}

	public ByteArrayOutputStream downloadStream(String blobName) {
		String blobPath = getBlobPath(blobName);
		Response response = this.ivyClient.get(blobPath);

		if (!isHttpOk(response)) {
			throw createDownloadException(blobName, response);
		}

		try {
			ByteArrayOutputStream targetStream = new ByteArrayOutputStream();
			response.readEntity(InputStream.class).transferTo(targetStream);
			return targetStream;
		} catch (Exception e) {
			throw new RuntimeException("Error while stream data from " + blobPath, e);
		}
	}

	private String getEndpoint(AzureStorageBlobClient client) {
		try {
			return client.getURL().toString();
		} catch (MalformedURLException e) {
			Ivy.log().error("Error while getting endpoint", e);
		}
		return EMPTY;
	}
}
