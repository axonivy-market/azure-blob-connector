package com.axonivy.connector.azure.blob.internal.client;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyMap;
import static org.apache.commons.collections4.MapUtils.emptyIfNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;

import com.axonivy.connector.azure.blob.internal.auth.StorageShareKeyAuthorizationManager;
import com.axonivy.connector.azure.blob.internal.auth.StorageTokenAuthorizationManager;
import com.axonivy.connector.azure.blob.internal.constant.Constants;
import com.axonivy.connector.azure.blob.internal.constant.MSHeader;
import com.axonivy.connector.azure.blob.internal.helper.HttpRequestBuilder;

import ch.ivyteam.ivy.environment.Ivy;

public class AzureStorageClient {
	private static final String POST_METHOD = "POST";
	private static final String PUT_METHOD = "PUT";
	private static final String GET_METHOD = "GET";
	private static final String DELETE_METHOD = "DELETE";
	
	private static final Map<String, String> EMPTY_HEADERS = emptyMap();	
	private static final Map<String, String> EMPTY_QUERIES = emptyMap();
	private static final Map<String, String> NO_BODY = null;
	
	private AuthorizationManager authorizationManager;

	public AzureStorageClient(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}
	
	public HttpResponse<Path> downloadToFile(String uri, String filePath) {
		HttpRequest downloadRequest = createHttpRequest(uri, GET_METHOD, NO_BODY, EMPTY_HEADERS, EMPTY_QUERIES);

		Path path = Path.of(filePath);		
		HttpClient client = HttpClient.newHttpClient();
		try {
			HttpResponse<Path> response = client.send(downloadRequest, BodyHandlers.ofFile(path));
			return response;	
		} catch (Exception e) {
			throw createRuntimeException(downloadRequest, e);
		}		
	}

	public HttpResponse<InputStream> downloadStream(String uri) {
		HttpRequest downloadRequest = createHttpRequest(uri, GET_METHOD, NO_BODY, EMPTY_HEADERS, EMPTY_QUERIES);
		return excuteInputStreamResponse(downloadRequest);
	}

	public HttpResponse<Void> get(String uri) {
		HttpRequest getRequest = createHttpRequest(uri, GET_METHOD, NO_BODY, EMPTY_HEADERS, EMPTY_QUERIES);
		HttpResponse<Void> response = excuteVoidResponse(getRequest);
		logHttpClient(getRequest, response);
		return response;
	}
	
	public HttpResponse<String> get(String uri, Map<String, String> queries, Object... uriVariables) throws Exception  {
		HttpRequest getRequest = createHttpRequest(uri, GET_METHOD, NO_BODY, EMPTY_HEADERS, queries, uriVariables);	
		HttpResponse<String> response = excuteStringResponse(getRequest);
		logHttpClient(getRequest, response);
		return response;
	}
	
	public HttpResponse<String> post(String uri, Object body, Map<String, String> headers, Map<String, String> queries) {
		HttpRequest postRequest = createHttpRequest(uri, POST_METHOD, body, headers, queries);
		return excuteStringResponse(postRequest);
	}

	public HttpResponse<Void> put(String uri, Map<String, String> headers, Object... uriVariables) {
		return put(uri, headers, EMPTY_QUERIES, uriVariables);
	}
	
	public HttpResponse<Void> put(String uri, Map<String, String> headers, Map<String, String> queries, Object... uriVariables) {
		return put(uri, NO_BODY, headers, queries, uriVariables);
	}
	
	public HttpResponse<Void> put(String uri, Object body, Map<String, String> headers, Object... uriVariables) {
		return put(uri, body, headers, EMPTY_QUERIES, uriVariables);
	}
	
	public HttpResponse<Void> put(String uri, Object body, Map<String, String> headers, Map<String, String> queries, Object... uriVariables) {
		HttpRequest putRequest = createHttpRequest(uri, PUT_METHOD, body, headers, queries, uriVariables);
		HttpResponse<Void> response = excuteVoidResponse(putRequest);
		logHttpClient(putRequest, response);
		return response;
	}
	
	public HttpResponse<Void> delete(String uri, Map<String, String> headers, Object... uriVariables) {
		return delete(uri, headers, EMPTY_QUERIES, uriVariables);
	}
	
	public HttpResponse<Void> delete(String uri, Map<String, String> headers, Map<String, String> queries, Object... uriVariables) {
		HttpRequest deleteRequest = createHttpRequest(uri, DELETE_METHOD, NO_BODY, headers, queries, uriVariables);
		HttpResponse<Void> response = excuteVoidResponse(deleteRequest);
		logHttpClient(deleteRequest, response);
		return response;
	}
	
