package com.axonivy.cloud.storage.bean;

import com.azure.storage.blob.models.BlobItem;

public class Blob {
	private BlobItem bi;
	private String linkDownLoad;
	
	public BlobItem getBi() {
		return bi;
	}
	public void setBi(BlobItem bi) {
		this.bi = bi;
	}
	public String getLinkDownLoad() {
		return linkDownLoad;
	}
	public void setLinkDownLoad(String linkDownLoad) {
		this.linkDownLoad = linkDownLoad;
	}
}
