package com.axonivy.connector.azure.blob.internal.auth;

import static com.axonivy.connector.azure.blob.internal.helper.EncodeHelper.urlDecode;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.constant.Constants;
import com.axonivy.connector.azure.blob.internal.constant.MSHeader;
import com.axonivy.connector.azure.blob.internal.helper.EncryptionHelper;

public class StorageShareKeyAuthorizationManager implements AuthorizationManager {
	private static final Collator ROOT_COLLATOR = Collator.getInstance(Locale.ROOT);
	private static final String SHARED_KEY_FORMAT = "SharedKey %s:%s";
	private static final String LENGTH_DEFAULT = "0";
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
	public String getSharedKey(URL url, String httpMethod, Map<String, String> headers) {
		if (credential == null) {
			throw new NullPointerException("The credential is null");
		}

		return generateAuthorizationHeader(url, httpMethod, headers);
	}

	public String generateAuthorizationHeader(URL requestURL, String httpMethod, Map<String, String> headers) {
		String stringToSign = buildStringToSign(requestURL, httpMethod, headers, credential);
		String signature = EncryptionHelper.computeHMac256(credential.getAccountKey(), stringToSign);
		return String.format(SHARED_KEY_FORMAT, credential.getAccountName(), signature);
	}

	private String buildStringToSign(URL requestURL, String httpMethod, Map<String, String> headers, AzureNamedKeyCredential credential) {
		String contentLength = headers.getOrDefault(HttpHeaders.CONTENT_LENGTH, LENGTH_DEFAULT);		
		contentLength = LENGTH_DEFAULT.equals(contentLength) ? EMPTY : contentLength;

		// If the x-ms-header exists ignore the Date header
		String dateHeader = (headers.get(MSHeader.X_MS_DATE) != null) ? EMPTY
				: headers.getOrDefault(HttpHeaders.DATE, EMPTY);

		String stringToSign = String.join(StringUtils.LF, httpMethod,
				headers.getOrDefault(HttpHeaders.CONTENT_ENCODING, EMPTY),
				headers.getOrDefault(HttpHeaders.CONTENT_LANGUAGE, EMPTY), contentLength,
				headers.getOrDefault(HttpHeaders.CONTENT_MD5, EMPTY),
				headers.getOrDefault(HttpHeaders.CONTENT_TYPE, EMPTY), dateHeader,
				headers.getOrDefault(HttpHeaders.IF_MODIFIED_SINCE, EMPTY),
				headers.getOrDefault(HttpHeaders.IF_MATCH, EMPTY),
				headers.getOrDefault(HttpHeaders.IF_NONE_MATCH, EMPTY),
				headers.getOrDefault(HttpHeaders.IF_UNMODIFIED_SINCE, EMPTY),
				headers.getOrDefault(HttpHeaders.RANGE, EMPTY), getAdditionalXmsHeaders(headers),
				getCanonicalizedResource(requestURL, credential));

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

	private String getCanonicalizedResource(URL requestURL, AzureNamedKeyCredential credential) {

		// Resource path
		String resourcePath = credential.getAccountName();

		// Note that AbsolutePath starts with a '/'.
		String absolutePath = requestURL.getPath();
		if (isEmpty(absolutePath)) {
			absolutePath = Constants.SLASH;
		}

		// check for no query params and return
		String query = requestURL.getQuery();
		if (isEmpty(query)) {
			return Constants.SLASH + resourcePath + absolutePath;
		}

		int stringBuilderSize = 1 + resourcePath.length() + absolutePath.length() + query.length();

		// First split by comma and decode each piece to prevent confusing legitimate
		// separate query values from query
		// values that contain a comma.
		//
		// Example 1: prefix=a%2cb => prefix={decode(a%2cb)} => prefix={"a,b"}
		// Example 2: prefix=a,2 => prefix={decode(a),decode(b) => prefix={"a","b"}
		TreeMap<String, List<String>> pieces = new TreeMap<>(ROOT_COLLATOR);

		EncryptionHelper.parseQueryParameters(query).forEachRemaining(kvp -> {
			String key = urlDecode(kvp.getKey()).toLowerCase(Locale.ROOT);

			pieces.compute(key, (k, values) -> {
				if (values == null) {
					values = new ArrayList<>();
				}

				for (String value : kvp.getValue().split(",")) {
					values.add(urlDecode(value));
				}

				return values;
			});
		});

		stringBuilderSize += pieces.size();

		StringBuilder canonicalizedResource = new StringBuilder(stringBuilderSize)
				.append(Constants.SLASH)
				.append(resourcePath)
				.append(absolutePath);

		for (Map.Entry<String, List<String>> queryParam : pieces.entrySet()) {
			List<String> queryParamValues = queryParam.getValue();
			queryParamValues.sort(ROOT_COLLATOR);
			canonicalizedResource.append(StringUtils.LF)
					.append(queryParam.getKey())
					.append(Constants.COLON);

			int size = queryParamValues.size();
			for (int i = 0; i < size; i++) {
				String queryParamValue = queryParamValues.get(i);
				if (i > 0) {
					canonicalizedResource.append(Constants.COMMA);
				}

				canonicalizedResource.append(queryParamValue);
			}
		}

		// append to main string builder the join of completed params with new line
		return canonicalizedResource.toString();
	}
}
