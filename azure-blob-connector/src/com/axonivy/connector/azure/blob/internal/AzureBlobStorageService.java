package com.axonivy.connector.azure.blob.internal;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.azure.blob.StorageService;
import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.internal.service.AzureStorageBlobService;
import com.axonivy.connector.azure.blob.internal.service.AzureStorageContainerService;
import com.axonivy.connector.azure.blob.internal.service.AzureStorageSASService;
import com.axonivy.connector.azure.blob.model.BlobItem;

import ch.ivyteam.ivy.environment.Ivy;

public class AzureBlobStorageService implements StorageService {
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private Duration downloadLinkLiveTime = Duration.ofHours(8);
	
	private AzureStorageContainerService azureStorageContainerService;
	private AzureStorageSASService azureStorageSASService;
	private AzureStorageBlobService azureStorageBlobService;

	/**
	 * Create a AzureBlobStorageService with the give identity credential,
	 * ivyRestClientId and container
	 * 
	 * @param tokenCredential - The credential type
	 * @param restClientUUID - The Ivy rest client UUID
	 * @param container       - The container name
	 */
	public AzureBlobStorageService(Credential tokenCredential, UUID restClientUUID, String container) {
		this.azureStorageContainerService = new AzureStorageContainerService(tokenCredential, restClientUUID, container);
		this.azureStorageSASService = new AzureStorageSASService(tokenCredential, restClientUUID, container);
		this.azureStorageBlobService = new AzureStorageBlobService(tokenCredential, restClientUUID, container);
	}

	/**
	 * Create a AzureBlobStorageService with the give identity credential,
	 * ivyRestClientId and container
	 * 
	 * @param tokenCredential      - The credential types
	 * @param ivyRestClientId      - The Ivy rest client UUID
	 * @param container            - The container name
	 * @param downloadLinkLiveTime - The time live of download link
	 */
	public AzureBlobStorageService(Credential tokenCredential, UUID ivyRestClientId, String container, Duration downloadLinkLiveTime) {
		this.azureStorageContainerService = new AzureStorageContainerService(tokenCredential, ivyRestClientId, container);
		this.azureStorageSASService = new AzureStorageSASService(tokenCredential, ivyRestClientId, container);
		this.azureStorageBlobService = new AzureStorageBlobService(tokenCredential, ivyRestClientId, container);
		
		this.downloadLinkLiveTime = downloadLinkLiveTime;
	}
	
	@Override
	public String upload(String content, String fileName) {
		return azureStorageBlobService.upload(fileName, content);
	}

	@Override
	public String uploadFromFile(String path) {
		String fileName = FilenameUtils.getName(path);
		try {
			return azureStorageBlobService.uploadFromPath(fileName, path, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String uploadFromFile(String path, String uploadToFolder, boolean isOverwrite) {
		String fileName = FilenameUtils.getName(path);
		String blobName = createBlobPath(uploadToFolder, fileName);
		try {
			return azureStorageBlobService.uploadFromPath(blobName, path, isOverwrite);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String upload(byte[] content, String fileName) throws Exception {
		return azureStorageBlobService.uploadFromFile(fileName, content, true);
	}

	@Override
	public String upload(byte[] content, String fileName, String uploadToFolder, boolean isOverwrite) throws Exception {
		String blobName = createBlobPath(uploadToFolder, fileName);
		return azureStorageBlobService.uploadFromFile(blobName, content, isOverwrite);
	}

	@Override
	public String uploadFromUrl(String url) {
		String fileName = getFileNameFromUrl(url);
		return azureStorageBlobService.uploadFromUrl(fileName, url, true);
	}

	@Override
	public String uploadFromUrl(String url, String uploadToFolder, boolean isOverwrite) {
		String fileName = getFileNameFromUrl(url);
		String blobName = createBlobPath(uploadToFolder, fileName);

		return azureStorageBlobService.uploadFromUrl(blobName, url, isOverwrite);
	}

	@Override
	public boolean delete(String blobName) {
		return azureStorageBlobService.delete(blobName);
	}

	@Override
	public void delete(Date date) {
		List<BlobItem> bi = azureStorageContainerService.getBlobs().stream()
				.filter(b -> isSameDate(b, date))
				.collect(Collectors.toList());
		bi.forEach(blob -> delete(blob.getName()));
	}

	@Override
	public void restore(String blobName) {
		azureStorageBlobService.undelete(blobName);

	}

	@Override
	public byte[] downloadContent(String blobName) {
		byte[] content = azureStorageBlobService.downloadContent(blobName);
		return content;
	}

	@Override
	public void downloadToFile(String blobName, String filePath) {
		try {
			azureStorageBlobService.downloadToFile(blobName, filePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ByteArrayOutputStream downloadStream(String blobName) {
		return azureStorageBlobService.downloadStream(blobName);
	}

	@Override
	public String getDownloadLink(String blobName) {
		try {
			BlobItem blobItem = azureStorageBlobService.getBlob(blobName);

			String sasToken = azureStorageSASService.generateUserDelegationSas(blobName, downloadLinkLiveTime);
			String downloadLink = blobItem.getUrl() + "?" + sasToken;
			return downloadLink;
		} catch (Exception e) {
			Ivy.log().warn("Get download link for blob {0}. Error message {1}.", e, blobName, e.getMessage());
			return EMPTY;
		}
	}

	@Override
	public List<BlobItem> getBlobs() {
		List<BlobItem> blobItems = azureStorageContainerService.getBlobs();
		return blobItems;
	}

	@Override
	public boolean exists(String blobName) {
		BlobItem blobItem = azureStorageBlobService.getBlob(blobName);
		return blobItem != null;
	}

	private boolean isSameDate(BlobItem bi, Date date) {
		String creationTime2String = bi.getProperties().getCreationTime().format(DateTimeFormatter.ofPattern(DATE_PATTERN));
		String date2String =  new SimpleDateFormat(DATE_PATTERN).format(date); 
		return creationTime2String.equals(date2String);
	}
	
	private String createBlobPath(String folderName, String fileName) {
		if (isNotBlank(folderName)) {
			return String.format("%s/%s", folderName, fileName);
		}
		return fileName;
	}

	private String getFileNameFromUrl(String url) {
		try {
			return Paths.get(new URI(url).getPath()).getFileName().toString();
		} catch (URISyntaxException e) {
			Ivy.log().warn("Can not get file name from " + url);
		}
		return StringUtils.EMPTY;
	}
}
