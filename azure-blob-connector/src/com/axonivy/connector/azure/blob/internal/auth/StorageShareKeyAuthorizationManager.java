package com.axonivy.connector.azure.blob.internal.auth;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.client.WebTarget;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.client.IvyClientRequest;
import com.axonivy.connector.azure.blob.internal.constant.Constants;
import com.axonivy.connector.azure.blob.internal.helper.EncryptionHelper;
import com.axonivy.connector.azure.blob.internal.helper.WebTargetHelper;

import ch.ivyteam.ivy.environment.Ivy;

public class StorageShareKeyAuthorizationManager implements AuthorizationManager {
	private static final String SHARED_KEY_FORMAT = "SharedKey %s:%s";
	private static final String XMS_HEADER_PREFIX = "x-ms-";

	private AzureNamedKeyCredential credential;

	public StorageShareKeyAuthorizationManager(AzureNamedKeyCredential credential) {
		this.credential = credential;
	}

	@Override
	public String getToken() {
		throw new UnsupportedOperationException("Unsuport Token With AzureNamedKeyCredential");
	}

	@Override
	public String getSharedKey(WebTarget webTarget, IvyClientRequest request) {
		if (credential == null) {
			throw new NullPointerException("The credential is null");
		}

		return generateAuthorizationHeader(webTarget, request);
	}

	public String generateAuthorizationHeader(WebTarget webTarget, IvyClientRequest ivyClientRequest) {
		String stringToSign = buildStringToSign(webTarget, ivyClientRequest, credential);
		String signature = EncryptionHelper.computeHMac256(credential.getAccountKey(), stringToSign);
		return String.format(SHARED_KEY_FORMAT, credential.getAccountName(), signature);
	}

	private String buildStringToSign(WebTarget root, IvyClientRequest request, AzureNamedKeyCredential credential) {
		String contentLength = WebTargetHelper.getContentLength(request.getHttpMethod(), request.getBody());
		String getContentType = WebTargetHelper.getMediaType(request.getHttpMethod(), request.getBody()); 
		Map<String, String> headers = request.getHeaders();
		
		String stringToSign = String.join(StringUtils.LF, 
				request.getHttpMethod(),
				headers.getOrDefault(HttpHeaders.CONTENT_ENCODING, EMPTY),
				headers.getOrDefault(HttpHeaders.CONTENT_LANGUAGE, EMPTY),
				contentLength,
				headers.getOrDefault(HttpHeaders.CONTENT_MD5, EMPTY),				
				getContentType,
				EMPTY,
				headers.getOrDefault(HttpHeaders.IF_MODIFIED_SINCE, EMPTY),
				headers.getOrDefault(HttpHeaders.IF_MATCH, EMPTY),
				headers.getOrDefault(HttpHeaders.IF_NONE_MATCH, EMPTY),
				headers.getOrDefault(HttpHeaders.IF_UNMODIFIED_SINCE, EMPTY),
				headers.getOrDefault(HttpHeaders.RANGE, EMPTY),
				getAdditionalXmsHeaders(headers),
				getCanonicalizedResource(root, request, credential));
		
		Ivy.log().info(stringToSign);

		return stringToSign;
	}

	private String getAdditionalXmsHeaders(Map<String, String> headers) {
		List<Entry<String, String>> xmsHeaders = headers.entrySet().stream()
				.filter(it -> it.getKey().startsWith(XMS_HEADER_PREFIX)).toList();

		if (xmsHeaders.isEmpty()) {
			return EMPTY;
		}

		int stringBuilderSize = xmsHeaders.stream().map(it -> it.getKey().length() + it.getValue().length()).reduce(0,
				(a, b) -> a + b, Integer::sum);

		List<Entry<String, String>> xmsHeadersSorted = xmsHeaders.stream().sorted(Comparator.comparing(Entry::getKey))
				.toList();

		final StringBuilder canonicalizedHeaders = new StringBuilder(
				stringBuilderSize + (2 * xmsHeadersSorted.size()) - 1);

		for (Entry<String, String> xmsHeader : xmsHeadersSorted) {
			if (canonicalizedHeaders.length() > 0) {
				canonicalizedHeaders.append(StringUtils.LF);
			}
			String header = String.format("%s:%s", xmsHeader.getKey().toLowerCase(Locale.ROOT), xmsHeader.getValue());
			canonicalizedHeaders.append(header);
		}

		return canonicalizedHeaders.toString();
	}

	private String getCanonicalizedResource(WebTarget root, IvyClientRequest request,  AzureNamedKeyCredential credential) {

		// Resource path
		String resourcePath = credential.getAccountName();
		WebTarget webTarget = request.getWebTarget(root);
		
		// Note that AbsolutePath starts with a '/'.		
		String absolutePath = webTarget.getUri().getPath();
				
		String resourceRequestPath = Constants.SLASH + resourcePath + absolutePath;
		
		// check for no query params and return	
		Map<String, String> queries = request.getQueries();
		if (MapUtils.isEmpty(queries)) {
			return resourceRequestPath;
		}
		
		List<String>  resources = new ArrayList<>();
		resources.add(resourceRequestPath);
		
		List<Map.Entry<String, String>> sortedQuery = queries.entrySet().stream()
				.sorted(Comparator.comparing(Map.Entry::getKey)).toList(); 
		
		for(Map.Entry<String, String> query : sortedQuery) {
			String queryParam = String.format("%s:%s", query.getKey(), query.getValue());
			resources.add(queryParam);
		}

		// append to main string builder the join of completed params with new line
		return resources.stream().collect(Collectors.joining(StringUtils.LF));
	}
	
	
}
