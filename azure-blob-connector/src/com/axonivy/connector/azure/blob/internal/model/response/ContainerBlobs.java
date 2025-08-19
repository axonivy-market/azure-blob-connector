package com.axonivy.connector.azure.blob.internal.model.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EnumerationResults")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContainerBlobs {

	@XmlElement(name = "Prefix")
	private String prefix;

	@XmlElement(name = "Marker")
	private String marker;

	@XmlElement(name = "MaxResults")
	private String daxResults;

	@XmlElement(name = "Delimiter")
	private String delimiter;

	@XmlElement(name = "Blobs")
	private BlobItemsInternal blobItemsInternal;

	public ContainerBlobs() {
		super();
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getDaxResults() {
		return daxResults;
	}

	public void setDaxResults(String daxResults) {
		this.daxResults = daxResults;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public BlobItemsInternal getBlobItemsInternal() {
		return blobItemsInternal;
	}

	public void setBlobItemsInternal(BlobItemsInternal blobItemsInternal) {
		this.blobItemsInternal = blobItemsInternal;
	}
}
