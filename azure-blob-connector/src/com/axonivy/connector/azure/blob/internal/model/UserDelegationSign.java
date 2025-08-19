package com.axonivy.connector.azure.blob.internal.model;

public class UserDelegationSign {
	private String startTime;
	private String expiryTime;
	private String permissions;
	private String signedObjectId;
	private String signedTenantId;
	private String signedStart;
	private String signedExpiry;
	private String signedService;
	private String signedVersion;
	private String authorizedAadObjectId;
	private String correlationId;
	private String suoid;
	private String sasIpRange;
	private String protocol;
	private String version;
	private String resource;
	private String versionSegment;
	private String encryptionScope;
	private String cacheControl;
	private String contentDisposition;
	private String contentEncoding;
	private String contentLanguage;
	private String contentType;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}
	
	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public String getSignedObjectId() {
		return signedObjectId;
	}

	public void setSignedObjectId(String signedObjectId) {
		this.signedObjectId = signedObjectId;
	}

	public String getSignedTenantId() {
		return signedTenantId;
	}

	public void setSignedTenantId(String signedTenantId) {
		this.signedTenantId = signedTenantId;
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

	public String getSignedService() {
		return signedService;
	}

	public void setSignedService(String signedService) {
		this.signedService = signedService;
	}

	public String getSignedVersion() {
		return signedVersion;
	}

	public void setSignedVersion(String signedVersion) {
		this.signedVersion = signedVersion;
	}

	public String getAuthorizedAadObjectId() {
		return authorizedAadObjectId;
	}

	public void setAuthorizedAadObjectId(String authorizedAadObjectId) {
		this.authorizedAadObjectId = authorizedAadObjectId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getSuoid() {
		return suoid;
	}

	public void setSuoid(String suoid) {
		this.suoid = suoid;
	}

	public String getSasIpRange() {
		return sasIpRange;
	}

	public void setSasIpRange(String sasIpRange) {
		this.sasIpRange = sasIpRange;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getVersionSegment() {
		return versionSegment;
	}

	public void setVersionSegment(String versionSegment) {
		this.versionSegment = versionSegment;
	}

	public String getEncryptionScope() {
		return encryptionScope;
	}

	public void setEncryptionScope(String encryptionScope) {
		this.encryptionScope = encryptionScope;
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
