package com.axonivy.connector.azure.blob;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;

public class BlobServiceClientHelper {
	
	/**
	 *  Create client to a storage account. 
	 * @param clientId - the client ID of the application
	 * @param clientSecret - the secret value of the Microsoft Entra application.
	 * @param tenantId - the tenant ID of the application.
	 * @param endpoint - URL of the service
	 * @return a {@link BlobServiceClient} created from the configurations in this builder
	 */
	public static BlobServiceClient getBlobServiceClient(String clientId, String clientSecret, String tenantId, String endpoint) {
		
		ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.tenantId(tenantId)
				.build();

		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
				.endpoint(endpoint)
				.credential(clientSecretCredential)
				.buildClient();
		
		return blobServiceClient;
	}

	/**
	 * Create client to a storage account. 
	 * @param connectionString - onnection string of the storage account
	 * @param endpoint - URL of the service
	 * @return  a {@link BlobServiceClient} created from the configurations in this builder
	 */
	public static BlobServiceClient getBlobServiceClient(String connectionString, String endpoint) {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
				.endpoint(endpoint)
				.connectionString(connectionString)
				.buildClient();

		return blobServiceClient;
	}
	
	/**
	 * * Create client to a storage account. 
	 * @param accountName The account name associated with the request.
     * @param accountKey The account access key used to authenticate the request.
	 * @param endpoint - URL of the service
	 * @return  a {@link BlobServiceClient} created from the configurations in this builder
	 */
	public static BlobServiceClient getBlobServiceClient(String accountName, String accountKey, String endpoint) {
		StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
				.endpoint(endpoint)
				.credential(credential)
				.buildClient();

		return blobServiceClient;
	}
}
