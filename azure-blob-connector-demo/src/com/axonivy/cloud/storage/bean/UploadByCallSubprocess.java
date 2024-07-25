package com.axonivy.cloud.storage.bean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.file.UploadedFile;

import com.azure.storage.blob.models.BlobItem;

public class UploadByCallSubprocess {
	private String url;
	private String localPath;
	private UploadedFile uploadedFile;
	private String fileName;
	private String fileNamePath;
	private String fileNameURL;
	private byte[] content;
	private InputStream inputStreamContent;
	private List<Blob> blobs;
	private List<BlobItem> blobItems;
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

	public void init() {
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

	public List<BlobItem> getBlobItems() {
		return blobItems;
	}

	public void setBlobItems(List<BlobItem> blobItems) {
		this.blobItems = blobItems;
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

	public void getBlobs(List<BlobItem> bis) {
		blobs = new ArrayList<>();
		for (BlobItem item : bis) {
			Blob b = new Blob();
			b.setBi(item);
			blobs.add(b);
		}
	}
	
	public String createBlobPath(String folderName, String fileName) {
		String path = StringUtils.isNotBlank(folderName) ? folderName + "/" : StringUtils.EMPTY;
		return path + fileName;
	}
}
