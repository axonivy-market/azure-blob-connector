package com.axonivy.connector.azure.blob.internal.auth;

public final class AzureNamedKeyCredential implements Credential {
	private String accountName;
	private String accountKey;

	/**
	 * Creates a credential with specified {@code name} that authorizes request with
	 * the given {@code key}.
	 * 
	 * @param name - The name of the key credential.
	 * @param key  - The key used to authorize requests.
	 */
	public AzureNamedKeyCredential(String accountName, String accountKey) {
		super();
		this.accountName = accountName;
		this.accountKey = accountKey;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getAccountKey() {
		return accountKey;
	}
}
