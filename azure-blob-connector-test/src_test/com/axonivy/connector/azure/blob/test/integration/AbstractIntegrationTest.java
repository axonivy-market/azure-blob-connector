package com.axonivy.connector.azure.blob.test.integration;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import com.axonivy.connector.azure.blob.BlobServiceClientHelper;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.specialized.BlockBlobClient;

abstract class AbstractIntegrationTest {

	protected static final String TEST_CONTAINTER = "test-container";
	private static final String END_POINT_FORMAT = "http://127.0.0.1:%s/%s";

	protected static final String EXTENSION_TEST = ".test";
	protected static final String CONTENT_TEST = "testContent.txt";
	private static final int BLOB_PORT = 10000;

	@SuppressWarnings("resource")
	public static final GenericContainer<?> azure = new GenericContainer<>(
			DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite")).withExposedPorts(BLOB_PORT);

	static {
		azure.start();
		Runtime.getRuntime().addShutdownHook(new Thread(azure::stop));
	}

	protected static BlobServiceClient getBlobServiceClient() throws IOException {
		String account = System.getProperty("azureBlobAccount");
		String key = System.getProperty("azureBlobKey");

		if (StringUtils.isEmpty(account)) {
			try (var in = AbstractIntegrationTest.class.getResourceAsStream("credentials.properties")) {
				if (in != null) {
					Properties props = new Properties();
					props.load(in);
					account = (String) props.get("account");
					key = (String) props.get("key");
				}
			}
		}

		final String endpoint = String.format(END_POINT_FORMAT, azure.getMappedPort(BLOB_PORT), account);
		return BlobServiceClientHelper.getBlobServiceClient(account, key, endpoint);
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
