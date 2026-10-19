package com.axonivy.connector.azure.blob.internal.auth;

import java.time.LocalDateTime;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.client.IvyClientRequest;
import com.axonivy.connector.azure.blob.internal.model.response.AzureStorageToken;

import ch.ivyteam.ivy.environment.Ivy;

public class StorageTokenAuthorizationManager implements AuthorizationManager {

	private static final String STORAGE_RESOURCE = "https://storage.azure.com/";
	private static final String AUTH_END_POINT = "https://login.microsoftonline.com/%s/oauth2/token";
	private static final int MAX_ERROR_BODY_LENGTH = 1000;

	private static AzureStorageToken azureStorageToken = null;

	private ClientSecretCredential credential;

	public StorageTokenAuthorizationManager(ClientSecretCredential credential) {
		this.credential = credential;
	}

	@Override
	public String getToken() {
		if (credential == null) {
			throw new NullPointerException("The credential is null");
		}

		if (isExpired(azureStorageToken)) {
			azureStorageToken = getAzureStorageToken(credential);
		}
		return azureStorageToken.getAuthAccessToken();

	}

	@Override
	public String getSharedKey(WebTarget webTarget, IvyClientRequest ivyClientRequest) {
		throw new UnsupportedOperationException("Unsuport Share Key With ClientSecretCredential");
	}

	private AzureStorageToken getAzureStorageToken(ClientSecretCredential clientSecretCredential) {
		Form formData = new Form();
		formData.param("client_id", clientSecretCredential.getClientId());
		formData.param("client_secret", clientSecretCredential.getClientSecret());
		formData.param("grant_type", "client_credentials");
		formData.param("resource", STORAGE_RESOURCE);

		String path = String.format(AUTH_END_POINT, clientSecretCredential.getTenantId());

		Client client = ClientBuilder.newClient();
		try {
			Builder builder = client.target(path).request(MediaType.APPLICATION_JSON);

			try (Response response = builder.post(Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED))) {
				if (Response.Status.OK.getStatusCode() == response.getStatus()) {
					AzureStorageToken accessToken = response.readEntity(AzureStorageToken.class);
					return accessToken;
				}

				String message = "Getting azure access token is error. HttpStatus " + response.getStatus()
						+ ". Response: " + readBody(response);
				Ivy.log().error(message);

				throw new WebApplicationException(message, response.getStatus());
			}
		} finally {
			client.close();
		}
	}

	private static String readBody(Response response) {
		if (!response.hasEntity()) {
			return "";
		}
		try {
			return StringUtils.abbreviate(response.readEntity(String.class), MAX_ERROR_BODY_LENGTH);
		} catch (Exception e) {
			return "<body not readable: " + e.getMessage() + ">";
		}
	}

	private boolean isExpired(AzureStorageToken azureStorageToken) {
		return azureStorageToken == null || azureStorageToken.getExpiresDate().isBefore(LocalDateTime.now());
	}
}
