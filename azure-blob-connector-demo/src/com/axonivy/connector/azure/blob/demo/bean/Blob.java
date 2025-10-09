package com.axonivy.connector.azure.blob.demo.bean;

import com.axonivy.connector.azure.blob.demo.utils.FileTypeUtils;
import com.axonivy.connector.azure.blob.model.BlobItem;

public class Blob {
	private BlobItem blobItem;
	private String linkDownLoad;

	public BlobItem getBlobItem() {
		return blobItem;
	}

	public void setBlobItem(BlobItem BlobItem) {
		this.blobItem = BlobItem;
	}

	public String getLinkDownLoad() {
		return linkDownLoad;
	}

	public void setLinkDownLoad(String linkDownLoad) {
		this.linkDownLoad = linkDownLoad;
	}

	public boolean getIsImage() {
		return FileTypeUtils.isImage(blobItem.getName());
	}
}
