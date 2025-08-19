package com.axonivy.connector.azure.blob.demo.bean;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.primefaces.model.file.UploadedFile;

abstract class AbstractDemoBean {
	protected String url;
	protected String localPath;
	protected UploadedFile uploadedFile;
	protected String fileName;
	protected String fileNamePath;
	protected String fileNameURL;
	protected byte[] content;
	protected InputStream inputStreamContent;
	protected List<Blob> blobs;
	protected String uploadToFolderByPrimefaces;
	protected String uploadToFolderByLocalPath;
	protected String uploadToFolderByURL;
	protected Date startDate;
	protected String blobName;
	protected Boolean isFileAlreadyExist;
	protected Boolean isOverwriteFile;
	
	protected Boolean isFileAlreadyExistURL;
	protected Boolean isOverwriteFileURL;
	
	protected Boolean isFileAlreadyExistPath;
	protected Boolean isOverwriteFilePath;
	
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
}

