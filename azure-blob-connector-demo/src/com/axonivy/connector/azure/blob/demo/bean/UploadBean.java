package com.axonivy.connector.azure.blob.demo.bean;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.axonivy.connector.azure.blob.StorageService;
import com.axonivy.connector.azure.blob.demo.utils.UploadUtils;
import com.axonivy.connector.azure.blob.internal.AzureBlobStorageService;
import com.axonivy.connector.azure.blob.internal.auth.ClientSecretCredential;
import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.model.BlobItem;

import ch.ivyteam.ivy.environment.Ivy;

public class UploadBean extends AbstractDemoBean {

	private StorageService storageService = null;

	public void init() {
		super.init();

		String clientId = Ivy.var().get("AzureBlob.ClientId");
		String clientSecret = Ivy.var().get("AzureBlob.ClientSecret");
		String tenantId = Ivy.var().get("AzureBlob.TenantId");
		String containerName = Ivy.var().get("AzureBlob.ContainterName");
		String restClientUUID = Ivy.var().get("AzureBlob.RestClientUUID");

		Credential credential = new ClientSecretCredential(tenantId, clientId, clientSecret);
		storageService = new AzureBlobStorageService(credential, UUID.fromString(restClientUUID), containerName);

		this.blobs = getAllBlobs();
	}

	public void upload() throws Exception {

		isFileAlreadyExist = isFileAlreadyExist(uploadToFolderByPrimefaces, fileName, isOverwriteFile);
		if (isFileAlreadyExist) {
			return;
		}

		String name = storageService.upload(content, fileName, uploadToFolderByPrimefaces, isOverwriteFile);
		if (isNotEmpty(name)) {
			fetchAllBlobsAndAddMessage("Uploaded blobs successfully");
		}
	}

	public void uploadFromURL() {
		fileNameURL = UploadUtils.getFileNameFromUrl(url);

		isFileAlreadyExistURL = isFileAlreadyExist(uploadToFolderByURL, fileNameURL, isOverwriteFileURL);

		if (isFileAlreadyExistURL) {
			return;
		}

		String name = storageService.uploadFromUrl(url, uploadToFolderByURL, isOverwriteFileURL);
		if (isNotEmpty(name)) {
			fetchAllBlobsAndAddMessage("Uploaded blobs successfully");
		}
	}

	public void uploadFromPath() {
		fileNamePath = FilenameUtils.getName(localPath);

		isFileAlreadyExistPath = isFileAlreadyExist(uploadToFolderByLocalPath, fileNamePath, isOverwriteFilePath);
		if (isFileAlreadyExistPath) {
			return;
		}

		String name = storageService.uploadFromFile(localPath, uploadToFolderByLocalPath, isOverwriteFilePath);
		if (isNotEmpty(name)) {
			fetchAllBlobsAndAddMessage("Uploaded blobs successfully");
		}
	}

	public void deleteBlobs(Date date) {
		storageService.delete(date);
		fetchAllBlobsAndAddMessage("Delete blobs successfully");
	}

	public void deleteBlob(String blobName) {
		if (storageService.delete(blobName)) {
			fetchAllBlobsAndAddMessage("Delete blobs successfully");
		}
	}

	public void undeleteBlob(String blobName) {
		storageService.restore(blobName);
		fetchAllBlobsAndAddMessage("Undelete blobs successfully");
	}

	public void getDownloadLink(Blob blob) {
		String downloadLink = storageService.getDownloadLink(blob.getBlobItem().getName());
		blob.setLinkDownLoad(downloadLink);
	}

	public void showCopiedMessage() {
		doAddInfoMessageForInstance("Download link is copied");
	}

	public StreamedContent downloadFile(Blob blob) throws IOException {
		var data = storageService.downloadStream(blob.getBlobItem().getName());

		InputStream is = new ByteArrayInputStream(data.toByteArray());

		return DefaultStreamedContent.builder().name(blob.getBlobItem().getName())
				.contentType(blob.getBlobItem().getProperties().getContentType()).stream(() -> is).build();
	}

	public void downloadToFile(Blob blob, String filePath) {
		storageService.downloadToFile(blob.getBlobItem().getName(), filePath);
	}

	private void fetchAllBlobsAndAddMessage(String message) {
		this.blobs = getAllBlobs();
		doAddInfoMessageForInstance(message);
	}

	private List<Blob> getAllBlobs() {
		List<BlobItem> blobItems = storageService.getBlobs();
		List<Blob> blobs = new ArrayList<>();
		for (BlobItem item : blobItems) {
			Blob b = new Blob();
			b.setBlobItem(item);
			b.setLinkDownLoad(null);
			blobs.add(b);
		}
		return blobs;
	}

	private boolean isFileAlreadyExist(String folderName, String name, boolean isOverwriteFile) {
		if (isOverwriteFile) {
			return false;
		}

		String blobName = isNotBlank(folderName) ? String.format("%s/%s", folderName, name) : name;
		return storageService.exists(blobName);
	}

	private void doAddInfoMessageForInstance(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	}
}
