package com.axonivy.connector.azure.blob.internal.model.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Tag")
@XmlAccessorType(XmlAccessType.FIELD)
public final class BlobTag implements Serializable {

	private static final long serialVersionUID = 8778913142459541823L;

	@XmlElement(name = "Key", required = true)
	private String key;

	@XmlElement(name = "Value", required = true)
	private String value;

	public BlobTag() {
		super();
	}

	public String getKey() {
		return this.key;
	}

	public BlobTag setKey(String key) {
		this.key = key;
		return this;
	}

	public String getValue() {
		return this.value;
	}

	public BlobTag setValue(String value) {
		this.value = value;
		return this;
	}
}
