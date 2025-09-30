package com.axonivy.connector.azure.blob.internal.model.response;

import java.io.Serializable;
import java.time.OffsetDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.axonivy.connector.azure.blob.internal.enums.BlobImmutabilityPolicyMode;
import com.axonivy.connector.azure.blob.internal.enums.BlobType;
import com.axonivy.connector.azure.blob.internal.enums.CopyStatusType;
import com.axonivy.connector.azure.blob.internal.enums.LeaseDurationType;
import com.axonivy.connector.azure.blob.internal.enums.LeaseStateType;
import com.axonivy.connector.azure.blob.internal.enums.LeaseStatusType;
import com.axonivy.connector.azure.blob.internal.helper.OffsetDateTimeRFC1123Adapter;

@XmlRootElement(name = "Properties")
@XmlAccessorType(XmlAccessType.FIELD)
public final class BlobItemPropertiesInternal implements Serializable {

	private static final long serialVersionUID = 3086318684770966546L;

	@XmlElement(name = "Creation-Time")
	@XmlJavaTypeAdapter(OffsetDateTimeRFC1123Adapter.class)
	private OffsetDateTime creationTime;

	@XmlElement(name = "Last-Modified", required = true)
	@XmlJavaTypeAdapter(OffsetDateTimeRFC1123Adapter.class)
	private OffsetDateTime lastModified;

	@XmlElement(name = "Etag", required = true)
	private String eTag;

	@XmlElement(name = "Content-Length")
	private Long contentLength;

	@XmlElement(name = "Content-Type")
	private String contentType;

	@XmlElement(name = "Content-Encoding")
	private String contentEncoding;

	@XmlElement(name = "Content-Language")
	private String contentLanguage;

	@XmlElement(name = "Content-MD5")
	private byte[] contentMd5;

	@XmlElement(name = "Content-Disposition")
	private String contentDisposition;

	@XmlElement(name = "Cache-Control")
	private String cacheControl;

	@XmlElement(name = "x-ms-blob-sequence-number")
	private Long blobSequenceNumber;

	@XmlElement(name = "BlobType")
	private BlobType blobType;

	@XmlElement(name = "LeaseStatus")
	private LeaseStatusType leaseStatus;

	@XmlElement(name = "LeaseState")
	private LeaseStateType leaseState;

	@XmlElement(name = "LeaseDuration")
	private LeaseDurationType leaseDuration;

	@XmlElement(name = "CopyId")
	private String copyId;

	@XmlElement(name = "CopyStatus")
	private CopyStatusType copyStatus;

	@XmlElement(name = "CopySource")
	private String copySource;

	@XmlElement(name = "CopyProgress")
	private String copyProgress;

	@XmlElement(name = "CopyCompletionTime")
	@XmlJavaTypeAdapter(OffsetDateTimeRFC1123Adapter.class)
	private OffsetDateTime copyCompletionTime;

	@XmlElement(name = "CopyStatusDescription")
	private String copyStatusDescription;

	@XmlElement(name = "ServerEncrypted")
	private Boolean serverEncrypted;

	@XmlElement(name = "IncrementalCopy")
	private Boolean incrementalCopy;

	@XmlElement(name = "DestinationSnapshot")
	private String destinationSnapshot;

	@XmlElement(name = "DeletedTime")
	private OffsetDateTime deletedTime;

	@XmlElement(name = "RemainingRetentionDays")
	private Integer remainingRetentionDays;

	@XmlElement(name = "AccessTier")
	private String accessTier;

	@XmlElement(name = "AccessTierInferred")
	private Boolean accessTierInferred;

	@XmlElement(name = "ArchiveStatus")
	private String archiveStatus;

	@XmlElement(name = "CustomerProvidedKeySha256")
	private String customerProvidedKeySha256;

	@XmlElement(name = "EncryptionScope")
	private String encryptionScope;

