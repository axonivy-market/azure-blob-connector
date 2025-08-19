package com.axonivy.connector.azure.blob.internal.constant;

public final class SASParam {
	private SASParam() {
	}

	/**
	 * The snapshot parameters.
	 */
	public static final String SNAPSHOT_QUERY_PARAMETER = "snapshot";

	/**
	 * The versionId parameters.
	 */
	public static final String VERSIONID_QUERY_PARAMETER = "versionid";

	/**
	 * The deletionId parameters.
	 */
	public static final String DELETIONID_QUERY_PARAMETER = "deletionid";

	/**
	 * The SAS service version parameter.
	 */
	public static final String SAS_SERVICE_VERSION = "sv";

	/**
	 * The SAS services parameter.
	 */
	public static final String SAS_SERVICES = "ss";

	/**
	 * The SAS resource types parameter.
	 */
	public static final String SAS_RESOURCES_TYPES = "srt";

	/**
	 * The SAS protocol parameter.
	 */
	public static final String SAS_PROTOCOL = "spr";

	/**
	 * The SAS start time parameter.
	 */
	public static final String SAS_START_TIME = "st";

	/**
	 * The SAS expiration time parameter.
	 */
	public static final String SAS_EXPIRY_TIME = "se";

	/**
	 * The SAS IP range parameter.
	 */
	public static final String SAS_IP_RANGE = "sip";

	/**
	 * The SAS signed identifier parameter.
	 */
	public static final String SAS_SIGNED_IDENTIFIER = "si";

	/**
	 * The SAS signed resource parameter.
	 */
	public static final String SAS_SIGNED_RESOURCE = "sr";

	/**
	 * The SAS signed permissions parameter.
	 */
	public static final String SAS_SIGNED_PERMISSIONS = "sp";

	/**
	 * The SAS signature parameter.
	 */
	public static final String SAS_SIGNATURE = "sig";

	/**
	 * The SAS encryption scope parameter.
	 */
	public static final String SAS_ENCRYPTION_SCOPE = "ses";

	/**
	 * The SAS cache control parameter.
	 */
	public static final String SAS_CACHE_CONTROL = "rscc";

	/**
	 * The SAS content disposition parameter.
	 */
	public static final String SAS_CONTENT_DISPOSITION = "rscd";

	/**
	 * The SAS content encoding parameter.
	 */
	public static final String SAS_CONTENT_ENCODING = "rsce";

	/**
	 * The SAS content language parameter.
	 */
	public static final String SAS_CONTENT_LANGUAGE = "rscl";

	/**
	 * The SAS content type parameter.
	 */
	public static final String SAS_CONTENT_TYPE = "rsct";

	/**
	 * The SAS signed object id parameter for user delegation SAS.
	 */
	public static final String SAS_SIGNED_OBJECT_ID = "skoid";

	/**
	 * The SAS signed tenant id parameter for user delegation SAS.
	 */
	public static final String SAS_SIGNED_TENANT_ID = "sktid";

	/**
	 * The SAS signed key-start parameter for user delegation SAS.
	 */
	public static final String SAS_SIGNED_KEY_START = "skt";

	/**
	 * The SAS signed key-expiry parameter for user delegation SAS.
	 */
	public static final String SAS_SIGNED_KEY_EXPIRY = "ske";

	/**
	 * The SAS signed service parameter for user delegation SAS.
	 */
	public static final String SAS_SIGNED_KEY_SERVICE = "sks";

	/**
	 * The SAS signed version parameter for user delegation SAS.
	 */
	public static final String SAS_SIGNED_KEY_VERSION = "skv";

	/**
	 * The SAS preauthorized agent object id parameter for user delegation SAS.
	 */
	public static final String SAS_PREAUTHORIZED_AGENT_OBJECT_ID = "saoid";

	/**
	 * The SAS agent object id parameter for user delegation SAS.
	 */
	public static final String SAS_AGENT_OBJECT_ID = "suoid";

	/**
	 * The SAS correlation id parameter for user delegation SAS.
	 */
	public static final String SAS_CORRELATION_ID = "scid";

	/**
	 * The SAS directory depth parameter.
	 */
	public static final String SAS_DIRECTORY_DEPTH = "sdd";

	/**
	 * The SAS queue constant.
	 */
	public static final String SAS_QUEUE_CONSTANT = "q";
}
