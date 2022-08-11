# Payload Offloading for Azure Blob

## Running Reconciliation

When payload offloading is configured, the LinuxForHealth FHIR Server stores the payload in an Azure Blob container. This store operation is not transactional, so if the global transaction is rolled back, any cleanup of the payload data stored during the transaction must be handled by the application code.

If the LinuxForHealth FHIR Server terminates before this cleanup is completed or the Azure Blob service is no longer available, records may be left in the container which are not associated with any resource record in the RDBMS. Although this is likely to be uncommon, the LinuxForHealth FHIR Server provides a reconciliation tool to scan the container and look for resource payload records which do not have a corresponding RDBMS record. The reconciliation tool can optionally delete these records.

By default, the reconciliation tool will operate in `dry-run` mode which prevents the tool from making any changes (it is read-only). To enable operations to actually make changes, you must disable dry-run with the `--no-dry-run` command line option. If both `--dry-run` and `--no-dry-run` are specified, the last one wins.

The following examples use PostgreSQL as the database type, but the tool also supports PostgreSQL and derby as options.

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
    --no-dry-run \
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
    --no-dry-run \
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

Note: you may need to specify `serviceVersion` in the LinuxForHealth FHIR Server payload connection properties if the azurite image is older than the Azure Blob client API.

### Checking Stored Values

When new resources are ingested, the class `org.linuxforhealth.fhir.persistence.blob.BlobStorePayload` logs a `FINE` message which can be seen in the Liberty Profile `trace.log` file:

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

The dot (`.`) character is valid for FHIR resource ids, but there are restrictions on where it can be used in an Azure Blob storage path. The [documentation](https://docs.microsoft.com/en-us/rest/api/storageservices/naming-and-referencing-containers--blobs--and-metadata#blob-names) states that "No path segments should end with a dot (.)". To work around this, the logical id of the payload storage path is encoded by replacing any occurrence of `.` with `*` (an unreserved character under [RFC 2396](https://www.ietf.org/rfc/rfc2396.txt)). For example:
```
97/my.blob./7/b0a2fb75-1ef8-45c8-b5d7-658350fb45cc
```
becomes
```
97/my*blob*/7/b0a2fb75-1ef8-45c8-b5d7-658350fb45cc
```

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
| --db-type `<db-type>` | String | (Required for running with `--reconcile`) The database type, `postgresql` or `derby`. |
| --reconcile | | Run the reconciliation process |
| --read `<blob-key>` | String | Read the resource payload identified by its blob-key |
| --create-container | | Create the container. The container name is obtained by reading the IBM FHIR Server tenant payload configuration. |
| --dry-run | | (Default) Do not make any changes (create/delete). |
| --no-dry-run | | (Optional) Enable changes (create/delete). |
| --max-scan-seconds `<seconds>` | Integer | (Optional) Stop the scan after `<seconds>`. The scan emits a continuation token which can be used to restart the scan from a prior point. |
| --continuation-token `<token>` | String | (Optional) Start the scan from a previous point.|
