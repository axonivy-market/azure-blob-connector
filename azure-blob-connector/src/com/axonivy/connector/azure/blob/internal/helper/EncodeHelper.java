package com.axonivy.connector.azure.blob.internal.helper;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.UriBuilder;

public final class EncodeHelper {

	/**
	 * Performs a safe decoding of the passed string, taking care to preserve each
	 * {@code +} character rather than replacing it with a space character.
	 *
	 * @param stringToDecode String value to decode
	 * @return the decoded string value
	 * @throws RuntimeException If the UTF-8 charset isn't supported
	 */
	public static String urlDecode(final String stringToDecode) {
		if (isEmpty(stringToDecode)) {
			return "";
		}

		int lastIndexOfPlus = 0;
		int indexOfPlus = stringToDecode.indexOf('+');

		if (indexOfPlus == -1) {
			// No '+' characters to preserve.
			return decode(stringToDecode);
		}

		// Create a StringBuilder large enough to contain the decoded string.
		// This will create a StringBuilder larger than the final string as decoding
		// shrinks in size ('%20' -> ' ').
		StringBuilder outBuilder = new StringBuilder(stringToDecode.length());

		do {
			// Decode the range of characters between the last two '+'s found.
			outBuilder.append(decode(stringToDecode.substring(lastIndexOfPlus, indexOfPlus)));

			// Append the preserved '+'/
			outBuilder.append('+');

			// Set the last found plus index to the index after the '+' just found.
			lastIndexOfPlus = indexOfPlus + 1;

			// Continue until no further '+' characters are found.
		} while ((indexOfPlus = stringToDecode.indexOf('+', lastIndexOfPlus)) != -1);

		// If the last found plus wasn't the last character decode the remaining string.
		if (lastIndexOfPlus != stringToDecode.length()) {
			outBuilder.append(decode(stringToDecode.substring(lastIndexOfPlus)));
		}

		return outBuilder.toString();
	}

	/*
	 * Helper method to reduce duplicate calls of URLDecoder.decode
	 */
	private static String decode(final String stringToDecode) {
		try {
			return URLDecoder.decode(stringToDecode, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Performs a safe encoding of the specified string, taking care to insert %20
	 * for each space character instead of inserting the {@code +} character.
	 *
	 * @param stringToEncode String value to encode
	 * @return the encoded string value
	 * @throws RuntimeException If the UTF-8 charset ins't supported
	 */
	public static String urlEncode(final String stringToEncode) {
		if (stringToEncode == null) {
			return null;
		}

		if (stringToEncode.isEmpty()) {
			return "";
		}

		int lastIndexOfSpace = 0;
		int indexOfSpace = stringToEncode.indexOf(' ');

		if (indexOfSpace == -1) {
			// No ' ' characters to escape.
			return encode(stringToEncode);
		}

		// Create a StringBuilder with an estimated size large enough to contain the
		// encoded string.
		// It's unknown how many characters will need encoding so this is a best effort
		// as encoding increases size
		// (' ' -> '%20').
		// Use 2x the string length, this means every third character will need to be
		// encoded to three characters.
		// 90 characters / 3 = 30 encodings of 3 characters = 90, 2 * 90 = 180.
		StringBuilder outBuilder = new StringBuilder(stringToEncode.length() * 2);

		do {
			// Encode the range of characters between the last two ' 's found.
			outBuilder.append(encode(stringToEncode.substring(lastIndexOfSpace, indexOfSpace)));

			// Append the preserved ' '.
			outBuilder.append("%20");

			// Set the last found space index to the index after the ' ' just found.
			lastIndexOfSpace = indexOfSpace + 1;

			// Continue until no further ' ' characters are found.
		} while ((indexOfSpace = stringToEncode.indexOf(' ', lastIndexOfSpace)) != -1);

		// If the last found space wasn't the last character encode the remaining
		// string.
		if (lastIndexOfSpace != stringToEncode.length()) {
			outBuilder.append(encode(stringToEncode.substring(lastIndexOfSpace)));
		}

		return outBuilder.toString();
	}

	private static String encode(final String stringToEncode) {
		try {
			return URLEncoder.encode(stringToEncode, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String encodeUrlPath(String url) {
		URI builder = UriBuilder.fromPath(url).build();
		String path = builder.getPath();
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		path = EncodeHelper.urlEncode(EncodeHelper.urlDecode(path));
		return path;
	}

	/**
	 * Appends a query parameter to a url.
	 *
	 * @param url   The url.
	 * @param key   The query key.
	 * @param value The query value.
	 * @return The updated url.
	 */
	public static String appendQueryParameter(String url, String key, String value) {
		return (url.indexOf('?') != -1) ? url + "&" + key + "=" + value : url + "?" + key + "=" + value;
	}
}
