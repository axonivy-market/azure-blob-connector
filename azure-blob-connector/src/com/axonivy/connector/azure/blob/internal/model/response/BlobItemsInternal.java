package com.axonivy.connector.azure.blob.internal.model.response;

import java.io.Serializable;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Blobs")
@XmlAccessorType(XmlAccessType.FIELD)
public final class BlobItemsInternal implements Serializable {

	private static final long serialVersionUID = 1861446222645013914L;

	@XmlElement(name = "Blob")
	private List<BlobItemInternal> blobItemInternals;

	public BlobItemsInternal() {
	}

	public List<BlobItemInternal> getBlobItemInternals() {
		return blobItemInternals;
	}

	public void setBlobItemInternals(List<BlobItemInternal> blobItemInternals) {
		this.blobItemInternals = blobItemInternals;
	}
}
