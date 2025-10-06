package com.axonivy.connector.azure.blob.internal.model.response;

import static java.util.stream.Collectors.joining;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureStorageToken implements Serializable {

	private static final long serialVersionUID = -4356293010552185694L;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("expires_in")
	private int expiresIn;

	@JsonProperty("expires_on")
	private long expiresOn;

	@JsonProperty("access_token")
	private String accessToken;

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public long getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(long expiresOn) {
		this.expiresOn = expiresOn;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public LocalDateTime getExpiresDate() {
		return LocalDateTime.ofInstant(Instant.ofEpochSecond(expiresOn), ZoneId.systemDefault());
	}

	public String getAuthAccessToken() {
		String token = Stream.of(this.tokenType, this.accessToken).filter(StringUtils::isNoneEmpty)
				.collect(joining(StringUtils.SPACE));
		return token;
	}
}
