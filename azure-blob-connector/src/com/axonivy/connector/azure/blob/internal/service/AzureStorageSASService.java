package com.axonivy.connector.azure.blob.internal.service;

import static com.axonivy.connector.azure.blob.internal.constant.Constants.ISO_8601_UTC_DATE_FORMATTER;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.client.AzureStorageBlobClient;
import com.axonivy.connector.azure.blob.internal.constant.BlobQuery;
import com.axonivy.connector.azure.blob.internal.constant.Constants;
import com.axonivy.connector.azure.blob.internal.constant.SASParam;
import com.axonivy.connector.azure.blob.internal.helper.EncodeHelper;
import com.axonivy.connector.azure.blob.internal.helper.EncryptionHelper;
import com.axonivy.connector.azure.blob.internal.helper.JAXBHelper;
import com.axonivy.connector.azure.blob.internal.model.UserDelegationSign;
import com.axonivy.connector.azure.blob.internal.model.request.KeyInfo;
import com.axonivy.connector.azure.blob.internal.model.response.UserDelegationKey;

public class AzureStorageSASService extends AbstractAzureStorage {
	private static final String READ_PERMISSION = "r";
	private static final String SAS_BLOB_CONSTANT = "b";
	private static final String SAS_CONTAINER_CONSTANT = "c";
	private static final String HTTPS_PROTOCOL = "https";

	private static UserDelegationKey delegationKey;
	private AzureStorageBlobClient ivyClient;
	private String container;

	public AzureStorageSASService(Credential credential, String container) {
		this.container = container;

		AuthorizationManager authManager = getAuthorizationManager(credential);
		this.ivyClient = new AzureStorageBlobClient(authManager);
	}

	public String generateUserDelegationSas(String blobName, Duration liveTime) throws Exception {
		if (delegationKey == null || delegationKey.getSignedStart().isBefore(OffsetDateTime.now().minusMinutes(5))) {
			delegationKey = getUserDelegationKey(liveTime);
		}

		String accountName = EncryptionHelper.getAccountName(ivyClient.getURL());
		String canonicalizedResource = getCanonicalizedResource(accountName, this.container, blobName);

		OffsetDateTime now = OffsetDateTime.now();
		UserDelegationSign sign = new UserDelegationSign();
		sign.setSignedPermissions(READ_PERMISSION);
		sign.setSignedStart(ISO_8601_UTC_DATE_FORMATTER.format(now.minusMinutes(1)));
		sign.setSignedExpiry(ISO_8601_UTC_DATE_FORMATTER.format(now.plusSeconds(liveTime.getSeconds())));
		sign.setCanonicalizedResource(canonicalizedResource);

		sign.setSignedKeyObjectId(toString(delegationKey.getSignedObjectId()));
		sign.setSignedKeyTenantId(toString(delegationKey.getSignedTenantId()));
		sign.setSignedKeyStart(ISO_8601_UTC_DATE_FORMATTER.format((delegationKey.getSignedStart())));
		sign.setSignedKeyExpiry(ISO_8601_UTC_DATE_FORMATTER.format((delegationKey.getSignedExpiry())));
		sign.setSignedKeyService(toString(delegationKey.getSignedService()));
		sign.setSignedKeyVersion(toString(delegationKey.getSignedVersion()));

		sign.setSignedAuthorizedUserObjectId(EMPTY);
		sign.setSignedUnauthorizedUserObjectId(EMPTY);
		sign.setSignedCorrelationId(EMPTY);
		sign.setSignedIP(EMPTY);
		sign.setSignedProtocol(HTTPS_PROTOCOL);
		sign.setSignedVersion(Constants.MS_VERSION);
		sign.setSignedResource(getResource(blobName));
		sign.setSignedEncryptionScope(EMPTY);

		String stringToSign = stringToSign(sign);

		String signature = EncryptionHelper.computeHMac256(delegationKey.getValue(), stringToSign);
		String encodeValue = encode(sign, signature);
		return encodeValue;
	}

	private String getCanonicalizedResource(String account, String containerName, String blobName) {
		// Container: "/blob/account/containername"
		// Blob: "/blob/account/containername/blobname"

		String blob = Objects.toString(blobName, EMPTY).replace('\\', '/');
		String canonicalName = Stream.of("blob", account, containerName, blob).filter(StringUtils::isNoneEmpty)
				.collect(Collectors.joining("/"));
		return String.format("/%s", canonicalName);
	}

