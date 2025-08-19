package com.axonivy.connector.azure.blob.internal.model;

public class UserDelegationSign {
	private String signedPermissions;
	private String signedStart;
	private String signedExpiry;
	private String canonicalizedResource;
	
	private String signedKeyObjectId;
	private String signedKeyTenantId;
	private String signedKeyStart;
	private String signedKeyExpiry;
	private String signedKeyService;
	private String signedKeyVersion;
	
	private String signedAuthorizedUserObjectId;
	private String signedUnauthorizedUserObjectId;
	private String signedCorrelationId;
	private String signedIP;
	private String signedProtocol;
	private String signedVersion;
	private String signedResource;
	private String signedSnapshotTime;
	private String signedEncryptionScope;
	// rscc
	private String cacheControl;
	// rscd
	private String contentDisposition;
	// rsce
	private String contentEncoding;
	// rscl
	private String contentLanguage;
	// rsct
	private String contentType;

	public String getSignedPermissions() {
		return signedPermissions;
	}

	public void setSignedPermissions(String signedPermissions) {
		this.signedPermissions = signedPermissions;
	}

	public String getSignedStart() {
		return signedStart;
	}

	public void setSignedStart(String signedStart) {
		this.signedStart = signedStart;
	}

	public String getSignedExpiry() {
		return signedExpiry;
	}

	public void setSignedExpiry(String signedExpiry) {
		this.signedExpiry = signedExpiry;
	}

	public String getCanonicalizedResource() {
		return canonicalizedResource;
	}

	public void setCanonicalizedResource(String canonicalizedResource) {
		this.canonicalizedResource = canonicalizedResource;
	}

	public String getSignedKeyObjectId() {
		return signedKeyObjectId;
	}

	public void setSignedKeyObjectId(String signedKeyObjectId) {
		this.signedKeyObjectId = signedKeyObjectId;
	}

	public String getSignedKeyTenantId() {
		return signedKeyTenantId;
	}

	public void setSignedKeyTenantId(String signedKeyTenantId) {
		this.signedKeyTenantId = signedKeyTenantId;
	}

	public String getSignedKeyStart() {
		return signedKeyStart;
	}

	public void setSignedKeyStart(String signedKeyStart) {
		this.signedKeyStart = signedKeyStart;
	}

	public String getSignedKeyExpiry() {
		return signedKeyExpiry;
	}

	public void setSignedKeyExpiry(String signedKeyExpiry) {
		this.signedKeyExpiry = signedKeyExpiry;
	}

	public String getSignedKeyService() {
		return signedKeyService;
	}

	public void setSignedKeyService(String signedKeyService) {
		this.signedKeyService = signedKeyService;
	}

	public String getSignedKeyVersion() {
		return signedKeyVersion;
	}

	public void setSignedKeyVersion(String signedKeyVersion) {
		this.signedKeyVersion = signedKeyVersion;
	}

	public String getSignedAuthorizedUserObjectId() {
		return signedAuthorizedUserObjectId;
	}

	public void setSignedAuthorizedUserObjectId(String signedAuthorizedUserObjectId) {
		this.signedAuthorizedUserObjectId = signedAuthorizedUserObjectId;
	}

	public String getSignedUnauthorizedUserObjectId() {
		return signedUnauthorizedUserObjectId;
	}

	public void setSignedUnauthorizedUserObjectId(String signedUnauthorizedUserObjectId) {
		this.signedUnauthorizedUserObjectId = signedUnauthorizedUserObjectId;
	}

	public String getSignedCorrelationId() {
		return signedCorrelationId;
	}

	public void setSignedCorrelationId(String signedCorrelationId) {
		this.signedCorrelationId = signedCorrelationId;
	}

	public String getSignedIP() {
		return signedIP;
	}

	public void setSignedIP(String signedIP) {
		this.signedIP = signedIP;
	}

	public String getSignedProtocol() {
		return signedProtocol;
	}

	public void setSignedProtocol(String signedProtocol) {
		this.signedProtocol = signedProtocol;
	}

	public String getSignedVersion() {
		return signedVersion;
	}

	public void setSignedVersion(String signedVersion) {
		this.signedVersion = signedVersion;
	}

	public String getSignedResource() {
		return signedResource;
	}

	public void setSignedResource(String signedResource) {
		this.signedResource = signedResource;
	}

	public String getSignedSnapshotTime() {
		return signedSnapshotTime;
	}

	public void setSignedSnapshotTime(String signedSnapshotTime) {
		this.signedSnapshotTime = signedSnapshotTime;
	}

	public String getSignedEncryptionScope() {
		return signedEncryptionScope;
	}

	public void setSignedEncryptionScope(String signedEncryptionScope) {
		this.signedEncryptionScope = signedEncryptionScope;
	}

	public String getCacheControl() {
		return cacheControl;
	}

	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
