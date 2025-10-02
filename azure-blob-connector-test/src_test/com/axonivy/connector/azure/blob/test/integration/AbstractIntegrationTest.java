package com.axonivy.connector.azure.blob.test.integration;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.testcontainers.azure.AzuriteContainer;
import org.testcontainers.utility.DockerImageName;

import com.axonivy.connector.azure.blob.internal.auth.AzureNamedKeyCredential;
import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.internal.constant.Constants;

abstract class AbstractIntegrationTest {

	private static final String ACCOUNT_NAME_EQUAL = "AccountName=";
	private static final String ACCOUNT_KEY_EQUAL = "AccountKey=";
	private static final String BLOB_ENDPOINT_EQUAL = "BlobEndpoint=";

	protected static final String TEST_CONTAINTER = "test-container";
	protected static final String EXTENSION_TEST = ".test";
	protected static final String CONTENT_TEST_BLOB_NAME = "testContent.txt";

	protected static String accountName = null;
	protected static String accountKey = null;
	protected static String blobEndpoint = null;

	private static AzuriteContainer azurite = new AzuriteContainer(
			DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite:3.35.0"));

	static {
		azurite.start();

		String connectionString = azurite.getConnectionString();
		accountName = getAttributeValue(connectionString, ACCOUNT_NAME_EQUAL);
		accountKey = getAttributeValue(connectionString, ACCOUNT_KEY_EQUAL);
		blobEndpoint = getAttributeValue(connectionString, BLOB_ENDPOINT_EQUAL);

		Runtime.getRuntime().addShutdownHook(new Thread(azurite::stop));
	}

	protected static Credential getCredential() {
		return new AzureNamedKeyCredential(accountName, accountKey);
	}

	private static String getAttributeValue(String connectionString, String key) {
		for (String part : connectionString.split(Constants.SEMI_COLON)) {
			if (part.startsWith(key)) {

				return part.substring(key.length());
			}
		}
		return EMPTY;
	}
}
