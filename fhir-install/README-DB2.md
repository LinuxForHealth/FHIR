# IBM FHIR Server Install - Using docker container db2 as data store

Running fhir server integration tests using docker db2.

## Prerequisites

- [Docker](https://www.docker.com)

## Build
- Navigate to the `fhir-install/docker` directory;
- Using Docker Terminal, execute:

```sh
docker build -t fhirserverdb2 . -f Dockerfile-db2 --squash
```

## Run

Once the image is built, start it with:

```sh
docker run -itd --name fhirdb2 --privileged=true -p 50000:50000 -e LICENSE=accept -e DB2INST1_PASSWORD=change-password  -v <db storage dir>:/database --rm fhirserverdb2
```

Where `<db storage dir>` is the persistent storage filesystem/directory to store your database data/instance configuration/transaction logs.

## Configuration

The db2 docker container is started with the default fhirdb database created, for the integration test, we need to create three more database as following:

```sh
docker exec -ti fhirdb2 bash -c "su - db2inst1"
db2 CREATE DB study using codeset UTF-8 territory us PAGESIZE 32768
db2 CREATE DB ref using codeset UTF-8 territory us PAGESIZE 32768
db2 CREATE DB profile using codeset UTF-8 territory us PAGESIZE 32768
```

For the created databases fhirdb, study, ref and profile, please use the [db schema tool](https://github.com/IBM/FHIR/tree/master/fhir-persistence-schema) in fhir-persistence-schema project to deploy new schema and grant privileges to data access user "fhirserver". And then add "default" and "tenant1" tenants for fhirdb database, and add "tenant1" tenant for the other 3 databases.  

Configure to use db2 for the default tenant in fhir-server-config.json, e.g:

```json
            "datasources": {
                "default": {
                    "type": "db2",
                    "tenantKey": "<the-base64-tenant-key>",
                    "connectionProperties": {
                        "serverName": "localhost",
                        "portNumber": 50000,
                        "databaseName": "fhirdb",
                        "user": "fhirserver",
                        "password": "change-password",
                        "currentSchema": "FHIRDATA",
                        "driverType": 4
                    }
                }
            },
```

Configure to use db2 for the "tenant1" tenant in fhir-server-config.json, e.g:

```json
            "datasources": {
                "default": {
                    "type": "db2",
                    "tenantKey": "<the-base64-tenant-key>",
                    "connectionProperties": {
                        "serverName": "localhost",
                        "portNumber": 50000,
                        "databaseName": "fhirdb",
                        "user": "fhirserver",
                        "password": "change-password",
                        "currentSchema": "FHIRDATA",
                        "driverType": 4
                    }
                },
                "profile": {
                    "type": "db2",
                    "tenantKey": "<the-base64-tenant-key>",
                    "connectionProperties": {
                        "serverName": "localhost",
                        "portNumber": 50000,
                        "databaseName": "profile",
                        "user": "fhirserver",
                        "password": "change-password",
                        "currentSchema": "FHIRDATA",
                        "driverType": 4
                    }
                },
                "reference": {
                    "type": "db2",
                    "tenantKey": "<the-base64-tenant-key>",
                    "connectionProperties": {
                        "serverName": "localhost",
                        "portNumber": 50000,
                        "databaseName": "ref",
                        "user": "fhirserver",
                        "password": "change-password",
                        "currentSchema": "FHIRDATA",
                        "driverType": 4
                    }
                },
                "study1": {
                    "type": "db2",
                    "tenantKey": "<the-base64-tenant-key>",
                    "connectionProperties": {
                        "serverName": "localhost",
                        "portNumber": 50000,
                        "databaseName": "study",
                        "user": "fhirserver",
                        "password": "change-password",
                        "currentSchema": "FHIRDATA",
                        "driverType": 4
                    }
                }
            }
```

Restart fhir server, and then run testng integration tests under the fhir-server-test project.


FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
