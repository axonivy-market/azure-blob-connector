package com.axonivy.connector.azure.blob.internal.model.response;

import java.time.OffsetDateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.axonivy.connector.azure.blob.internal.helper.OffsetDateTimeAdapter;

@XmlRootElement(name = "UserDelegationKey")
@XmlAccessorType(XmlAccessType.FIELD)
public final class UserDelegationKey {

	/*
	 * The Azure Active Directory object ID in GUID format.
	 */
	@XmlElement(name = "SignedOid", required = true)
	private String signedObjectId;

	/*
	 * The Azure Active Directory tenant ID in GUID format
	 */
	@XmlElement(name = "SignedTid", required = true)
	private String signedTenantId;

	@XmlElement(name = "SignedStart", required = true)
	@XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
	private OffsetDateTime signedStart;

	@XmlElement(name = "SignedExpiry", required = true)
	@XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
	private OffsetDateTime signedExpiry;

	/*
	 * Abbreviation of the Azure Storage service that accepts the key
	 */
	@XmlElement(name = "SignedService", required = true)
	private String signedService;

	/*
	 * The service version that created the key
	 */
	@XmlElement(name = "SignedVersion", required = true)
	private String signedVersion;

	/*
	 * The key as a base64 string
	 */
	@XmlElement(name = "Value", required = true)
	private String value;

	public UserDelegationKey() {
	}

	public String getSignedObjectId() {
		return this.signedObjectId;
	}

	public void setSignedObjectId(String signedObjectId) {
		this.signedObjectId = signedObjectId;
	}

	public String getSignedTenantId() {
		return this.signedTenantId;
	}

	public void setSignedTenantId(String signedTenantId) {
		this.signedTenantId = signedTenantId;
	}

	public OffsetDateTime getSignedStart() {
		return this.signedStart;
	}

	public UserDelegationKey setSignedStart(OffsetDateTime signedStart) {
		this.signedStart = signedStart;
		return this;
	}

	public OffsetDateTime getSignedExpiry() {
		return this.signedExpiry;
	}

	public UserDelegationKey setSignedExpiry(OffsetDateTime signedExpiry) {
		this.signedExpiry = signedExpiry;
		return this;
	}

	public String getSignedService() {
		return this.signedService;
	}

	public void setSignedService(String signedService) {
		this.signedService = signedService;
	}

	public String getSignedVersion() {
		return this.signedVersion;
	}

	public void setSignedVersion(String signedVersion) {
		this.signedVersion = signedVersion;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
