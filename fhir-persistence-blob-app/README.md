# Payload Offload for Azure Blob

The IBM FHIR Server can be configured to store the resource payload records in an Azure Blob container. Each record is stored as a JSON string with UTF-8 encoding. The IBM FHIR Server relies on the blob service to compress/encrypt the data at rest. The blob service must apply the necessary security controls required when storing PHI, and connection to the service must use an encrypted transport.

## Configuring the IBM FHIR Server to use Payload Offloading with Azure Blob

To enable payload offloading using Azure Blob storage, complete the steps summarized below:

1. Pick a container name to use for each tenant/datasource combination;
2. Create a database.properties file with connection information for the FHIRSERVER user (not FHIRADMIN);
3. Configure payload offloading in the main `default/fhir-server-config.json` file;
4. Configure payload offloading in each tenant-specific `fhir-server-config.json` file;
5. Create the container using the Azure Blob user interface, the Azure Blob API or the IBM FHIR Server fhir-persistence-blob-app-*-cli.jar tool;
6. (Optional) Periodically run the reconciliation tool to remove any orphan resource records from the offload datastore.

Take the following restrictions into account:

1. Payload offloading is enabled at the server level. Offloading cannot be enabled/disabled on a per-tenant basis. If you want to support offloading for just one tenant, use a different IBM FHIR Server instance;
2. Payload offloading must be configured prior to ingesting any resource data;
3. Payload offloading must not be disabled after resource data has been ingested;
4. When payload offloading is enabled, FHIR resources are limited to 4 MiB in size when rendered as uncompressed JSON. If this limit is too small, please raise an issue in the IBM FHIR Server [repository](https://github.com/IBM/FHIR/issues) with a description of your use case.


### 1. Pick a Container Name

Each tenant/datasource combination requires its own container. Use a name that can be easily identified as belonging to the tenant and datasource, subject to the Azure Blob service naming restrictions.

### 2. Create the `database.properties` file

Create a properties file containing the RDBMS connection information. Note that the user should not be the FHIRADMIN user which is only used by the RDBMS schema creation tool. Use the database user configured in the IBM FHIR Server `datasource.xml` file. Following the principle of least privilege access, this user typically has just the right set of privileges for the application to use objects in the FHIR data (`fhirdata`) schema:

```
db.host=localhost
db.port=5432
db.database=fhirdb
user=fhirserver
password=change-password
currentSchema=fhirdata
```

### 3. Add Payload Offload Configuration to `default/fhir-server-config.json`

In the main `default/fhir-server-config.json`, configure the `fhirServer/persistence/factoryClassname` as shown below and add a `fhirServer/persistence/payload` block containing the connection information for each datasource you have defined under `fhirServer/persistence/datasources` (typically there is just one datasource called `default`). 
```
{
    "__comment": "FHIR Server configuration",
    "fhirServer": {
        ...
        "persistence": {
            "factoryClassname": "com.ibm.fhir.persistence.blob.FHIRPersistenceJDBCBlobFactory",
            "datasources": {
                "default": {
                    ...
                }
            },
            "payload": {
                "default": {
                    "__comment": "Azure Blob (azurite docker) configuration for storing FHIR resource payload data",
                    "type": "azure.blob",
                    "connectionProperties" : {
                        "connectionString": "your-azure-connection-string",
                        "containerName": "default-default"
                    }
                }
            }
        }
    }
}
```

Container names allowed by Azure Blob are more restrictive than IBM FHIR Server tenant and datasource names (for example they must be lower case and `_` is not allowed - see the [official documentation](https://docs.microsoft.com/en-us/azure/storage/blobs/storage-blobs-introduction#containers) for more details). For this reason, the container name for each tenant and datasource must be specified in the `fhirServer/persistence/payload/connectionProperties/containerName` property.

### 4. Configure Payload Offloading Per Tenant

In each tenant `fhir-server-config.json` file, add a `fhirServer/persistence/payload` block containing the connection information for each datasource you have defined under `fhirServer/persistence/datasources` (typically there is just one datasource called `default`). 

```
{
    "__comment": "FHIR Server configuration",
    "fhirServer": {
        ...
        "persistence": {
            "datasources": {
                "default": {
                    ...
                }
            },
            "payload": {
                "default": {
                    "__comment": "Azure Blob (azurite docker) configuration for storing FHIR resource payload data",
                    "type": "azure.blob",
                    "connectionProperties" : {
                        "connectionString": "your-azure-connection-string",
                        "containerName": "default-default"
                    }
                }
            }
        }
    }
}
```


### 5. Create the Container

The container can be created using the Azure Blob service or by running the following command:

```
java -jar fhir-persistence-blob-app-*-cli.jar \
    --fhir-config-dir /path/to/wlp/usr/servers/defaultServer \
    --tenant-id <tenant-id> \
    --ds-id <ds-id> \
    --create-container \
    --confirm
```

The container name will be read from the tenant's fhir-server-config.json configuration, so it is important to complete that configuration step before running this command.

The command is designed to be idempotent - it checks first to see if the container already exists before attempting to create it. If the container is otherwise created after the exists check but before the create command is issued, the command will fail (this is a very small window).

## 6. Running Reconciliation

When payload offloading is configured, the IBM FHIR Server stores the payload in an Azure Blob container. This store operation is not transactional, so if the global transaction is rolled back, any cleanup of the payload data stored during the transaction must be handled by the application code.

If the IBM FHIR Server terminates before this cleanup is completed or the Azure Blob service is no longer available, records may be left in the container which are not associated with any resource record in the RDBMS. Although this is likely to be uncommon, the IBM FHIR Server provides a reconciliation tool to scan the container and look for resource payload records which do not have a corresponding RDBMS record. The reconciliation tool can optionally delete these records.

The following examples use PostgreSQL as the database type, but the tool also supports db2 and derby as options.

To identify orphan records without deleting anything, run:
```
java -jar fhir-persistence-blob-app-*-cli.jar \
    --fhir-config-dir /path/to/wlp/usr/servers/defaultServer \
    --tenant-id default \
    --ds-id default \
    --db-properties database.properties \
    --db-type postgresql \
    --reconcile \
    --dry-run \
    --max-scan-seconds 600 
```

To identify and delete orphan records, run:
```
java -jar fhir-persistence-blob-app-*-cli.jar \
    --fhir-config-dir /path/to/wlp/usr/servers/defaultServer \
    --tenant-id default \
    --ds-id default \
    --db-properties database.properties \
    --db-type postgresql \
    --reconcile \
    --confirm \
    --max-scan-seconds 600 
```

To continue an earlier scan and delete which didn't complete, use the last continuation token value reported in the log of the previous run then specify when running the command again:

```
java -jar fhir-persistence-blob-app-*-cli.jar \
    --fhir-config-dir /path/to/wlp/usr/servers/defaultServer \
    --tenant-id default \
    --ds-id default \
    --db-properties database.properties \
    --db-type postgresql  \
    --reconcile \
    --confirm \
    --max-scan-seconds 600 \
    --continuation-token "<token>"
```
The continuation token can be identified in the log by searching for the string `__CONTINUATION_TOKEN__ =`. The application logs an INFO message after each page of records is processed, so use the last occurrence found in the log. The value of the continuation token is opaque and only meaningful to the Azure Blob service.

## Development

For development, payload offload can be configured to point to a local `azurite` container which emulates the API of the Azure Blob store service. Run the following command to start a local `azurite` container:

```
podman run -d -p 10000:10000 \
    -v ./data/azurite:/data:z \
    mcr.microsoft.com/azure-storage/azurite \
    azurite-blob --blobHost 0.0.0.0 --blobPort 10000
```

More info on using `azurite` for development can be found in the official documents here: https://docs.microsoft.com/en-us/azure/storage/common/storage-use-azurite?tabs=docker-hub.

### Checking Stored Values

When new resources are ingested, the class `com.ibm.fhir.persistence.blob.BlobStorePayload` logs a `FINE` message which can be seen in the Liberty Profile `trace.log` file:

```
Payload storage path: 97/my-blob-5/7/b0a2fb75-1ef8-45c8-b5d7-658350fb45cc
```
Where:
| Value | Meaning |
| ----- | ------- |
|        97 | `resource_type_id` primary key from RDBMS `resource_types` table |
| my-blob-5 | resource logical-id |
|         7 | resource version number `meta.versionId` |
| b0a2fb75-1ef8-45c8-b5d7-658350fb45cc | resource payload key matching `xx_resources.resource_payload_key` |

For debugging, you can use the fhir-persistence-blob-app utility to read a payload offload value directly from the blob store as follows:

```
java -jar fhir-persistence-blob-app-*-cli.jar \
    --fhir-config-dir /path/to/wlp/usr/servers/defaultServer \
    --tenant-id default \
    --ds-id default \
    --read "97/my-blob-5/7/b0a2fb75-1ef8-45c8-b5d7-658350fb45cc"
```

The `--read` argument takes the full payload key reported in the IBM FHIR Server `trace.log` output described previously.

The resource payload key value is used to make sure that the RDBMS record and offload record are correctly tied together when ingesting records concurrently. The resource payload key is generated by the IBM FHIR Server when a resource is created or updated and stored in the RDBMS `xx_resources` table as well as being used to form part of the payload offload key. This ensures that if multiple concurrent processes attempt to create or update a particular resource at exactly the same time, the payload offload record can be distinguished among the attempts. Database locking guarantees that each resource version will be unique. If two create or updates occur at exactly the same time, one will fail with a version mismatch and cause the transaction to be rolled back. Because the payload has already been stored at this point, the rollback mechanism attempts to delete the payload offload entry which is no longer required.

If this delete fails, or a transaction fails for any reason, the payload offload record may exist without a corresponding record in the RDBMS. These orphan records can be identified and deleted using the fhir-persistence-blob-app utility.

If you know the resource type, logical id and version but not the resource payload key, you can still read the payload data. Note that in this case, there may be multiple blob keys which match. If multiple resources are returned, it is up to you to determine which is the correct version. It is worth repeating that this tool is only intended for development. All accesses to FHIR resources in production scenarios should be made via the IBM FHIR Server REST endpoints.

To read a resource without knowing the resource payload key, specify the `--read` option and include only the resource-type-id, logical-id and version values as shown here:

```
java -jar fhir-persistence-blob-app-*-cli.jar \
    --fhir-config-dir /path/to/wlp/usr/servers/defaultServer \
    --tenant-id default \
    --ds-id default \
    --read "97/my-blob-5/7"
```

If you provide configuration for the RDBMS datasource, you can use the resource-type-name instead of the resource-type-id value:

```
java -jar fhir-persistence-blob-app-*-cli.jar \
    --fhir-config-dir /path/to/wlp/usr/servers/defaultServer \
    --tenant-id default \
    --ds-id default \
    --db-properties database.properties \
    --db-type postgresql \
    --read "Observation/my-blob-5/7"
```

The utility will read the resource-type-name to resource-type-id mapping from the RDBMS and use this to tranlate `Observation` to the id (`97`) used in the Azure Blob path.

## Command Line Options

| Option | Argument Type | Description |
| -------- | ---- | ----------- |
| --fhir-config-dir `<dir>` | String | (Required) Path to the IBM FHIR Server base server configuration directory. |
| --tenant-id `<tenant-id>` | String | (Optional, Default="default") The tenant identifier. |
| --ds-id `<ds-id>` | String | (Optional, Default="default") The datasource identifier. |
| --db-properties `<db-properties>` | String | (Required for running with `--reconcile`) File name of a `.properties` file containing database connection details. |
| --db-type `<db-type>` | String | (Required for running with `--reconcile`) The database type, `postgresql`, `db2` or `derby`. |
| --reconcile | | Run the reconciliation process |
| --read `<blob-key>` | String | Read the resource payload identified by its blob-key |
| --create-container | | Create the container. The container name is obtained by reading the IBM FHIR Server tenant payload configuration. |
| --dry-run | | (Default) Do not make any changes (create/delete). |
| --confirm | | (Optional) Enable changes (create/delete). |
| --max-scan-seconds `<seconds>` | Integer | (Optional) Stop the scan after `<seconds>`. The scan emits a continuation token which can be used to restart the scan from a prior point. |
| --continuation-token `<token>` | String | (Optional) Start the scan from a previous point.|
