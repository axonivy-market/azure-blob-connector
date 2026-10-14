package com.axonivy.connector.azure.blob.test.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.axonivy.connector.azure.blob.StorageService;
import com.axonivy.connector.azure.blob.internal.AzureBlobStorageService;

import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.environment.IvyTest;

@Testcontainers
@IvyTest
public class FailedCallTest extends AbstractIntegrationTest {

	// More failed calls than connections in the pool of the REST client (Ivy default: 5)
	private static final int FAILED_CALLS = 10;

	private static StorageService storageService = null;

	@BeforeAll
	public static void setup(AppFixture fixture) {
		fixture.config("RestClients.AzureBlobStorage.Url", blobEndpoint);
		storageService = new AzureBlobStorageService(getCredential(), TEST_CONTAINER);
	}

	/**
	 * A response that is not closed keeps its pooled connection. Without closing, the calls after the pool is
	 * exhausted wait forever for a connection, and the timeout interrupts the test.
	 */
	@Test
	@Timeout(value = 60, unit = TimeUnit.SECONDS)
	void shouldReleaseConnectionOfFailedCalls() throws Exception {
		for (int i = 0; i < FAILED_CALLS; i++) {
			assertFalse(storageService.delete("missing-" + i + ".txt"));
			assertFalse(storageService.exists("missing-" + i + ".txt"));
		}

		String blobName = storageService.upload("content".getBytes(), "after-failed-calls.txt", StringUtils.EMPTY,
				true);
		assertEquals("after-failed-calls.txt", blobName);
	}

	@Test
	void shouldReportAzureErrorCode() {
		var exception = assertThrows(RuntimeException.class,
				() -> storageService.downloadContent("missing-for-download.txt"));

		assertTrue(exception.getMessage().contains("BlobNotFound"), exception.getMessage());
	}
}
