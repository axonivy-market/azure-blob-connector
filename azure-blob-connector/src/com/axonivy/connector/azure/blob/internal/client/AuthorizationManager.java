package com.axonivy.connector.azure.blob.internal.client;

import javax.ws.rs.client.WebTarget;

public interface AuthorizationManager {
	String getToken();

	String getSharedKey(WebTarget webTarget, IvyClientRequest request);
}
