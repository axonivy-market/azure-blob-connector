package com.axonivy.connector.azure.blob.test.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.axonivy.connector.azure.blob.StorageService;
import com.axonivy.connector.azure.blob.internal.AzureBlobStorageService;

import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.environment.IvyTest;

@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
@IvyTest
public class AzureBlobStorageServiceTest extends AbstractIntegrationTest {

	private static StorageService storageService = null;
	private static String blobNameOfFileUpload = null;

	@BeforeAll
	public static void setup(AppFixture fixture) {
		fixture.config("RestClients.AzureBlobStorageTest.Url", getEndpoint());
		storageService = new AzureBlobStorageService(getCredential(), REST_CLIENT_TEST, TEST_CONTAINTER);
	}

	@Test
	void shouldUploadContent() throws Exception {
		final String content = "Test Content";
		String blobName = storageService.upload(content, CONTENT_TEST_BLOB_NAME);

		assertEquals(CONTENT_TEST_BLOB_NAME, blobName);
	}

	@Test
	@Order(1)
	void shouldUploadFromFile() throws Exception {
		String path = new File("resource_test/picture/singapore.png").getAbsolutePath();
		String blobName = storageService.uploadFromFile(path, StringUtils.EMPTY, true);
		blobNameOfFileUpload = blobName;
		assertNotNull(blobName);
	}

	@Test
	void shouldUploadByteArray() throws Exception {
		String sampleData = "Sample data for blob";
		String blobName = storageService.upload(sampleData.getBytes(), CONTENT_TEST_BLOB_NAME, StringUtils.EMPTY, true);
		assertNotNull(blobName);
	}

	@Test
	void shouldUploadByteArrayWithException() throws Exception {
		var exception = assertThrows(Exception.class,
				() -> storageService.upload(null, "nullContent.txt", StringUtils.EMPTY, false));

		assertEquals("Upload content must be not null", exception.getMessage());
	}

	@Test
	@Order(2)
	void shouldDownloadContent() throws Exception {
		byte[] result = storageService.downloadContent(blobNameOfFileUpload);
		assertNotNull(result);
	}

	@Test
	@Order(3)
	void shouldDownloadToFile() throws Exception {
		File templeLocalFile = new File("resource_test/picture/local-file.png");
		storageService.downloadToFile(blobNameOfFileUpload, templeLocalFile.getAbsolutePath());
		boolean fileDeleted = Files.deleteIfExists(templeLocalFile.toPath());
		assertTrue(fileDeleted);
	}

	@Test
	@Order(4)
	void shouldDownloadStream() throws Exception {
		ByteArrayOutputStream baos = storageService.downloadStream(blobNameOfFileUpload);
		assertNotNull(baos);
	}

	@Test
	@Order(5)
	void shouldDeletedWithBlobIsExists() {
		boolean result = storageService.delete(blobNameOfFileUpload);
		assertTrue(result);
	}

	@Test
	@Order(6)
	void shouldDeletedWithBlobIsNotExists() {
		boolean result = storageService.delete(blobNameOfFileUpload);
		assertFalse(result);
	}

	@Test
	@Order(7)
	void shouldRestoreBlobIsDeleted() {
		// Azurite: Current API is not implemented yet.
	}

	@Disabled
	void shouldGetDownloadLink() throws Exception {
		// Azurite: Only authentication scheme Bearer is supported
	}

	@Disabled
	void shouldUploadFromUrl() throws Exception {
		// Azurite: Current API is not implemented yet.
	}
}
