package com.axonivy.connector.azure.blob.internal.model.response;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Blobs")
@XmlAccessorType(XmlAccessType.FIELD)
public final class BlobItemInternal implements Serializable {

	private static final long serialVersionUID = -1922569512589236088L;

	@XmlElement(name = "Name", required = true)
	private String name;

	@XmlElement(name = "Deleted")
	private boolean deleted;

	@XmlElement(name = "Snapshot")
	private String snapshot;

	@XmlElement(name = "VersionId")
	private String versionId;

	@XmlElement(name = "IsCurrentVersion")
	private Boolean isCurrentVersion;

	@XmlElement(name = "Properties")	
	private BlobItemPropertiesInternal properties;

	@XmlElement(name = "Metadata")
	private Map<String, String> metadata;

	@XmlElement(name = "Tags")
	private BlobTags blobTags;

	@XmlElement(name = "OrMetadata")
	private Map<String, String> objectReplicationMetadata;

	@XmlElement(name = "HasVersionsOnly")
	private Boolean hasVersionsOnly;

	@XmlElement(name = "IsPrefix")
	private Boolean isPrefix;

	public BlobItemInternal() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public Boolean getIsCurrentVersion() {
		return isCurrentVersion;
	}

	public void setIsCurrentVersion(Boolean isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
	}

	public BlobItemPropertiesInternal getProperties() {
		return properties;
	}

	public void setProperties(BlobItemPropertiesInternal properties) {
		this.properties = properties;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public BlobTags getBlobTags() {
		return blobTags;
	}

	public void setBlobTags(BlobTags blobTags) {
		this.blobTags = blobTags;
	}

	public Map<String, String> getObjectReplicationMetadata() {
		return objectReplicationMetadata;
	}

	public void setObjectReplicationMetadata(Map<String, String> objectReplicationMetadata) {
		this.objectReplicationMetadata = objectReplicationMetadata;
	}

	public Boolean getHasVersionsOnly() {
		return hasVersionsOnly;
	}

	public void setHasVersionsOnly(Boolean hasVersionsOnly) {
		this.hasVersionsOnly = hasVersionsOnly;
	}

	public Boolean getIsPrefix() {
		return isPrefix;
	}

	public void setIsPrefix(Boolean isPrefix) {
		this.isPrefix = isPrefix;
	}
}
