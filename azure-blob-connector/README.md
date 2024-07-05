# How to run at local

## Run with DockerHub

You can run  with docker or docker-compose
 
### Run Azurite V3 docker image

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

### Run docker compose at root folder of project

```
	make app_local_compose_up
```

For other ways, read out [DockerHub](https://github.com/Azure/Azurite/blob/main/README.md#dockerhub)


## How to explorer data?

- Install https://azure.microsoft.com/en-us/products/storage/storage-explorer
- Setup to access the local 
Read our [Storage Explorer](https://learn.microsoft.com/en-us/azure/storage/storage-explorer/vs-azure-tools-storage-manage-with-storage-explorer)