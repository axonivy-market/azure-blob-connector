# Azure Blob Storage

Azure Blob Storage ist ein Cloud-basierter Objektspeicherdienst, der von Microsoft Azure bereitgestellt wird. Er ermöglicht es dir, große Mengen unstrukturierter Daten wie Bilder, Videos und Dokumente auf skalierbare und kostengünstige Weise zu speichern. Die Daten werden in Containern innerhalb eines Speicherkontos gespeichert und können über HTTP/HTTPS abgerufen werden, wodurch sie sich ideal für die Integration in Ihre Axon Ivy Business Prozesse eignen.

Dieser Konnektor:
- Unterstützt dich bei der Implementierung des Zugriffs auf Azure Blob Storage.
- Unterstützt dich beim Hochladen von Inhalten auf Ihren Azure Blob Storage - direkt aus einem Axon Ivy Geschäftsprozess.
- Erstellt einen Download-Link mit expiry date (“Verfallsdatum”).

## Setup

In the project, you only add the dependency in your pom.xml and call public APIs

**1. Add dependency**
```XML
	<dependency>
		<groupId>com.axonivy.connector</groupId>
		<artifactId>azure-blob-connector</artifactId>
		<version>${process.analyzer.version}</version>
	</dependency>
```
**2. Azure Blob connection in variables**

You need to provide Azure Blob connection in variables.yaml. Below is an example for connect by client secret
```yaml
Variables:
  AzureBlob:
    # The application ID that's assigned to your app.
    ClientId: ''
    # The client secret that you generated for your app in the app registration portal.
    ClientSecret: ''
    # The directory tenant the application plans to operate against, in GUID or domain-name format.
    TenantId: ''
    # The uuid of rest client to storage endpoint
    RestClientUUID: ''
    # Your container name.
    ContainterName: ''
```

### For Process GUI
**1. What is support in BlobStorage Callable Sub Process?**
 ![azure-blob-connector](images/BlobStorageFunctions.png)

**2. How to call an event from BlobStorage Callable Sub Process?**
- From Extensions on Tool Bar, we can see a BlobStorage element
![azure-blob-connector](images/ElementInExtensions.png)

- We can draw a process with uploadFromUrl selection and field some information like: external url, blob name, the directory on Azure Blob Container, .. 

![azure-blob-connector](images/AddBlobStorageAndCallFunction.png)

### For Java Developer
**1. Call the constructor to set some basic information.**
```java
	/**
	 * Create a AzureBlobStorageService with the give identity credential, restClientUUID
	 * and container
	 * 
	 * @param tokenCredential - The credential type
	 * @param restClientUUID - The Ivy rest client UUID
	 * @param container       - The container name
	 */	
	public AzureBlobStorageService(Credential tokenCredential, UUID restClientUUID, String container) {}
```

**2. Application requests to Azure Blob Storage must be authorized. You must to create a BlobServiceClient.**

  -  This credential authenticates the created service principal through its client secret
```java
	/**
	 * Creates a ClientSecretCredential with the given identity client options.
	 * 
	 * @param clientId     - the client ID of the application
	 * @param clientSecret - the secret value of the Microsoft Entra application.
	 * @param tenantId     - the tenant ID of the application.
	 */
	public ClientSecretCredential(String tenantId, String clientId, String clientSecret) {}
```

 -  This credential authenticates the created service principal through its account and key. 
```java
	/**
	 * Creates a credential with specified {@code name} that authorizes request with
	 * the given {@code key}.
	 * 
	 * @param name - The name of the key credential.
	 * @param key  - The key used to authorize requests.
	 */
	public AzureNamedKeyCredential(String accountName, String accountKey) {}
```

**3. You can call `uploadFromUrl` to upload a file from url, `getDownloadLink`  to get download link of a blob.**
```java
	/**
	 * The API to copy operation from a source object URL
	 * @param url - The source URL to upload from
	 * @return - The blob name
	 */
	public String uploadFromUrl(String url);
	
	/**
	 * The API to create a temporary download link with expired time 
	 * @param url - The blob name
	 * @return - The url for download
	 */
	public String getDownloadLink(String blobName);
```	

### Example

Below is a simple example for upload a file from url and get temporary download link.
``` java
	Credential tokenCredential = new ClientSecretCredential(TENANT_ID, CLIENT_ID, SECRET_VALUE);
	storageService = new AzureBlobStorageService(tokenCredential, REST_CLIENT_UUID, TEST_CONTAINTER);
	
	// Upload file from url
	String blobName = storageService.uploadFromUrl("https://sample.com/video.mp4");
	// Get temporary download link
	String downloadLink = storageService.getDownloadLink(blobName);
```

## Demo

### Run with Azurite at local

Start docker local:  
You can run  with docker or docker-compose
 
#### Run Azurite V3 docker image

> Note. Find more docker images tags in <https://mcr.microsoft.com/v2/azure-storage/azurite/tags/list>

```bash
docker pull mcr.microsoft.com/azure-storage/azurite
```

```bash
docker run -p 10000:10000 -p 10001:10001 -p 10002:10002 mcr.microsoft.com/azure-storage/azurite
```

`-p 10000:10000` will expose blob service's default listening port.
`-p 10001:10001` will expose queue service's default listening port.
`-p 10002:10002` will expose table service's default listening port.

#### Run docker compose at root folder of project

```
make app_local_compose_up
```

For other ways, read out [DockerHub](https://github.com/Azure/Azurite/blob/main/README.md#dockerhub)

### How to explorer data?

- Install https://azure.microsoft.com/en-us/products/storage/storage-explorer
- Setup to access the local 
Read our [Storage Explorer](https://learn.microsoft.com/en-us/azure/storage/storage-explorer/vs-azure-tools-storage-manage-with-storage-explorer)

Provide the account name and account key in varibles.yaml with [Well Known Storage Account And Key](https://learn.microsoft.com/en-us/azure/storage/common/storage-use-azurite?tabs=visual-studio%2Cblob-storage#well-known-storage-account-and-key)

[StorageAccountAndKey](images/DevAccountKey.png)