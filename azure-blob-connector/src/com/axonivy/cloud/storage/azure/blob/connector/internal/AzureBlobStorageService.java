package com.axonivy.cloud.storage.azure.blob.connector.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import com.axonivy.cloud.storage.azure.blob.connector.StorageService;
import com.axonivy.cloud.storage.azure.blob.connector.internal.helper.BlobSASHelper;
import com.azure.core.http.rest.PagedResponse;
import com.azure.core.http.rest.Response;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobListDetails;
import com.azure.storage.blob.models.DeleteSnapshotsOptionType;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.azure.storage.blob.options.BlobDownloadToFileOptions;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.ParallelTransferOptions;

import ch.ivyteam.ivy.environment.Ivy;

/**
 * For first version, only upload file to demo-container. In next, maybe create
 * virtual directory inside container.
 */
public class AzureBlobStorageService implements StorageService {
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private BlobContainerClient destinationContainer = null;
	private BlobServiceClient blobServiceClient = null;
	private Duration downloadLinkLiveTime = Duration.ofHours(8);
	private static final long FIVE_MG = (5 * 1024 * 1024);
	
	
	/** 
	 * @param blobServiceClient - A client to interact with the Blob Service at the account level
	 * @param container - The container name
	 */
	public AzureBlobStorageService(BlobServiceClient blobServiceClient, String container) {
		this.blobServiceClient = blobServiceClient;
		this.destinationContainer = getBlobContainerClient(this.blobServiceClient, container);
	}

	public AzureBlobStorageService(BlobServiceClient blobServiceClient, String container,
			Duration downloadLinkLiveTime) {
		this.blobServiceClient = blobServiceClient;
		this.destinationContainer = getBlobContainerClient(this.blobServiceClient, container);
		this.downloadLinkLiveTime = downloadLinkLiveTime;
	}
	
	@Override
	public String upload(String content, String fileName) {
		BlockBlobClient blockBlobClient = getBlobClient(fileName).getBlockBlobClient();
		blockBlobClient.upload(BinaryData.fromString(content));
		return blockBlobClient.getBlobName();
	}

	@Override
	public String uploadFromUrl(String sourceURL, String uploadToFolder, boolean isOverwite) {
		String fileName = getFileNameFromUrl(sourceURL);
		String blobName = createBlobPath(uploadToFolder, fileName);
		BlobClient destination = getBlobClient(blobName);

		destination.getBlockBlobClient().uploadFromUrl(sourceURL, isOverwite);
		return destination.getBlockBlobClient().getBlobName();
	}

	@Override
	public String uploadFromFile(String path, String uploadToFolder, boolean isOverwite) {
		String fileName = FilenameUtils.getName(path);
		String blobName = createBlobPath(uploadToFolder, fileName);
		BlobClient blobClient = getBlobClient(blobName);
		blobClient.uploadFromFile(path, isOverwite);
		return blobClient.getBlockBlobClient().getBlobName();
	}

	@Override
	public String getDownloadLink(String blobName) {
		BlobClient blobClient = getBlobClient(blobName);
		String sasToken = BlobSASHelper.createServiceSASBlob(blobClient, this.downloadLinkLiveTime);
		String downloadLink = blobClient.getBlobUrl() + "?" + sasToken;
		return downloadLink;
	}
	
	@Override
	public String upload(byte[] content, String fileName, String uploadToFolder, boolean isOverwite) throws Exception {
		String blobName = createBlobPath(uploadToFolder, fileName);
		BlockBlobClient blockBlobClient = getBlobClient(blobName).getBlockBlobClient();

		try (ByteArrayInputStream dataStream = new ByteArrayInputStream(content)) {
			blockBlobClient.upload(dataStream, content.length, isOverwite);
			return blockBlobClient.getBlobName();
		} catch (Exception ex) {
			throw new Exception("Upload file error. Exception message: " + ex.getMessage(), ex);
		}
	}
	
	@Override
	public boolean delete(String blobName) {
		BlobClient blobClient = getBlobClient(blobName);
		
		Response<Boolean> response = blobClient.deleteIfExistsWithResponse(DeleteSnapshotsOptionType.INCLUDE, null, null, null);
	    return response.getStatusCode() != HttpStatus.SC_NOT_FOUND;
	}
	
	@Override
	public void restore(String blobName) {
		BlobClient blobClient = getBlobClient(blobName);
		blobClient.undelete();
	}
	
	@Override
	public byte[] downloadContent(String blobName) {
		BlockBlobClient blockBlobClient = getBlobClient(blobName).getBlockBlobClient();
		BinaryData data = blockBlobClient.downloadContent();
		return data.toBytes();
	}

