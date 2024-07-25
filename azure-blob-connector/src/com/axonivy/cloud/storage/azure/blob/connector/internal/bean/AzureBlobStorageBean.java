package com.axonivy.cloud.storage.azure.blob.connector.internal.bean;

import com.axonivy.cloud.storage.azure.blob.connector.BlobServiceClientHelper;
import com.axonivy.cloud.storage.azure.blob.connector.StorageService;
import com.axonivy.cloud.storage.azure.blob.connector.internal.AzureBlobStorageService;
import com.azure.storage.blob.BlobServiceClient;

import ch.ivyteam.ivy.environment.Ivy;

public class AzureBlobStorageBean {
	
	private StorageService azureBlobStorageService;

	public AzureBlobStorageBean() {
		String clientId = Ivy.var().get("AzureBlob.ClientId");
		String clientSecret = Ivy.var().get("AzureBlob.ClientSecret");
		String tenantId = Ivy.var().get("AzureBlob.TenantId");
		String endPoint = Ivy.var().get("AzureBlob.EndPoint");
		String containerName = Ivy.var().get("AzureBlob.ContainterName");
		
		BlobServiceClient blobServiceClient = BlobServiceClientHelper.getBlobServiceClient(clientId,  clientSecret, tenantId, endPoint);
		azureBlobStorageService = new AzureBlobStorageService(blobServiceClient, containerName);
	}
	
	public StorageService getAzureBlobStorageService() {
		return azureBlobStorageService;
	}

	public void setAzureBlobStorageService(StorageService azureBlobStorageService) {
		this.azureBlobStorageService = azureBlobStorageService;
	}
	
	public boolean isBlobExist(String blobName) {
		return azureBlobStorageService.getBlobClient(blobName).exists();
	}
}