	private HttpRequest createHttpRequest(String uri, String httpMethod, Object body, Map<String, String> headers, Map<String, String> queries, Object... uriVariables) {
		try {
			URL url = buildUrl(uri, queries, uriVariables);

			Map<String, String> allHeaders = combineWithCommonHeader(headers);
			String token = getAuthoration(url, httpMethod, allHeaders);

			Map<String, String> mapWithAuth = new HashMap<>(Map.of(HttpHeaders.AUTHORIZATION, token));
			mapWithAuth.putAll(allHeaders);

			HttpRequest httpRequest = HttpRequestBuilder.builder()
					.uri(uri)
					.httpMethod(httpMethod)
					.queries(queries)
					.uriVariables(uriVariables)
					.headers(mapWithAuth)
					.body(body)
					.build();

			return httpRequest;
		} catch (Exception e) {
			String msg = String.format("Error when creating http request for %s", uri);
			throw new RuntimeException(msg, e);
		}		
	}
	
	private HttpResponse<InputStream> excuteInputStreamResponse(HttpRequest httpRequest) {
		HttpClient client = HttpClient.newHttpClient();
		try {
			return client.send(httpRequest, BodyHandlers.ofInputStream());
		} catch (IOException | InterruptedException e) {
			throw createRuntimeException(httpRequest, e);
		}
	}
	
	private HttpResponse<String> excuteStringResponse(HttpRequest httpRequest) {
		HttpClient client = HttpClient.newHttpClient();
		try {
			return client.send(httpRequest, BodyHandlers.ofString(UTF_8));
		} catch (IOException | InterruptedException e) {
			throw createRuntimeException(httpRequest, e);
		}
	}

	private HttpResponse<Void> excuteVoidResponse(HttpRequest httpRequest) {
		HttpClient client = HttpClient.newHttpClient();
		try {
			return client.send(httpRequest, BodyHandlers.discarding());
		} catch (IOException | InterruptedException e) {
			throw createRuntimeException(httpRequest, e);
		}
	}
	
	private Map<String, String> combineWithCommonHeader(Map<String, String> headers) {
		Map<String, String> allHeaders = new HashMap<>();
		allHeaders.putAll(emptyIfNull(headers));
		allHeaders.putAll(createCommonHeader());
		return allHeaders;
	}
	
	private Map<String, String> createCommonHeader() {
		Map<String, String> headers = new HashMap<>();
	
		headers.put(HttpHeaders.DATE, Constants.RFC_1123_UTC_DATE_FORMATTER.format(OffsetDateTime.now()));
		headers.put(MSHeader.X_MS_VERSION, Constants.MS_VERSION);
		headers.put(MSHeader.X_MS_CLIENT_REQUEST_ID, UUID.randomUUID().toString());
		
		return headers;
	}	
	
	private URL buildUrl(String path, Map<String, String> queries, Object... uriVariables) throws MalformedURLException {
		UriBuilder uriBuilder = UriBuilder.fromPath(path);
		emptyIfNull(queries).entrySet().forEach(it-> {
			uriBuilder.queryParam(it.getKey(),  it.getValue());
		});
		return uriBuilder.build(uriVariables).toURL();
	}
	
	private String getAuthoration(URL url, String httpMethod, Map<String, String> headers) {
		if (authorizationManager instanceof StorageTokenAuthorizationManager) {
			return this.authorizationManager.getToken();
		} else if (authorizationManager instanceof StorageShareKeyAuthorizationManager) {

			return this.authorizationManager.getSharedKey(url, httpMethod, headers);
		}
		return null;
	}
	
	private RuntimeException createRuntimeException(HttpRequest httpRequest, Exception e) {
		String msg = String.format("Error when excuting http request for %s", httpRequest.uri().toString());
		return new RuntimeException(msg, e);
	}
		
	private void logHttpClient(HttpRequest httpRequest, HttpResponse<?> httpResponse) {
		try {
			Ivy.log().debug("HttpClient Request: [{0}] {1}",  httpRequest.method(), httpRequest.uri());
			Ivy.log().debug("HttpClient Response: [{0}] {1}",  httpResponse.statusCode(), httpResponse.body());	
		} catch (Exception e) {
			//EnvironmentNotAvailableException
		}		
	}
}
