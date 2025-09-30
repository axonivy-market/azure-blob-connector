package com.axonivy.connector.azure.blob.test.integration;

import java.util.UUID;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import com.axonivy.connector.azure.blob.internal.auth.AzureNamedKeyCredential;
import com.axonivy.connector.azure.blob.internal.auth.Credential;

abstract class AbstractIntegrationTest {

	private static final int BLOB_PORT = 10000;

	protected static final UUID REST_CLIENT_TEST = UUID.fromString("b6cd1c14-6fc8-4aa0-bf47-39e8f1fd8ba5");
	protected static final String TEST_CONTAINTER = "test-container";
	protected static final String ACCOUNT_NAME = "devstoreaccount1";
	protected static final String END_POINT_FORMAT = "http://127.0.0.1:%s/devstoreaccount1";
	// This is a well known key from Azurite
 /**
  * Dear Bug Hunter,
  * This credential is intentionally included for educational purposes only and does not provide access to any production systems.
  * Please do not submit it as part of our bug bounty program.
  */
	protected static final String ACCOUNT_KEY_TEST = "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==";

	protected static final String EXTENSION_TEST = ".test";
	protected static final String CONTENT_TEST_BLOB_NAME = "testContent.txt";

	@SuppressWarnings("resource")
	public static final GenericContainer<?> azure = new GenericContainer<>(
			DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite:3.35.0")).withExposedPorts(BLOB_PORT);

	static {
		azure.start();
		Runtime.getRuntime().addShutdownHook(new Thread(azure::stop));
	}

	protected static Credential getCredential() {
		return new AzureNamedKeyCredential(ACCOUNT_NAME, ACCOUNT_KEY_TEST);
	}

	protected static String getEndpoint() {
		return String.format(END_POINT_FORMAT, azure.getMappedPort(BLOB_PORT));
	}
}
