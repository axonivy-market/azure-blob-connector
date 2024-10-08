package com.axonivy.connector.azure.blob.demo.bean;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.file.UploadedFile;

import com.axonivy.connector.azure.blob.BlobServiceClientHelper;
import com.axonivy.connector.azure.blob.StorageService;
import com.axonivy.connector.azure.blob.demo.utils.UploadUtils;
import com.axonivy.connector.azure.blob.internal.AzureBlobStorageService;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;

import ch.ivyteam.ivy.environment.Ivy;

public class UploadBean {
	private String url;
	private String localPath;
	private UploadedFile uploadedFile;
	private String fileName;
	private String fileNamePath;
	private String fileNameURL;
	private byte[] content;
	private InputStream inputStreamContent;
	private List<Blob> blobs;
	private String uploadToFolderByPrimefaces;
	private String uploadToFolderByLocalPath;
	private String uploadToFolderByURL;
	private Date startDate;
	private String blobName;
	private Boolean isFileAlreadyExist;
	private Boolean isOverwriteFile;
	
	private Boolean isFileAlreadyExistURL;
	private Boolean isOverwriteFileURL;
	
	private Boolean isFileAlreadyExistPath;
	private Boolean isOverwriteFilePath;
	
	private StorageService storageService = null;
	private static BlobServiceClient blobServiceClient = null;
	
	
	public void init() {
		String clientId = Ivy.var().get("AzureBlob.ClientId");
		String clientSecret = Ivy.var().get("AzureBlob.ClientSecret");
		String tenantId = Ivy.var().get("AzureBlob.TenantId");
		String endPoint = Ivy.var().get("AzureBlob.EndPoint");
		String containerName = Ivy.var().get("AzureBlob.ContainterName");
		
		blobServiceClient = BlobServiceClientHelper.getBlobServiceClient(clientId,  clientSecret, tenantId, endPoint);
		storageService = new AzureBlobStorageService(blobServiceClient, containerName);
		this.blobs = getAllBlobs();
		isFileAlreadyExist = false;
		isFileAlreadyExistURL = false;
		isFileAlreadyExistPath = false;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public List<Blob> getBlobs() {
		return blobs;
	}

	public void setBlobs(List<Blob> blobs) {
		this.blobs = blobs;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public InputStream getInputStreamContent() {
		return inputStreamContent;
	}

	public void setInputStreamContent(InputStream inputStreamContent) {
		this.inputStreamContent = inputStreamContent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileNamePath() {
		return fileNamePath;
	}

	public void setFileNamePath(String fileNamePath) {
		this.fileNamePath = fileNamePath;
	}

	public String getFileNameURL() {
		return fileNameURL;
	}

	public void setFileNameURL(String fileNameURL) {
		this.fileNameURL = fileNameURL;
	}

	public String getBlobName() {
		return blobName;
	}

	public void setBlobName(String blobName) {
		this.blobName = blobName;
	}

	public Boolean getIsFileAlreadyExist() {
		return isFileAlreadyExist;
	}

	public void setIsFileAlreadyExist(Boolean isFileAlreadyExist) {
		this.isFileAlreadyExist = isFileAlreadyExist;
	}

	public Boolean getIsOverwriteFile() {
		return isOverwriteFile;
	}

	public void setIsOverwriteFile(Boolean isOverwriteFile) {
		this.isOverwriteFile = isOverwriteFile;
	}

	public Boolean getIsFileAlreadyExistURL() {
		return isFileAlreadyExistURL;
	}

	public void setIsFileAlreadyExistURL(Boolean isFileAlreadyExistURL) {
		this.isFileAlreadyExistURL = isFileAlreadyExistURL;
	}

	public Boolean getIsOverwriteFileURL() {
		return isOverwriteFileURL;
	}

	public void setIsOverwriteFileURL(Boolean isOverwriteFileURL) {
		this.isOverwriteFileURL = isOverwriteFileURL;
	}

	public Boolean getIsFileAlreadyExistPath() {
		return isFileAlreadyExistPath;
	}

	public void setIsFileAlreadyExistPath(Boolean isFileAlreadyExistPath) {
		this.isFileAlreadyExistPath = isFileAlreadyExistPath;
	}

	public Boolean getIsOverwriteFilePath() {
		return isOverwriteFilePath;
	}

	public void setIsOverwriteFilePath(Boolean isOverwriteFilePath) {
		this.isOverwriteFilePath = isOverwriteFilePath;
	}

	public StorageService getStorageService() {
		return storageService;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}

	public String getUploadToFolderByPrimefaces() {
		return uploadToFolderByPrimefaces;
	}

	public void setUploadToFolderByPrimefaces(String uploadToFolderByPrimefaces) {
		this.uploadToFolderByPrimefaces = uploadToFolderByPrimefaces;
	}

	public String getUploadToFolderByLocalPath() {
		return uploadToFolderByLocalPath;
	}

	public void setUploadToFolderByLocalPath(String uploadToFolderByLocalPath) {
		this.uploadToFolderByLocalPath = uploadToFolderByLocalPath;
	}

	public String getUploadToFolderByURL() {
		return uploadToFolderByURL;
	}

	public void setUploadToFolderByURL(String uploadToFolderByURL) {
		this.uploadToFolderByURL = uploadToFolderByURL;
	}

	public void upload() throws Exception {
		boolean isExistFile = checkFileAlreadyExist(fileName);
		if (!isExistFile || isOverwriteFile) {
			try {
				String name = storageService.upload(content, fileName, uploadToFolderByPrimefaces, isOverwriteFile);
				if (StringUtils.isNotEmpty(name)) {
					fetchAllBlobsAndAddMessage("Uploaded blobs successfully");					
				}
			} catch (Exception e) {
				throw new Exception("upload file error " + e.getMessage(), e);
			}
		} else {
			isFileAlreadyExist = true;
		}
	}
	
	public void uploadFromURL() {
		fileNameURL = UploadUtils.getFileNameFromUrl(url);
		boolean isExistFile = checkFileAlreadyExist(fileNameURL);
		if (!isExistFile || isOverwriteFileURL) {
			String name = storageService.uploadFromUrl(url, uploadToFolderByURL, isOverwriteFileURL);
			if (StringUtils.isNotEmpty(name)) {
				fetchAllBlobsAndAddMessage("Uploaded blobs successfully");				
			}
		} else {
			isFileAlreadyExistURL = true;
		}
	}
	
	public void uploadFromPath() {
		fileNamePath = FilenameUtils.getName(localPath);
		boolean isExistFile = checkFileAlreadyExist(fileNamePath);
		if (!isExistFile || isOverwriteFilePath) {
			String name = storageService.uploadFromFile(localPath, uploadToFolderByLocalPath, isOverwriteFilePath);
			if (isNotEmpty(name)) {
				fetchAllBlobsAndAddMessage("Uploaded blobs successfully");
			}
		} else {
			isFileAlreadyExistPath = true;
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
	
	public void showCopiedMessage() {
		doAddInfoMessageForInstance("Download link is copied");
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
			b.setLinkDownLoad(storageService.getDownloadLink(item.getName()));
			blobs.add(b);
		}
		return blobs;
	}
	
	private Boolean checkFileAlreadyExist(String name) {
		return storageService.getBlobs().stream().anyMatch(b -> b.getName().equals(name));
	}
	
	private void doAddInfoMessageForInstance(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	}
}

