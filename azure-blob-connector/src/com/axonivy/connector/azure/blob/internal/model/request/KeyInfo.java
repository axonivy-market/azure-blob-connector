
package com.axonivy.connector.azure.blob.internal.model.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "KeyInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class KeyInfo implements Serializable {

	private static final long serialVersionUID = -1630108626337319361L;

	/*
	 * The date-time the key is active in ISO 8601 UTC time
	 */
	@XmlElement(name = "Start", required = true)
	private String start;

	/*
	 * The date-time the key expires in ISO 8601 UTC time
	 */
	@XmlElement(name = "Expiry", required = true)
	private String expiry;

	public String getStart() {
		return this.start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getExpiry() {
		return this.expiry;
	}

	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof KeyInfo)) {
			return false;
		}
		KeyInfo keyInfo = (KeyInfo) other;
		return Objects.equal(this.start, keyInfo.start) && Objects.equal(this.expiry, keyInfo.expiry);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(start, expiry);
	}
}