	@Override
	public void downloadToFile(String blobName, String filePath) {
		BlockBlobClient blockBlobClient = getBlobClient(blobName).getBlockBlobClient();
		long blogSize = blockBlobClient.getProperties().getBlobSize();
		if (blogSize >= FIVE_MG) {
			downloadFileWithLargeSize(blobName, filePath);
		} else {
			blockBlobClient.downloadToFile(filePath);
		}
	}

	@Override
	public ByteArrayOutputStream downloadStream(String blobName) throws IOException {
		BlockBlobClient blockBlobClient = getBlobClient(blobName).getBlockBlobClient();
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			blockBlobClient.downloadStream(outputStream);
			return outputStream;
		} catch (IOException e) {
			throw new IOException ("download file error. Exception message: " + e.getMessage(), e);
		}
	}
	
	@Override
	public BlobClient getBlobClient(String blobName) {
		return this.destinationContainer.getBlobClient(blobName);
	}
	
	private void downloadFileWithLargeSize(String blobName, String filePath) {
		BlockBlobClient blockBlobClient = getBlobClient(blobName).getBlockBlobClient();
		ParallelTransferOptions parallelTransferOptions = new ParallelTransferOptions()
		        .setBlockSizeLong((long) (4 * 1024 * 1024)) // 4 MiB block size
		        .setMaxConcurrency(4);

		BlobDownloadToFileOptions options = new BlobDownloadToFileOptions(filePath);
		options.setParallelTransferOptions(parallelTransferOptions);

		blockBlobClient.downloadToFileWithResponse(options, null, null);
	}
	
	private BlobContainerClient getBlobContainerClient(BlobServiceClient blobServiceClient, String container) {
		if (ObjectUtils.anyNull(container)) {
			throw new NullPointerException("The container are not allow null.");
		}

		BlobContainerClient blobContainerClient = blobServiceClient.createBlobContainerIfNotExists(container);
		return blobContainerClient;
	}

	private static String getFileNameFromUrl(String url) {
		try {
			return Paths.get(new URI(url).getPath()).getFileName().toString();
		} catch (URISyntaxException e) {
			Ivy.log().warn("Can not get file name from " + url);
		}
		return StringUtils.EMPTY;
	}
	
	private String createBlobPath(String folderName, String fileName) {
		String path = StringUtils.isNotBlank(folderName) ? folderName + "/" : StringUtils.EMPTY;
		return path + fileName;
	}

	@Override
	public String uploadFromUrl(String url) {
		String blobName = getFileNameFromUrl(url);
		BlobClient destination = getBlobClient(blobName);
		destination.getBlockBlobClient().uploadFromUrl(url, false);
		return destination.getBlockBlobClient().getBlobName();
	}

	@Override
	public String uploadFromFile(String path) {
		String blobName = FilenameUtils.getName(path);
		BlobClient blobClient = getBlobClient(blobName);
		blobClient.uploadFromFile(path);
		return blobClient.getBlockBlobClient().getBlobName();
	}

	@Override
	public String upload(byte[] content, String fileName) throws Exception {
		BlockBlobClient blockBlobClient = getBlobClient(fileName).getBlockBlobClient();

		try (ByteArrayInputStream dataStream = new ByteArrayInputStream(content)) {
			blockBlobClient.upload(dataStream, content.length);
			return blockBlobClient.getBlobName();
		} catch (Exception ex) {
			throw new Exception("Upload file error. Exception message: " + ex.getMessage(), ex);
		}
	}

	@Override
	public List<BlobItem> getBlobs() {
		List<BlobItem> bis = new ArrayList<>();
		ListBlobsOptions options = new ListBlobsOptions()
				.setDetails(new BlobListDetails().setRetrieveDeletedBlobs(true));
		Iterable<PagedResponse<BlobItem>> blobPages = destinationContainer.listBlobs(options, null).iterableByPage();
		for (PagedResponse<BlobItem> page : blobPages) {
			page.getElements().forEach(blob -> bis.add(blob));
		}
		return bis;
	}
	
	@Override
	public void delete(Date d) {
		List<BlobItem> bi = destinationContainer.listBlobs().stream().filter(b -> isSameDate(b, d)).collect(Collectors.toList());
		bi.forEach(blob -> delete(blob.getName()));
	}
	
	private boolean isSameDate(BlobItem bi, Date date) {
		String creationTime2String = bi.getProperties().getCreationTime().format(DateTimeFormatter.ofPattern(DATE_PATTERN));
		String date2String =  new SimpleDateFormat(DATE_PATTERN).format(date); 
		return creationTime2String.equals(date2String);
	}
}