	private String stringToSign(UserDelegationSign sign) {
		String stringToSign = String.join(StringUtils.LF, sign.getSignedPermissions(), sign.getSignedStart(),
				sign.getSignedExpiry(), sign.getCanonicalizedResource(),

				sign.getSignedKeyObjectId(), sign.getSignedKeyTenantId(), sign.getSignedKeyStart(),
				sign.getSignedKeyExpiry(), sign.getSignedKeyService(), sign.getSignedKeyVersion(),

				toString(sign.getSignedAuthorizedUserObjectId()), toString(sign.getSignedUnauthorizedUserObjectId()),
				toString(sign.getSignedCorrelationId()), EMPTY, EMPTY, toString(sign.getSignedIP()),
				toString(sign.getSignedProtocol()), toString(sign.getSignedVersion()),
				toString(sign.getSignedResource()), toString(sign.getSignedSnapshotTime()),
				toString(sign.getSignedEncryptionScope()),

				toString(sign.getCacheControl()), toString(sign.getContentDisposition()),
				toString(sign.getContentEncoding()), toString(sign.getContentLanguage()),
				toString(sign.getContentType()));

		return stringToSign;
	}

	private String encode(UserDelegationSign sign, String signature) {

		Map<String, String> queries = new LinkedHashMap<>();
		queries.put(SASParam.SAS_SIGNED_PERMISSIONS, sign.getSignedPermissions());
		queries.put(SASParam.SAS_START_TIME, sign.getSignedStart());
		queries.put(SASParam.SAS_EXPIRY_TIME, sign.getSignedExpiry());

		queries.put(SASParam.SAS_SIGNED_KEY_OBJECT_ID, sign.getSignedKeyObjectId());
		queries.put(SASParam.SAS_SIGNED_KEY_TENANT_ID, sign.getSignedKeyTenantId());
		queries.put(SASParam.SAS_SIGNED_KEY_START, sign.getSignedKeyStart());
		queries.put(SASParam.SAS_SIGNED_KEY_EXPIRY, sign.getSignedKeyExpiry());
		queries.put(SASParam.SAS_SIGNED_KEY_SERVICE, sign.getSignedKeyService());
		queries.put(SASParam.SAS_SIGNED_KEY_VERSION, sign.getSignedKeyVersion());

		queries.put(SASParam.SAS_IP_RANGE, sign.getSignedIP());
		queries.put(SASParam.SAS_PROTOCOL, sign.getSignedProtocol());

		queries.put(SASParam.SAS_SERVICE_VERSION, sign.getSignedVersion());
		queries.put(SASParam.SAS_SIGNED_RESOURCE, sign.getSignedResource());

		queries.put(SASParam.SAS_SIGNATURE, signature);

		String endcode = queries.entrySet().stream().filter(entry -> StringUtils.isNotBlank(entry.getValue()))
				.map(it -> getQueryParameter(it.getKey(), it.getValue()))
				.collect(Collectors.joining(Constants.AND_SYMBOL));

		return endcode;
	}

	private String getResource(String blobName) {
		if (isEmpty(blobName)) {
			return SAS_CONTAINER_CONSTANT;
		} else {
			return SAS_BLOB_CONSTANT;
		}
	}

	private static String getQueryParameter(String param, String value) {
		String encodeValue = null;
		if (value != null) {
			encodeValue = String.format("%s=%s", EncodeHelper.urlEncode(param), EncodeHelper.urlEncode(value));
		}
		return encodeValue;
	}

	private UserDelegationKey getUserDelegationKey(Duration liveTime) throws Exception {

		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime start = now.minusMinutes(1);
		OffsetDateTime end = now.plusSeconds(liveTime.getSeconds());

		KeyInfo keyInfo = new KeyInfo();
		keyInfo.setStart(Constants.ISO_8601_UTC_DATE_FORMATTER.format(start));
		keyInfo.setExpiry(Constants.ISO_8601_UTC_DATE_FORMATTER.format(end));

		String keyInfoBody = JAXBHelper.marshal(keyInfo);

		Map<String, String> queries = new HashMap<>();
		queries.put(BlobQuery.Parameter.RESTYPE, BlobQuery.Value.SERVICE);
		queries.put(BlobQuery.Parameter.COMP, BlobQuery.Value.USERDELEGATIONKEY);

		Map<String, String> headers = new HashMap<>();
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML);
		Response response = this.ivyClient.post(EMPTY, keyInfoBody, headers, queries);

		UserDelegationKey userDelegationKey = getBody(response, UserDelegationKey.class);
		if (userDelegationKey != null) {
			return userDelegationKey;
		}

		String message = "Get user delegation key is error. Status code " + response.getStatus();
		throw new Exception(message);
	}

	private String toString(Object value) {
		return Objects.toString(value, EMPTY);
	}
}