	@XmlElement(name = "AccessTierChangeTime")
	@XmlJavaTypeAdapter(OffsetDateTimeRFC1123Adapter.class)
	private OffsetDateTime accessTierChangeTime;

	@XmlElement(name = "TagCount")
	private Integer tagCount;

	@XmlElement(name = "Expiry-Time")
	@XmlJavaTypeAdapter(OffsetDateTimeRFC1123Adapter.class)
	private OffsetDateTime expiresOn;

	@XmlElement(name = "Sealed")
	private Boolean isSealed;

	@XmlElement(name = "RehydratePriority")
	private String rehydratePriority;

	@XmlElement(name = "LastAccessTime")
	@XmlJavaTypeAdapter(OffsetDateTimeRFC1123Adapter.class)
	private OffsetDateTime lastAccessedOn;

	@XmlElement(name = "ImmutabilityPolicyUntilDate")
	@XmlJavaTypeAdapter(OffsetDateTimeRFC1123Adapter.class)
	private OffsetDateTime immutabilityPolicyExpiresOn;

	@XmlElement(name = "ImmutabilityPolicyMode")
	private BlobImmutabilityPolicyMode immutabilityPolicyMode;

	@XmlElement(name = "LegalHold")
	private Boolean legalHold;

	public BlobItemPropertiesInternal() {
		super();
	}

