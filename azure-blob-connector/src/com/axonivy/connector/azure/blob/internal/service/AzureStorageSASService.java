package com.axonivy.connector.azure.blob.internal.service;


import static com.axonivy.connector.azure.blob.internal.constant.Constants.ISO_8601_UTC_DATE_FORMATTER;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.connector.azure.blob.internal.auth.Credential;
import com.axonivy.connector.azure.blob.internal.client.AuthorizationManager;
import com.axonivy.connector.azure.blob.internal.client.AzureStorageClient;
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
    
    private static UserDelegationKey delegationKey;
    private AzureStorageClient httpClient;
	private String endpoint;
	private String container;

	public AzureStorageSASService(Credential credential, String endpoint, String container) {
		this.endpoint = endpoint;
		this.container = container;
		
		AuthorizationManager manager = getAuthorizationManager(credential);
		this.httpClient = new AzureStorageClient(manager);
	}

	public String generateUserDelegationSas(String blobName, Duration liveTime) throws Exception {
		if(delegationKey == null || delegationKey.getSignedStart().isBefore(OffsetDateTime.now().minusMinutes(5))) {
			delegationKey = getUserDelegationKey(liveTime);	
		}
		
		String accountName = EncryptionHelper.getAccountName(UriBuilder.fromPath(this.endpoint).build().toURL());
		
		OffsetDateTime now = OffsetDateTime.now();
		UserDelegationSign sign = new UserDelegationSign();
		sign.setStartTime(ISO_8601_UTC_DATE_FORMATTER.format(now.minusMinutes(1)));
		sign.setExpiryTime(ISO_8601_UTC_DATE_FORMATTER.format(now.plusSeconds(liveTime.getSeconds())));
		sign.setPermissions(READ_PERMISSION);
		
		sign.setSignedObjectId(defaultString(delegationKey.getSignedObjectId()));
		sign.setSignedTenantId(defaultString(delegationKey.getSignedTenantId()));
		sign.setSignedStart(ISO_8601_UTC_DATE_FORMATTER.format((delegationKey.getSignedStart())));
		sign.setSignedExpiry(ISO_8601_UTC_DATE_FORMATTER.format((delegationKey.getSignedExpiry())));
		sign.setSignedService(defaultString(delegationKey.getSignedService()));
		sign.setSignedVersion(defaultString(delegationKey.getSignedVersion()));
		
		sign.setResource(getResource(blobName));
		sign.setVersion(delegationKey.getSignedVersion());
		
		String canonicalName = getCanonicalName(accountName,this.container, blobName);
		String stringToSign = stringToSign(sign, canonicalName, liveTime);
		
		String signature = EncryptionHelper.computeHMac256(delegationKey.getValue(), stringToSign);
		String encodeValue = encode(sign, signature); 
		return encodeValue;
	}

	private String getCanonicalName(String account, String containerName, String blobName) {
		// Container: "/blob/account/containername"
		// Blob: "/blob/account/containername/blobname"
		
		String blob = defaultString(blobName).replace('\\', '/');
		String canonicalName = Stream.of("blob", account, containerName, blob).filter(StringUtils::isNoneEmpty).collect(Collectors.joining("/"));
		return String.format("/%s", canonicalName);
	}
	 
	private String stringToSign(UserDelegationSign sign, String canonicalName, Duration liveTime) {
		String stringToSign = String.join(StringUtils.LF, 
				sign.getPermissions(), 
				sign.getStartTime(),
				sign.getExpiryTime(),
				canonicalName,
				sign.getSignedObjectId(), 
				sign.getSignedTenantId(),
				sign.getSignedStart(),
				sign.getSignedExpiry(),
				sign.getSignedService(), 
				sign.getSignedVersion(),
				defaultString(sign.getAuthorizedAadObjectId()),
				defaultString(sign.getSuoid()),
				/* suoid - empty since this applies to HNS only accounts. */
				defaultString(sign.getCorrelationId()),
				defaultString(sign.getSasIpRange()), 
				defaultString(sign.getProtocol()), 
				sign.getVersion(), 
				sign.getResource(), 
				defaultString(sign.getVersionSegment()), 
				defaultString(sign.getEncryptionScope()), 
				defaultString(sign.getCacheControl()),
				defaultString(sign.getContentDisposition()), 
				defaultString(sign.getContentEncoding()), 
				defaultString(sign.getContentLanguage()), 
				defaultString(sign.getContentType()));
	                		
		return stringToSign;
	}
	
	private String encode(UserDelegationSign sign, String signature) {

		String endcode = Stream.of(
				getQueryParameter(SASParam.SAS_SERVICE_VERSION, sign.getVersion()),
				getQueryParameter(SASParam.SAS_PROTOCOL, sign.getProtocol()),
				getQueryParameter(SASParam.SAS_START_TIME, sign.getStartTime()),
				getQueryParameter(SASParam.SAS_EXPIRY_TIME, sign.getExpiryTime()),
				getQueryParameter(SASParam.SAS_IP_RANGE, sign.getSasIpRange()),
				
				getQueryParameter(SASParam.SAS_SIGNED_OBJECT_ID, sign.getSignedObjectId()),
				getQueryParameter(SASParam.SAS_SIGNED_TENANT_ID, sign.getSignedTenantId()),
				getQueryParameter(SASParam.SAS_SIGNED_KEY_START, sign.getSignedStart()),
				getQueryParameter(SASParam.SAS_SIGNED_KEY_EXPIRY, sign.getSignedExpiry()),
				getQueryParameter(SASParam.SAS_SIGNED_KEY_SERVICE, sign.getSignedService()),
				getQueryParameter(SASParam.SAS_SIGNED_KEY_VERSION, sign.getSignedVersion()),

				/* Only parameters relevant for user delegation SAS. */
				getQueryParameter(SASParam.SAS_PREAUTHORIZED_AGENT_OBJECT_ID, sign.getAuthorizedAadObjectId()),
				getQueryParameter(SASParam.SAS_CORRELATION_ID, sign.getCorrelationId()),

				getQueryParameter(SASParam.SAS_SIGNED_RESOURCE, sign.getResource()),
				getQueryParameter(SASParam.SAS_SIGNED_PERMISSIONS, sign.getPermissions()),
				getQueryParameter(SASParam.SAS_SIGNATURE, signature),
				getQueryParameter(SASParam.SAS_ENCRYPTION_SCOPE, sign.getEncryptionScope()),
				getQueryParameter(SASParam.SAS_CACHE_CONTROL, sign.getCacheControl()),
				getQueryParameter(SASParam.SAS_CONTENT_DISPOSITION, sign.getContentDisposition()),
				getQueryParameter(SASParam.SAS_CONTENT_ENCODING, sign.getContentEncoding()),
				getQueryParameter(SASParam.SAS_CONTENT_LANGUAGE, sign.getContentLanguage()),
				getQueryParameter(SASParam.SAS_CONTENT_TYPE, sign.getContentType()))
				.filter(StringUtils::isNoneEmpty)
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
		HttpResponse<String> response = this.httpClient.post(endpoint, keyInfoBody, headers, queries);

		UserDelegationKey userDelegationKey = getBody(response, UserDelegationKey.class);
		if (userDelegationKey != null) {
			return userDelegationKey;
		}

		String message = "Get user delegation key is error. Status code " + response.statusCode();
		throw new Exception(message);
	}
}
