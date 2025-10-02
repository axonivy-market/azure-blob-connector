package com.axonivy.connector.azure.blob.internal.client;

import static java.util.Collections.emptyMap;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import static org.apache.commons.collections4.MapUtils.emptyIfNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.axonivy.connector.azure.blob.internal.auth.StorageShareKeyAuthorizationManager;
import com.axonivy.connector.azure.blob.internal.auth.StorageTokenAuthorizationManager;
import com.axonivy.connector.azure.blob.internal.constant.Constants;
import com.axonivy.connector.azure.blob.internal.constant.MSHeader;
import com.axonivy.connector.azure.blob.internal.helper.WebTargetHelper;

import ch.ivyteam.ivy.environment.Ivy;

public class AzureStorageBlobClient {

	private static final String STORAGE_ACCOUNT_TEMPLATE = "storage.account";
	private static final UUID REST_CLIENT_UUID = UUID.fromString("a61f273e-f451-4fe3-a2b7-9f62fddb7bf3");

	private static final Map<String, String> EMPTY_HEADERS = emptyMap();
	private static final Map<String, String> EMPTY_QUERIES = emptyMap();
	private static final Map<String, String> NO_BODY = null;

	private AuthorizationManager authorizationManager;
	private WebTarget ivyClient;

	public AzureStorageBlobClient(String storageAccount, AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;

		ivyClient = Ivy.rest().client(REST_CLIENT_UUID).resolveTemplate(STORAGE_ACCOUNT_TEMPLATE, storageAccount);
	}

	public URL getURL() throws MalformedURLException {
		return ivyClient.getUri().toURL();
	}

	public Response get(String path) {
		return get(path, EMPTY_QUERIES);
	}

	public Response get(String path, Map<String, String> queries) {
		Response response = excute(path, GET, NO_BODY, EMPTY_HEADERS, queries);
		return response;
	}

	public Response post(String path, Object body) {
		Response response = excute(path, POST, body, EMPTY_HEADERS, EMPTY_QUERIES);
		return response;
	}

	public Response post(String path, Object body, Map<String, String> headers, Map<String, String> queries) {
		Response response = excute(path, POST, body, headers, queries);
		return response;
	}

	public Response put(String path, Map<String, String> headers) {
		return put(path, headers, EMPTY_QUERIES);
	}

	public Response put(String path, Object body, Map<String, String> headers) {
		return put(path, body, headers, EMPTY_QUERIES);
	}

	public Response put(String path, Map<String, String> headers, Map<String, String> queries) {
		return put(path, NO_BODY, headers, queries);
	}

	public Response put(String path, Object body, Map<String, String> headers, Map<String, String> queries) {
		return excute(path, PUT, body, headers, queries);
	}

	public Response delete(String path, Map<String, String> headers) {
		Response response = delete(path, headers, EMPTY_QUERIES);
		return response;
	}

	public Response delete(String path, Map<String, String> headers, Map<String, String> queries) {
		Response response = excute(path, DELETE, NO_BODY, headers, queries);
		return response;
	}

	private Response excute(String path, String httpMethod, Object body, Map<String, String> headers,
			Map<String, String> queries) {
		Map<String, String> allHeaders = combineWithCommonHeader(headers);

		IvyClientRequest request = IvyClientRequest.builder().path(path).body(body).httpMethod(httpMethod)
				.headers(allHeaders).queries(queries).build();

		String accessKey = EMPTY;
		if (authorizationManager instanceof StorageTokenAuthorizationManager) {
			accessKey = this.authorizationManager.getToken();
		} else if (authorizationManager instanceof StorageShareKeyAuthorizationManager) {
			accessKey = this.authorizationManager.getSharedKey(this.ivyClient, request);
		}

		Map<String, String> mapWithAuth = new HashMap<>(Map.of(HttpHeaders.AUTHORIZATION, accessKey));
		mapWithAuth.putAll(allHeaders);

		WebTarget webTarget = request.getWebTarget(this.ivyClient);
		Builder builder = webTarget.request(MediaType.APPLICATION_XML).headers(toMultivaluedMap(mapWithAuth));

		Entity<?> entity = buildEntity(httpMethod, body);

		switch (httpMethod) {
		case HttpMethod.GET:
			return builder.get();
		case HttpMethod.POST:
			return builder.post(entity);
		case HttpMethod.PUT:
			return builder.put(entity);
		case HttpMethod.DELETE:
			return builder.delete();
		}
		throw new NotSupportedException(String.format("Http Method %s is not support", httpMethod));
	}

	private Entity<?> buildEntity(String httpMethod, Object body) {
		if (PUT.equals(httpMethod) || POST.equals(httpMethod)) {
			String mediaType = WebTargetHelper.getMediaType(httpMethod, body);
			return Entity.entity(body != null ? body : EMPTY, mediaType);
		}

		return null;
	}

	private Map<String, String> combineWithCommonHeader(Map<String, String> headers) {
		Map<String, String> allHeaders = new HashMap<>();
		allHeaders.putAll(emptyIfNull(headers));
		allHeaders.putAll(createCommonHeader());
		return allHeaders;
	}

	private Map<String, String> createCommonHeader() {
		Map<String, String> headers = new LinkedHashMap<>();

		headers.put(MSHeader.X_MS_DATE, Constants.RFC_1123_UTC_DATE_FORMATTER.format(OffsetDateTime.now()));
		headers.put(MSHeader.X_MS_VERSION, Constants.MS_VERSION);
		headers.put(MSHeader.X_MS_CLIENT_REQUEST_ID, UUID.randomUUID().toString());

		return headers;
	}

	public static MultivaluedMap<String, Object> toMultivaluedMap(Map<String, String> headers) {
		MultivaluedMap<String, Object> multiValueMap = new MultivaluedHashMap<>();

		if (headers != null) {
			headers.forEach((key, value) -> multiValueMap.add(key, value));
		}

		return multiValueMap;
	}
}
