package com.axonivy.connector.azure.blob.internal.bean;

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
		String endPoint = Ivy.var().get("AzureBlob.EndPoint");
		String containerName = Ivy.var().get("AzureBlob.ContainterName");
		
		Credential credential = new ClientSecretCredential(tenantId, clientId, clientSecret);
		azureBlobStorageService = new AzureBlobStorageService(credential, endPoint, containerName);
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
