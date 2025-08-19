package com.axonivy.connector.azure.blob.internal.client;

import java.net.URL;
import java.util.Map;

public interface AuthorizationManager {
	String getToken();

	String getSharedKey(URL url, String httpMethod, Map<String, String> headers);
}
