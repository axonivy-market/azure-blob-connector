package com.axonivy.connector.azure.blob.demo.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.azure.blob.model.BlobItem;

public class UploadByCallSubprocess extends AbstractDemoBean {

	private List<BlobItem> blobItems;

	public void init() {
		super.init();
	}

	public List<BlobItem> getBlobItems() {
		return blobItems;
	}

	public void setBlobItems(List<BlobItem> blobItems) {
		this.blobItems = blobItems;
	}

	public void getBlobs(List<BlobItem> blobItems) {
		blobs = new ArrayList<>();
		for (BlobItem item : blobItems) {
			Blob b = new Blob();
			b.setBlobItem(item);
			blobs.add(b);
		}
	}

	public String createBlobPath(String folderName, String fileName) {
		String path = StringUtils.isNotBlank(folderName) ? folderName + "/" : StringUtils.EMPTY;
		return path + fileName;
	}

	public void showCopiedMessage() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Download link is copied", null));
	}
}
