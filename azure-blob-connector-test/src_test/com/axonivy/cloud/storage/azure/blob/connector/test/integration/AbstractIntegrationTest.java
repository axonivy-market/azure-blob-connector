package com.axonivy.cloud.storage.azure.blob.connector.test.integration;

import java.util.UUID;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import com.axonivy.cloud.storage.azure.blob.connector.BlobServiceClientHelper;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.specialized.BlockBlobClient;

abstract class AbstractIntegrationTest {

	protected static final String TEST_CONTAINTER = "test-container";
	protected static final String ACCOUNT_NAME = "devstoreaccount1";
	protected static final String END_POINT_FORMAT = "http://127.0.0.1:%s/devstoreaccount1";
	protected static final String ACCOUNT_KEY = "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==";

	protected static final String EXTENSION_TEST = ".test";
	protected static final String CONTENT_TEST = "testContent.txt";
	private static final int BLOB_PORT = 10000;	
	
	public static final GenericContainer<?> azure = new GenericContainer<>(
			DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite")).withExposedPorts(BLOB_PORT);

	static {
		azure.start();
	}

	protected static BlobServiceClient getBlobServiceClient() {
		final String endpoint = String.format(END_POINT_FORMAT, azure.getMappedPort(BLOB_PORT));
		return BlobServiceClientHelper.getBlobServiceClient(ACCOUNT_NAME, ACCOUNT_KEY, endpoint);
	}

	protected static BlockBlobClient getBlockBlobClient(BlobServiceClient blobServiceClient, String blobName) {
		BlobContainerClient blobContainerClient = getBlobContainerClient(blobServiceClient);
		BlockBlobClient blobClient = blobContainerClient.getBlobClient(blobName).getBlockBlobClient();
		return blobClient;
	}

	protected static BlobContainerClient getBlobContainerClient(BlobServiceClient blobServiceClient) {
		BlobContainerClient blobContainerClient = blobServiceClient.createBlobContainerIfNotExists(TEST_CONTAINTER);
		return blobContainerClient;
	}
	
	protected boolean isUUIDFomart(String value, String expectedExtension) {
		String uuid = value.split("\\.")[0];
		
		return UUID.fromString(uuid) != null && value.endsWith("." + expectedExtension);
	}
}
