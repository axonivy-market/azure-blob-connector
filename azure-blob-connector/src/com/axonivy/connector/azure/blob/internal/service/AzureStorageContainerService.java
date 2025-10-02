package com.axonivy.connector.azure.blob.internal.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.client.AzureStorageBlobClient;
import com.axonivy.connector.azure.blob.internal.constant.BlobQuery;
import com.axonivy.connector.azure.blob.internal.model.response.BlobItemInternal;
import com.axonivy.connector.azure.blob.internal.model.response.BlobItemPropertiesInternal;
import com.axonivy.connector.azure.blob.internal.model.response.BlobItemsInternal;
import com.axonivy.connector.azure.blob.internal.model.response.ContainerBlobs;
import com.axonivy.connector.azure.blob.model.BlobItem;
import com.axonivy.connector.azure.blob.model.BlobItemProperties;

import ch.ivyteam.ivy.environment.Ivy;

public class AzureStorageContainerService extends AbstractAzureStorage {

	private AzureStorageBlobClient ivyClient;

	public AzureStorageContainerService(Credential credential, String account, String container) {
		this.container = container;

		AuthorizationManager manager = getAuthorizationManager(credential);
		this.ivyClient = new AzureStorageBlobClient(account, manager);

		createContainerIfNotExists(container);
	}

	public boolean createContainerIfNotExists(String container) {
		if (isExists(container)) {
			return true;
		}

		Map<String, String> queries = new HashMap<>();
		queries.put(BlobQuery.Parameter.RESTYPE, BlobQuery.Value.CONTAINER);

		try {
			var response = this.ivyClient.put(this.container, emptyMap(), queries);
			return isHttpCreated(response);
		} catch (Exception e) {
			Ivy.log().warn("The container {} can not create", e, this.container);
			return false;
		}
	}

	private boolean isExists(String container) {
		Map<String, String> queries = new HashMap<>();
		queries.put(BlobQuery.Parameter.RESTYPE, BlobQuery.Value.CONTAINER);

		try {
			var response = this.ivyClient.get(this.container, queries);
			return isHttpOk(response);
		} catch (Exception e) {
			Ivy.log().warn("The container {} is not exists", e, this.container);
			return false;
		}
	}

	public List<BlobItem> getBlobs() {
		Map<String, String> queries = new HashMap<>();
		queries.put(BlobQuery.Parameter.RESTYPE, BlobQuery.Value.CONTAINER);
		queries.put(BlobQuery.Parameter.COMP, BlobQuery.Value.LIST);

		try {
			var response = this.ivyClient.get(this.container, queries);
			ContainerBlobs result = getBody(response, ContainerBlobs.class);
			List<BlobItemInternal> blobItemInternals = Optional.ofNullable(result)
					.map(ContainerBlobs::getBlobItemsInternal).map(BlobItemsInternal::getBlobItemInternals)
					.orElse(emptyList());

			return blobItemInternals.stream().map(it -> convertToBlobItem(it)).toList();
		} catch (Exception e) {
			Ivy.log().warn("Get list blob of container {} is error", e, this.container);
		}
		return emptyList();
	}

	private BlobItem convertToBlobItem(BlobItemInternal blobItemInternal) {
		String blobName = blobItemInternal.getName();

		BlobItemPropertiesInternal blobItemPropertiesInternal = blobItemInternal.getProperties();
		BlobItemProperties properties = BlobItemProperties.Builder.builder()
				.creationTime(blobItemPropertiesInternal.getCreationTime())
				.contentLength(blobItemPropertiesInternal.getContentLength())
				.contentLength(blobItemPropertiesInternal.getContentLength())
				.contentType(blobItemPropertiesInternal.getContentType()).build();

		return BlobItem.Builder.builder().name(blobName).properties(properties).url(getBlobPath(blobName))
				.deleted(blobItemInternal.isDeleted()).build();
	}
}
