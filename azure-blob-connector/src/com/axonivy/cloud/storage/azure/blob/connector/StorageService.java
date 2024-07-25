package com.axonivy.cloud.storage.azure.blob.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlobItem;

public interface StorageService {
	
	/**
	 * The API to create a blob string with content
	 * @param content - The string content
	 * @param fileName - The file name
	 * @return - the blob name
	 */
	public String upload(String content, String fileName);

	/**
	 * The API to create a blob from local machine with specific path file
	 * @param path - path file
	 * @return - The blob name
	 * */
	public String uploadFromFile(String path);
	
	/**
	 * The API to create a blob from local machine with specific path file
	 * @param path - path file
	 * @param uploadToFolder - The name of folder
	 * @param isOverwrite - boolean
	 * @return - The blob name
	 * */
	public String uploadFromFile(String path, String uploadToFolder, boolean isOverwite);
	
	/**
	 * The API to create a blob from GUI with upload function
	 * @param content - byte[]
	 * @param fileName - file name
	 * @return - The blob name
	 * */
	public String upload(byte[] content, String fileName) throws Exception;
	
	/**
	 * The API to create a blob from GUI with upload function
	 * @param content - byte[]
	 * @param fileName - file name
	 * @param uploadToFolder - The name of folder
	 * @param isOverwrite - boolean
	 * @return - The blob name
	 * */
	public String upload(byte[] content, String fileName, String uploadToFolder, boolean isOverwite) throws Exception;
	
	/**
	 * The API to copy operation from a source object URL
	 * @param url - The source URL to upload from
	 * @return - The blob name
	 */
	public String uploadFromUrl(String url);
	
	/**
	 * The API to copy operation from a source object URL
	 * @param url - The source URL to upload from
	 * @param uploadToFolder - The name of folder
	 * @param isOverwrite - boolean
	 * @return - The blob name
	 */
	public String uploadFromUrl(String url, String uploadToFolder, boolean isOverwite);
	
	/**
	 * The API to delete blob
	 * @param blob name
	 * @return - boolean
	 * */
	public boolean delete(String blobName);
	
	/**
	 * The API to delete blob
	 * @param specific date to delete all blobs
	 * */
	public void delete(Date date);
	
	/**
	 * The API to restore an deleted blob if soft deleted for blobs is enable.
	 * @param blob name
	 * */
	public void restore(String blobName);
	
	/**
	 * The API to download content
	 * @param blob name
	 * @return bye[]
	 * */
	public byte[] downloadContent(String blobName);
	
	/**
	 * The API to download from file
	 * @param blob name
	 * @param filePath 
	 * */
	public void downloadToFile(String blobName, String filePath);
	
	/**
	 * The API to download to a stream
	 * @param blob name
	 * */
	public ByteArrayOutputStream downloadStream(String blobName) throws IOException;
	
	/**
	 * The API to get the list blob
	 * */
	public List<BlobItem> getBlobs();
	
	/**
	 * The API to create a temporary download link with expired time 
	 * @param blobName - The blob name
	 * @return - The url for download
	 */
	public String getDownloadLink(String blobName);
	
	/**
	 * The API to get blob client
	 * @param blobName - The blob name
	 * @return BlobClient
	 * */
	public BlobClient getBlobClient(String blobName);
}