	public OffsetDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(OffsetDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public OffsetDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(OffsetDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public String getContentLanguage() {
		return contentLanguage;
	}

	public void setContentLanguage(String contentLanguage) {
		this.contentLanguage = contentLanguage;
	}

	public byte[] getContentMd5() {
		return contentMd5;
	}

	public void setContentMd5(byte[] contentMd5) {
		this.contentMd5 = contentMd5;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public String getCacheControl() {
		return cacheControl;
	}

	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}

	public Long getBlobSequenceNumber() {
		return blobSequenceNumber;
	}

	public void setBlobSequenceNumber(Long blobSequenceNumber) {
		this.blobSequenceNumber = blobSequenceNumber;
	}

	public BlobType getBlobType() {
		return blobType;
	}

	public void setBlobType(BlobType blobType) {
		this.blobType = blobType;
	}

	public LeaseStatusType getLeaseStatus() {
		return leaseStatus;
	}

	public void setLeaseStatus(LeaseStatusType leaseStatus) {
		this.leaseStatus = leaseStatus;
	}

	public LeaseStateType getLeaseState() {
		return leaseState;
	}

	public void setLeaseState(LeaseStateType leaseState) {
		this.leaseState = leaseState;
	}

	public LeaseDurationType getLeaseDuration() {
		return leaseDuration;
	}

	public void setLeaseDuration(LeaseDurationType leaseDuration) {
		this.leaseDuration = leaseDuration;
	}

	public String getCopyId() {
		return copyId;
	}

	public void setCopyId(String copyId) {
		this.copyId = copyId;
	}

	public CopyStatusType getCopyStatus() {
		return copyStatus;
	}

	public void setCopyStatus(CopyStatusType copyStatus) {
		this.copyStatus = copyStatus;
	}

	public String getCopySource() {
		return copySource;
	}

	public void setCopySource(String copySource) {
		this.copySource = copySource;
	}

	public String getCopyProgress() {
		return copyProgress;
	}

	public void setCopyProgress(String copyProgress) {
		this.copyProgress = copyProgress;
	}

	public OffsetDateTime getCopyCompletionTime() {
		return copyCompletionTime;
	}

	public void setCopyCompletionTime(OffsetDateTime copyCompletionTime) {
		this.copyCompletionTime = copyCompletionTime;
	}

	public String getCopyStatusDescription() {
		return copyStatusDescription;
	}

	public void setCopyStatusDescription(String copyStatusDescription) {
		this.copyStatusDescription = copyStatusDescription;
	}

	public Boolean getServerEncrypted() {
		return serverEncrypted;
	}

	public void setServerEncrypted(Boolean serverEncrypted) {
		this.serverEncrypted = serverEncrypted;
	}

	public Boolean getIncrementalCopy() {
		return incrementalCopy;
	}

	public void setIncrementalCopy(Boolean incrementalCopy) {
		this.incrementalCopy = incrementalCopy;
	}

	public String getDestinationSnapshot() {
		return destinationSnapshot;
	}

	public void setDestinationSnapshot(String destinationSnapshot) {
		this.destinationSnapshot = destinationSnapshot;
	}

	public OffsetDateTime getDeletedTime() {
		return deletedTime;
	}

	public void setDeletedTime(OffsetDateTime deletedTime) {
		this.deletedTime = deletedTime;
	}

	public Integer getRemainingRetentionDays() {
		return remainingRetentionDays;
	}

	public void setRemainingRetentionDays(Integer remainingRetentionDays) {
		this.remainingRetentionDays = remainingRetentionDays;
	}

	public String getAccessTier() {
		return accessTier;
	}

	public void setAccessTier(String accessTier) {
		this.accessTier = accessTier;
	}

	public Boolean getAccessTierInferred() {
		return accessTierInferred;
	}

	public void setAccessTierInferred(Boolean accessTierInferred) {
		this.accessTierInferred = accessTierInferred;
	}

	public String getArchiveStatus() {
		return archiveStatus;
	}

	public void setArchiveStatus(String archiveStatus) {
		this.archiveStatus = archiveStatus;
	}

	public String getCustomerProvidedKeySha256() {
		return customerProvidedKeySha256;
	}

	public void setCustomerProvidedKeySha256(String customerProvidedKeySha256) {
		this.customerProvidedKeySha256 = customerProvidedKeySha256;
	}

	public String getEncryptionScope() {
		return encryptionScope;
	}

	public void setEncryptionScope(String encryptionScope) {
		this.encryptionScope = encryptionScope;
	}

	public OffsetDateTime getAccessTierChangeTime() {
		return accessTierChangeTime;
	}

	public void setAccessTierChangeTime(OffsetDateTime accessTierChangeTime) {
		this.accessTierChangeTime = accessTierChangeTime;
	}

	public Integer getTagCount() {
		return tagCount;
	}

	public void setTagCount(Integer tagCount) {
		this.tagCount = tagCount;
	}

	public OffsetDateTime getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(OffsetDateTime expiresOn) {
		this.expiresOn = expiresOn;
	}

	public Boolean getIsSealed() {
		return isSealed;
	}

	public void setIsSealed(Boolean isSealed) {
		this.isSealed = isSealed;
	}

	public String getRehydratePriority() {
		return rehydratePriority;
	}

	public void setRehydratePriority(String rehydratePriority) {
		this.rehydratePriority = rehydratePriority;
	}

	public OffsetDateTime getLastAccessedOn() {
		return lastAccessedOn;
	}

	public void setLastAccessedOn(OffsetDateTime lastAccessedOn) {
		this.lastAccessedOn = lastAccessedOn;
	}

	public OffsetDateTime getImmutabilityPolicyExpiresOn() {
		return immutabilityPolicyExpiresOn;
	}

	public void setImmutabilityPolicyExpiresOn(OffsetDateTime immutabilityPolicyExpiresOn) {
		this.immutabilityPolicyExpiresOn = immutabilityPolicyExpiresOn;
	}

	public BlobImmutabilityPolicyMode getImmutabilityPolicyMode() {
		return immutabilityPolicyMode;
	}

	public void setImmutabilityPolicyMode(BlobImmutabilityPolicyMode immutabilityPolicyMode) {
		this.immutabilityPolicyMode = immutabilityPolicyMode;
	}

	public Boolean getLegalHold() {
		return legalHold;
	}

	public void setLegalHold(Boolean legalHold) {
		this.legalHold = legalHold;
	}
}
