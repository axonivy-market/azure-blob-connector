# Azure Blob Connector Demo

To run the demo module, you need to provide some information to create credential authenticates.  

## Run directly with Azure Portal

In the project, you only add the dependency in your pom.xml and call public APIs

** Below is an example for connect by client secret **
```yaml
Variables:
	CLIENT_ID: 'value'
	CLIENT_SECRET: 'value'
	TENANT_ID: 'value'
	END_POINT: 'https://<storage-account>.blob.core.windows.net/'
	TEST_CONTAINTER: 'containt_name'
```

If you want to create credential authenticates by account name, account key, .. You need to define variable names in variables.yaml
Then you need to get it from Ivy.var in {@link UploadBean} and create  BlobServiceClient
```java
	private static final String ACCOUNT_NAME = Ivy.var().get("ACCOUNT_NAME");
	private static final String ACCOUNT_KEY = Ivy.var().get("ACCOUNT_KEY");
	private static final String END_POINT = Ivy.var().get("END_POINT");
	private static final String TEST_CONTAINTER = Ivy.var().get("TEST_CONTAINTER");
	...
	
	BlobServiceClient blobServiceClient = BlobServiceClientHelper.getBlobServiceClient(ACCOUNT_NAME, ACCOUNT_KEY, END_POINT);
``` 

## Run with Azurite at local

Start docker local: Read our [documentation](../azure-blob-connector/README.md). 
Provide the account name and account key in varibles.yaml with [Well Known Storage Account And Key](https://learn.microsoft.com/en-us/azure/storage/common/storage-use-azurite?tabs=visual-studio%2Cblob-storage#well-known-storage-account-and-key)
```yaml
Variables:
	ACCOUNT_NAME: 'devstoreaccount1'
	ACCOUNT_KEY: 'Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw=='	
```

