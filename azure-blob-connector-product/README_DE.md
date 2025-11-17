# Himmelblau Blob Speicher

Himmelblau Blob Speicher ist eine Wolke-basisbezogener Objekt Speicher
#bespringen versehen bei #Microsoft Azur. Es erlaubt du zu lagern große Beträge
von unstructured #Daten, wie Images, Videos, und Dokumente, herein #ein scalable
und preisgünstige Art. #Daten ist gelagert in Behälter innerhalb ein Speicher
Konto, und es kann sein zugegriffen via HTTP/HTTPS, machend ihm ideal für die
Integration in euren #Axon Efeu Dienstliche Arbeitsgänge.

Dieser Anschluss:
- Unterstützt du in implementieren Zugang zu Himmelblau Blob Speicher.
- Unterstützt du in #hochladen #einverstanden zu eure Himmelblaues Blob Speicher
  - direkt von einen #Axon Efeu dienstlichen Arbeitsgang.
- Schafft ein #Herunterladen Band mit ein expiry Datum.

## Einrichtung

In dem Projekt, du zufügen nur die Kolonie herein eure pom.xml Und rufen
öffentlich APIs

**1. Füg zu Kolonie**
```XML
	<dependency>
		<groupId>com.axonivy.connector</groupId>
		<artifactId>azure-blob-connector</artifactId>
		<version>${process.analyzer.version}</version>
	</dependency>
```
**2. Himmelblau Blob Zusammenhang in Variablen**

Du brauchst zu versehen Himmelblau Blob Zusammenhang in Variablen.yaml. Unten
ist ein Beispiel für koppeln bei Kunden Geheimnis
```yaml
Variables:
  AzureBlob:
    # The application ID that's assigned to your app.
    ClientId: ''
    # The client secret that you generated for your app in the app registration portal.
    ClientSecret: ''
    # The directory tenant the application plans to operate against, in GUID or domain-name format.
    TenantId: ''
    # The storage endpoint: https://<storage-account>.blob.core.windows.net
    BaseUrl: ''
    # Your container name.
    ContainerName: ''
```

> [!BEACHTE] Die Variable `AzureBlob.ContainterName` Ist #umbenennen zu
> `AzureBlob.ContainerName` Von 13.1.4.

### Für Arbeitsgang #grafische Benutzeroberfläche
**1. Was ist unterstützt herein BlobStorage Callable Ersatz Arbeitsgang?**
![Himmelblau-blob-Anschluss](images/BlobStorageFunctions.png)

**2. Wie zu rufen ein Ereignis von BlobStorage Callable Ersatz Arbeitsgang?**
- Von den "Extensionen"-Menue in die Tool Bar, wir können sehen #kein
  BlobStorage Element
  ![Himmelblau-blob-Anschluss](images/ElementInExtensions.png)

- Wir können einen Arbeitsgang zeichnen mit uploadFromUrl Auslese und #auffangen
  einige Auskunft gleichnamig: Extern url, blob Name, das Telefonbuch auf
  Himmelblau Blob Behälter, ..

![Himmelblau-blob-Anschluss](images/AddBlobStorageAndCallFunction.png)

### Für #Java Entwickler
**1. Ruf den Erbauer zu setzen einige einfache Auskunft.**
```java
	/**
	 * Create a AzureBlobStorageService with the give identity credential, storage account
	 * and container
	 * 
	 * @param tokenCredential - The credential type	 
	 * @param container       - The container name
	 */	
	public AzureBlobStorageService(Credential tokenCredential, String account, String container) {}
```

**2. Antrag Bitten zu Himmelblau Blob Speicher muss sein autorisiert. Du musst
einen #Berechtigungsnachweis zu schaffen.**

  -  Dieser #Berechtigungsnachweis beglaubigt die geschaffene Bedienung
     Schulleiter durch sein Kunden Geheimnis
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

 -  Dieser #Berechtigungsnachweis beglaubigt die geschaffene Bedienung
    Schulleiter durch sein Konto und wesentlich.
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

**3. Du kannst rufen `uploadFromUrl` zu #hochladen eine Datei von url,
`getDownloadLink` zu bekommen #Herunterladen Band von ein blob.**
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

### Beispiel

Unten ist ein simples Beispiel für #hochladen eine Datei von url und bekommen
temporäres #Herunterladen Band.
``` java
	Credential tokenCredential = new ClientSecretCredential(TENANT_ID, CLIENT_ID, SECRET_VALUE);
	storageService = new AzureBlobStorageService(tokenCredential, STORAGE_ACCOUNT, TEST_CONTAINER);

	// Upload file from url
	String blobName = storageService.uploadFromUrl("https://sample.com/video.mp4");
	// Get temporary download link
	String downloadLink = storageService.getDownloadLink(blobName);
```

## Demo

### Gerannt Einheimische mit #Azurit Hafenarbeiter Image

Start Hafenarbeiter zu testen Himmelblau Blob Speicher örtlich.

##### Zug #Azurit V3 Hafenarbeiter Image und rennen ihm

> Note. Finde #mehr Hafenarbeiter Images Anhängsel herein
> <https://mcr.microsoft.com/v2/azure-storage/azurite/tags/list>

```bash
docker pull mcr.microsoft.com/azure-storage/azurite
```

```bash
docker run -p 10000:10000 -p 10001:10001 -p 10002:10002 mcr.microsoft.com/azure-storage/azurite
```

`-p 10000:10000` wollen darlegen blob Bedienung#voreingestellt Abhorchen
Hafenstadt. `-p 10001:10001` wollen darlegen Reihe Bedienungs #voreingestellt
Abhorchen Hafenstadt. `-p 10002:10002` wollen darlegen Tisch Bedienungs
#voreingestellt Abhorchen Hafenstadt.

#### Installation mit Hafenarbeiter fasst ab

Gerannt an Wurz Ordner von dem Projekt:
```
make app_local_compose_up
```

Für anderen Wege, Kontrolle
[DockerHub](https://github.com/Azure/Azurite/blob/main/README.md#dockerhub)

### Wie zu Forscher #der #Daten?

- Installier
  https://Himmelblau.microsoft.com/en-Uns/Produkte/Speicher/Speicher-Forscher
- Einrichtung zu zugreifen ihm

#Fern Auskunft kann auch sein gefunden an die [Speicher
Forscher](https://learn.microsoft.com/en-us/azure/storage/storage-explorer/vs-azure-tools-storage-manage-with-storage-explorer)

Versieh einen Konto Namen und verrechne wesentlich in Variablen.yaml Mit [Kuhle
Bekannt Speicher Konto Und
Wesentlich](https://learn.microsoft.com/en-us/azure/storage/common/storage-use-azurite?tabs=visual-studio%2Cblob-storage#well-known-storage-account-and-key)

[StorageAccountAndKey](images/DevAccountKey.png)

