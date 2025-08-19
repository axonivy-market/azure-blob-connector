package com.axonivy.connector.azure.blob.internal.auth;

public final class ClientSecretCredential implements Credential {
	private String tenantId;
	private String clientId;
	private String clientSecret;

	/**
	 * Creates a ClientSecretCredential with the given identity client options.
	 * 
	 * @param clientId     - the client ID of the application
	 * @param clientSecret - the secret value of the Microsoft Entra application.
	 * @param tenantId     - the tenant ID of the application.
	 */
	public ClientSecretCredential(String tenantId, String clientId, String clientSecret) {
		this.tenantId = tenantId;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}
}
