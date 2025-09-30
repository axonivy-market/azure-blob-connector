package com.axonivy.connector.azure.blob.internal.model.response;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "Tags")
@XmlAccessorType(XmlAccessType.FIELD)
public final class BlobTags implements Serializable {

	private static final long serialVersionUID = 8166874613358489776L;

	@JsonProperty(value = "TagSet", required = true)
	private List<BlobTag> blobTagSet;

	public BlobTags() {
		super();
		this.blobTagSet = Collections.emptyList();
	}

	public List<BlobTag> getBlobTagSet() {
		return blobTagSet;
	}

	public void setBlobTagSet(List<BlobTag> blobTagSet) {
		this.blobTagSet = blobTagSet;
	}
}
