# Azure Blob Connector Demo

To run the demo module, you need to provide some information to create credential authenticates.  

## Run directly with Azure Portal

In the project, you only add the dependency in your pom.xml and call public APIs

** Below is an example for connect by client secret **
```yaml
Variables:
  AzureBlob:
    # The application ID that's assigned to your app.
    ClientId: ''
    # The client secret that you generated for your app in the app registration portal.
    ClientSecret: ''
    # The directory tenant the application plans to operate against, in GUID or domain-name format.
    TenantId: ''
    # https://<storage-account>.blob.core.windows.net/
    EndPoint: ''
    # Your container name.
    ContainterName: ''
```
## Run with Azurite at local

Start docker local: Read our [documentation](../azure-blob-connector/README.md). 
Provide the account name and account key in varibles.yaml with [Well Known Storage Account And Key](https://learn.microsoft.com/en-us/azure/storage/common/storage-use-azurite?tabs=visual-studio%2Cblob-storage#well-known-storage-account-and-key)
```yaml
Variables:
	ACCOUNT_NAME: 'devstoreaccount1'
	ACCOUNT_KEY: 'Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw=='	
```