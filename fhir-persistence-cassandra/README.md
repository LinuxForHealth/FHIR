# FHIR payload offload in Cassandra

Experimental feature to support the storage of the JSON resource payload outside of the RDBMS.

There are two projects:

1. fhir-persistence-cassandra: implements the FHIRPayloadPersistence API
2. fhir-persistence-cassandra-app: utility to create Cassandra keyspace and tables as well as a tool to reconcile contents of the Cassandra datastore with the RDBMS.

## Limitations

Only suitable for development work. Should not be used for production or any instances
where PHI is involved until the client-server connection can be encrypted and supports
authentication.

## Packaging

As this feature is still experimental, it currently isn't packaged with the main server. Users
wishing to work with the feature must deploy the fhir-persistence-cassandra-x.y.z-buildid.jar
to the Liberty server userlib directory along with the Cassandra client driver and its
dependencies.

For development, add `fhir-persistence-cassandra` as a dependency to the fhir-server project `pom.xml`
file.

## Configuration

```
        "persistence": {
            "factoryClassname": "org.linuxforhealth.fhir.persistence.cassandra.FHIRPersistenceJDBCCassandraFactory",
            "datasources": {
                "default": {
                    ...
                },
            },
            "payload": {
                "default": {
                    "__comment": "Cassandra configuration for storing FHIR resource payload data",
                    "type": "cassandra",
                    "connectionProperties" : {
                        "contactPoints": [
                            { "host": "a-cassandra-host", "port": 9042 }
                        ],
                        "localDatacenter": "datacenter1",
                        "tenantKeyspace": "keyspace_to_use",
                        "localCoreConnectionsPerHost": 1,
                        "localMaxConnectionsPerHost": 4,
                        "remoteCoreConnectionsPerHost": 1,
                        "removeMaxConnectionsPerHost": 4
                    }
                },
            }
```

## Bootstrap

To create the Cassandra keyspace and tables, run the following:

```
java -jar /path/to/fhir-persistence-cassandra-app-*-SNAPSHOT-cli.jar \
  --fhir-config-dir /path/to/ibm/fhir/server/wlp/usr/servers/defaultServer --tenant-id [your-tenant-name] --bootstrap
```

The command will create a new keyspace for each of the datastores configured under the payload element in the tenant's
fhir-server-config.json file. In most cases, there will be a single datastore called `default`.

## Reconciliation

Reconciliation is a process used to scan the Cassandra resource payload tables and verify that each record is associated
with a parent record in the RDBMS. Discrepancies are flagged and can be deleted.

To only identify discrepancies without deleting any records, include the `--dry-run` option:

```
java -jar /path/to/fhir-persistence-cassandra-app-*-SNAPSHOT-cli.jar \
  --fhir-config-dir /path/to/ibm/fhir/server/wlp/usr/servers/defaultServer \
  --db-type postgresql \
  --db-properties fhiradmin.properties \
  --tenant-id default \
  --ds-id default \
  --reconcile \
  --dry-run
```

The `fhirdb.properties` file is the same format as used by the fhir-persistence-schema CLI tool. For example
a connection to a local database used for development would look like this:

```
db.host=localhost
db.port=5432
db.database=fhirdb
db.type=postgres
db.default.schema=fhirdata
user=fhiradmin
password=change-password
```

The current implementation does not support checkpointing so must be allowed to scan the entire dataset in one pass.

## Running Cassandra in Docker

Create a single node Cassandra container which can be used for development and testing of the payload persistence feature:

```
podman run --name fhircass1 -v ./data/fhircass1:/var/lib/cassandra:z -e CASSANDRA_CLUSTER_NAME=fhir -e CASSANDRA_DC=datacenter1 -e CASSANDRA_RACK=rack1 -e CASSANDRA_ENDPOINT_SNITCH=GossipingPropertyFileSnitch -d -p 9042:9042 cassandra:latest
```
