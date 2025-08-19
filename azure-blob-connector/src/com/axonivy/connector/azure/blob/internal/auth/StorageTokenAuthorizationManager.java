package com.axonivy.connector.azure.blob.internal.auth;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.model.response.AzureStorageToken;

import ch.ivyteam.ivy.environment.Ivy;

public class StorageTokenAuthorizationManager implements AuthorizationManager {

	private static final String STORAGE_RESOURCE = "https://storage.azure.com/";
	private static final String AUTH_END_POINT = "https://login.microsoftonline.com/%s/oauth2/token";

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
	public String getSharedKey(URL url, String httpMethod, Map<String, String> headers) {
		throw new UnsupportedOperationException("Unsuport Share Key With ClientSecretCredential");
	}

	private AzureStorageToken getAzureStorageToken(ClientSecretCredential clientSecretCredential) {
		Form formData = new Form();
		formData.param("client_id", clientSecretCredential.getClientId());
		formData.param("client_secret", clientSecretCredential.getClientSecret());
		formData.param("grant_type", "client_credentials");
		formData.param("resource", STORAGE_RESOURCE);

		String path = String.format(AUTH_END_POINT, clientSecretCredential.getTenantId());

		Builder builder = ClientBuilder.newClient().target(path).request(MediaType.APPLICATION_JSON);

		Response response = builder.post(Entity.entity(formData, MediaType.APPLICATION_FORM_URLENCODED));
		if (Response.Status.OK.getStatusCode() == response.getStatus()) {
			AzureStorageToken accessToken = response.readEntity(AzureStorageToken.class);
			return accessToken;
		}

		String message = "Getting azure access token is error.";
		Ivy.log().error(message);

		throw new WebApplicationException(message, response);
	}

	private boolean isExpired(AzureStorageToken azureStorageToken) {
		return azureStorageToken == null || azureStorageToken.getExpiresDate().isBefore(LocalDateTime.now());
	}
}
