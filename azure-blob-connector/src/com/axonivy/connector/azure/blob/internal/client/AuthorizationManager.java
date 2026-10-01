package com.axonivy.connector.azure.blob.internal.client;

import jakarta.ws.rs.client.WebTarget;

public interface AuthorizationManager {
	String getToken();

	String getSharedKey(WebTarget webTarget, IvyClientRequest request);
}
