package com.axonivy.connector.azure.blob.internal.bean;

import java.util.UUID;

import com.axonivy.connector.azure.blob.StorageService;
import com.axonivy.connector.azure.blob.internal.AzureBlobStorageService;
import com.axonivy.connector.azure.blob.internal.auth.ClientSecretCredential;
import com.axonivy.connector.azure.blob.internal.auth.Credential;

import ch.ivyteam.ivy.environment.Ivy;

public class AzureBlobStorageBean {

	private StorageService azureBlobStorageService;

	public AzureBlobStorageBean() {
		String clientId = Ivy.var().get("AzureBlob.ClientId");
		String clientSecret = Ivy.var().get("AzureBlob.ClientSecret");
		String tenantId = Ivy.var().get("AzureBlob.TenantId");
		String containerName = Ivy.var().get("AzureBlob.ContainterName");
		String restClientUUID = Ivy.var().get("AzureBlob.RestClientUUID");
		
		Credential credential = new ClientSecretCredential(tenantId, clientId, clientSecret);
		this.azureBlobStorageService = new AzureBlobStorageService(credential, UUID.fromString(restClientUUID), containerName);
	}

	public StorageService getAzureBlobStorageService() {
		return azureBlobStorageService;
	}

	public void setAzureBlobStorageService(StorageService azureBlobStorageService) {
		this.azureBlobStorageService = azureBlobStorageService;
	}

	public boolean isBlobExist(String blobName) {
		return azureBlobStorageService.exists(blobName);
	}
}