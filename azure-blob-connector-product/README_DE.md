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
    # https://<storage-account>.blob.core.windows.net/
    EndPoint: ''
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
**1. Call the constructor to set some basic information.  Each instance of the advanced process analyzer should care about one specific process model. This way we can store some private information (e.g. simplified model) in the instance and reuse it for different calculations on this object.**
```java
	/** 
	 * @param process - The process that should be analyzed.	 
	 */
	public AzureBlobStorageService(BlobServiceClient blobServiceClient, String container)
```

**2. Application requests to Azure Blob Storage must be authorized. You must to create a BlobServiceClient.**

  -  This credential authenticates the created service principal through its client secret
```java
	/**
	 *  Create client to a storage account. 
	 * @param clientId - the client ID of the application
	 * @param clientSecret - the secret value of the Microsoft Entra application.
	 * @param tenantId - the tenant ID of the application.
	 * @param endpoint - URL of the service
	 * @return a {@link BlobServiceClient} created from the configurations in this builder
	 */
	public static BlobServiceClient getBlobServiceClient(String clientId, String clientSecret, String tenantId, String endpoint) {}
```

  -  This credential authenticates the created service principal through its connection string
```java
	/**
	 * Create client to a storage account. 
	 * @param connectionString - connection string of the storage account
	 * @param endpoint - URL of the service
	 * @return  a {@link BlobServiceClient} created from the configurations in this builder
	 */
	public static BlobServiceClient getBlobServiceClient(String connectionString, String endpoint) {
```

 -  This credential authenticates the created service principal through its account and key. 
```java
	/**
	 * * Create client to a storage account. 
	 * @param accountName The account name associated with the request.
     * @param accountKey The account access key used to authenticate the request.
	 * @param endpoint - URL of the service
	 * @return  a {@link BlobServiceClient} created from the configurations in this builder
	 */
	public static BlobServiceClient getBlobServiceClient(String accountName, String accountKey, String endpoint) {}
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
```
	BlobServiceClient blobServiceClient = BlobServiceClientHelper.getBlobServiceClient(CLIENT_ID, SECRET_VALUE,TENANT_ID, END_POINT);
	StorageService storageService = new AzureBlobStorageService(blobServiceClient, TEST_CONTAINTER);

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