package com.axonivy.cloud.storage.azure.blob.connector.test.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.axonivy.cloud.storage.azure.blob.connector.StorageService;
import com.axonivy.cloud.storage.azure.blob.connector.internal.AzureBlobStorageService;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.specialized.BlockBlobClient;

@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class AzureBlobStorageServiceTest extends AbstractIntegrationTest {

	private static BlobServiceClient blobServiceClient = null;
	private static StorageService storageService = null;	
	private static String blobNameOfFileUpload = null;
	
	@BeforeAll
	public static void setup() {
		blobServiceClient = getBlobServiceClient();
		storageService = new AzureBlobStorageService(blobServiceClient, TEST_CONTAINTER);
	}

	@Test
	void shouldUploadContent() throws Exception {
		final String content = "Test Content";
		storageService.upload(content, CONTENT_TEST);
		BlockBlobClient blobClient = getBlockBlobClient(blobServiceClient, CONTENT_TEST);
		String downloadContent = blobClient.downloadContent().toString();

		assertEquals(content, downloadContent);
	}
	
	@Test
	@Order(1)
	void shouldUploadFromFile() {
		String path = new File("resource_test/picture/singapore.png").getAbsolutePath();
		String blobName = storageService.uploadFromFile(path, StringUtils.EMPTY, true);
		blobNameOfFileUpload = blobName;
		assertNotNull(blobName);
	}
	
	@Test
	void shouldUploadByteArray() throws Exception {
		String sampleData = "Sample data for blob";
		String blobName = storageService.upload(sampleData.getBytes(), CONTENT_TEST, StringUtils.EMPTY, true);
		assertNotNull(blobName);
	}
	
	@Test
	void shouldUploadByteArrayWithException() throws Exception {
		var exception = assertThrows(
				Exception.class,
	            () -> storageService.upload(null, "nullContent.txt", StringUtils.EMPTY, false));
		
		assertEquals("Upload file error. Exception message: Cannot read the array length because \"buf\" is null", exception.getMessage());
	}
	
	@Test
	@Order(2)
	void shouldDownloadContent() {
		byte[] result = storageService.downloadContent(blobNameOfFileUpload);
		assertNotNull(result);
	}
	
	@Test
	@Order(3)
	void shouldDownloadToFile() throws IOException {
		File templeLocalFile = new File("resource_test/picture/local-file.png");
		storageService.downloadToFile(blobNameOfFileUpload, templeLocalFile.getAbsolutePath());
		boolean fileDeleted = Files.deleteIfExists(templeLocalFile.toPath());
		assertTrue(fileDeleted);
	}
	
	@Test
	@Order(4)
	void shouldDownloadStream() throws IOException {
		ByteArrayOutputStream baos = storageService.downloadStream(blobNameOfFileUpload);
		assertNotNull(baos);
	}
	
	@Test
	@Order(5)
	void shouldDeletedWithBlobIsExists() {
		boolean result  = storageService.delete(blobNameOfFileUpload);
		assertTrue(result);
	}
	
	@Test
	@Order(6)
	void shouldDeletedWithBlobIsNotExists() {
		boolean result  = storageService.delete(blobNameOfFileUpload);
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
