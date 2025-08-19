package com.axonivy.connector.azure.blob.internal.helper;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.UriBuilder;

public class EncryptionHelper {
	private static final String INVALID_BASE64_KEY = "'base64Key' was not a valid Base64 scheme. Ensure the Storage account key or SAS key is properly formatted.";

	/**
	 * Parses the query string into a key-value pair map that maintains key, query
	 * parameter key, order. The value is stored as a parsed array (ex. key=[val1,
	 * val2, val3] instead of key=val1,val2,val3).
	 *
	 * @param queryString Query string to parse
	 * @return a mapping of query string pieces as key-value pairs.
	 */
	public static Map<String, String[]> parseQueryStringSplitValues(final String queryString) {
		// We need to first split by comma and then decode each piece since we don't
		// want to confuse legitimate separate
		// query values from query values that container a comma.
		// Example 1: prefix=a%2cb => prefix={decode(a%2cb)} => prefix={"a,b"}
		// Example 2: prefix=a,b => prefix={decode(a),decode(b)} => prefix={"a", "b"}
		TreeMap<String, String[]> pieces = new TreeMap<>();

		if (isEmpty(queryString)) {
			return pieces;
		}

		for (String kvp : queryString.split("&")) {
			int equalIndex = kvp.indexOf('=');
			String key = EncodeHelper.urlDecode(kvp.substring(0, equalIndex).toLowerCase(Locale.ROOT));

			String[] value = kvp.substring(equalIndex + 1).split(",");
			for (int i = 0; i < value.length; i++) {
				value[i] = EncodeHelper.urlDecode(value[i]);
			}

			pieces.putIfAbsent(key, value);
		}

		return pieces;
	}

	/**
	 * Computes a signature for the specified string using the HMAC-SHA256
	 * algorithm.
	 *
	 * @param base64Key    Base64 encoded key used to sign the string
	 * @param stringToSign UTF-8 encoded string to sign
	 * @return the HMAC-SHA256 encoded signature
	 * @throws RuntimeException If the HMAC-SHA256 algorithm isn't support, if the
	 *                          key isn't a valid Base64 encoded string, or the
	 *                          UTF-8 charset isn't supported.
	 */
	public static String computeHMac256(final String base64Key, final String stringToSign) {
		byte[] key = null;
		try {
			key = Base64.getDecoder().decode(base64Key);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(INVALID_BASE64_KEY, ex);
		}

		try {
			Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
			hmacSHA256.init(new SecretKeySpec(key, "HmacSHA256"));
			byte[] utf8Bytes = stringToSign.getBytes(StandardCharsets.UTF_8);
			return Base64.getEncoder().encodeToString(hmacSHA256.doFinal(utf8Bytes));
		} catch (NoSuchAlgorithmException | InvalidKeyException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static URL appendToUrlPath(String baseURL, String name) throws MalformedURLException {
		URI uri = UriBuilder.fromPath(baseURL).fragment(name).build();
		return uri.toURL();
	}

	/**
	 * Strips the account name from host part of the URL object.
	 *
	 * @param url URL having its hostanme
	 * @return account name.
	 */
	public static String getAccountName(URL url) {
		String accountName = null;
		String host = url.getHost();
		// Parse host to get account name
		// host will look like this : <accountname>.blob.core.windows.net
		if (isNoneEmpty(host)) {
			int accountNameIndex = host.indexOf('.');
			if (accountNameIndex == -1) {
				// host only contains account name
				accountName = host;
			} else {
				// if host is separated by .
				accountName = host.substring(0, accountNameIndex);
			}
		}
		return accountName;
	}

	public static Iterator<Map.Entry<String, String>> parseQueryParameters(String queryParameters) {
		return (isEmpty(queryParameters)) ? Collections.emptyIterator() : new QueryParameterIterator(queryParameters);
	}

	private static final class QueryParameterIterator implements Iterator<Map.Entry<String, String>> {
		private final String queryParameters;
		private final int queryParametersLength;

		private boolean done = false;
		private int position;

		QueryParameterIterator(String queryParameters) {
			this.queryParameters = queryParameters;
			this.queryParametersLength = queryParameters.length();

			// If the URL query begins with '?' the first possible start of a query
			// parameter key is the
			// second character in the query.
			position = (queryParameters.startsWith("?")) ? 1 : 0;
		}

		@Override
		public boolean hasNext() {
			return !done;
		}

		@Override
		public Map.Entry<String, String> next() {
			if (done) {
				throw new NoSuchElementException();
			}

			int nextPosition = position;
			char c;
			while (nextPosition < queryParametersLength) {
				// Next position can either be '=' or '&' as a query parameter may not have a
				// '=', ex 'key&key2=value'.
				c = queryParameters.charAt(nextPosition);
				if (c == '=') {
					break;
				} else if (c == '&') {
					String key = queryParameters.substring(position, nextPosition);

					// Position is set to nextPosition + 1 to skip over the '&'
					position = nextPosition + 1;

					return new AbstractMap.SimpleImmutableEntry<>(key, "");
				}

				nextPosition++;
			}

			if (nextPosition == queryParametersLength) {
				// Query parameters completed.
				done = true;
				return new AbstractMap.SimpleImmutableEntry<>(queryParameters.substring(position), "");
			}

			String key = queryParameters.substring(position, nextPosition);

			// Position is set to nextPosition + 1 to skip over the '='
			position = nextPosition + 1;

			nextPosition = queryParameters.indexOf('&', position);

			String value = null;
			if (nextPosition == -1) {
				// This was the last key-value pair in the query parameters
				// 'https://example.com?param=done'
				done = true;
				value = queryParameters.substring(position);
			} else {
				value = queryParameters.substring(position, nextPosition);
				// Position is set to nextPosition + 1 to skip over the '&'
				position = nextPosition + 1;
			}

			return new AbstractMap.SimpleImmutableEntry<>(key, value);
		}
	}
}
