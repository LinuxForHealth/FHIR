---
layout: post
title:  Setup for IBM Db2 on Cloud
description: Setup for IBM Db2 on Cloud
date:   2020-03-27 09:59:05 -0400
permalink: /db2oncloudsetup/
---

## Using Db2 for persistence

This document guides a developer or administrator through the steps necessary to setup and configure [IBM Db2 on IBM Cloud](https://cloud.ibm.com/catalog/services/db2) for the IBM FHIR Server.

### Create a Db2 instance

1. Log in to your IBM Cloud account [Link](https://cloud.ibm.com/).

1. Click `Create resource`.

1. Choose `Db2 (transactional)`.

1. Select the Pricing Plan: 
    - The IBM FHIR Server recommends the Flex plan for a production workload.
    - The IBM FHIR Server recommends the Flex plan for development, however, the Lite plan is possible for the development and evaluation. 
      - **Note** The Lite plan has a limit of 5 concurrent connections, and the IBM FHIR Server `fhirProxyDataSource` needs to be updated to avoid failures. One should update the server.xml connectionManager with maxPoolSize - `<connectionManager maxPoolSize="5"/>.
      
1. Create `Create`

Your instance is now creating or created.

### Scale the Instance

If you chose the Flex plan, you may want to scale the instance after it has been created (e.g. 4 cores, 16GB). The instance can be scaled more than once, so it doesn't matter if you don't get the sizing right first time. Please note, the scaling of the instance requires a service restart.

### Create the Administrator Credential

The administrator will be BLUADMIN but you need to create a credential. 

1. Open the `Service credentials` panel for the Db2 instance resource you just created. If you don't yet have any service credentials in the table at the bottom of the page, create a new one by clicking the `New credential (+)` button. Any name will do.

To access the credential, select View Credentials for the entry you just created. The result will be a block of JSON full of secrets (blanked out here):

``` json
{
  "hostname": "dashdb-txn-flex-.***********.services.dal.bluemix.net",
  "password": "bluadmin-password-very-secret",
  "https_url": "https://dashdb-txn-flex-************.services.dal.bluemix.net:8443",
  "port": 50000,
  "ssldsn": "DATABASE=BLUDB;HOSTNAME=dashdb-txn-flex-************.services.dal.bluemix.net;PORT=50001;PROTOCOL=TCPIP;UID=bluadmin;PWD=bluadmin-password-very-secret;Security=SSL;",
  "host": "dashdb-txn-flex-************.services.dal.bluemix.net",
  "jdbcurl": "jdbc:db2://dashdb-txn-flex-************.services.dal.bluemix.net:50000/BLUDB",
  "uri": "db2://bluadmin:bluadmin-password-very-secret@dashdb-txn-flex-************.services.dal.bluemix.net:50000/BLUDB",
  "db": "BLUDB",
  "dsn": "DATABASE=BLUDB;HOSTNAME=dashdb-txn-flex-************.services.dal.bluemix.net;PORT=50000;PROTOCOL=TCPIP;UID=bluadmin;PWD=bluadmin-password-very-secret;",
  "username": "bluadmin",
  "ssljdbcurl": "jdbc:db2://dashdb-txn-flex-************.services.dal.bluemix.net:50001/BLUDB:sslConnection=true;"
}
```

These properties are needed to deploy and manage the IBM FHIR Server schema.


### FHIRSERVER User and API Key

The **BLUADMIN** user is used to deploy the initial schema objects (tables, indexes, stored procedures etc). Following the least-privilege principle, the FHIR server itself does not use **BLUADMIN**. The FHIR server uses an API Key associated with an IAM Service Id. This Service Id is mapped to a Db2 user which is granted explicit privileges to the tables and stored procedures.

```
    API KEY -------> Service Id -------> DB2 User ------> SELECT/UPDATE/EXECUTE etc Privileges
 (FHIR Config)          (IAM)              (DB2)                          (DB2)
```

On the IBM Cloud console, select Manage / Access (IAM).

Select the [Service IDs panel](https://cloud.ibm.com/iam/serviceids).

Wait for the table to populate. If you do not yet have an appropriate service id, click `Create (+)`

Specify a meaningful name and description. After creating the Service ID, record the ID value which will start with `ServiceId-...`.

Select the API keys tab on the Service ID management page. Note that this is not the `IBM Cloud API keys` on the left.

Select `Create (+)` to add a new API key. Copy or download the key. If you mess up and don't retain a copy, don't panic - you can simply delete the API key and create a new one. API keys are designed to support rolling so you can add as many as you need and delete others if you need to revoke access for any reason.

The API key is used in the database configuration section of the `fhir-server-config.json` file.

Before the API key can be used, we need to create a Db2 user and associate it with the new ServiceId.

Navigate to the IBM Cloud Db2 resource page for your instance and select Manage. Click Open Console to access the IBM Db2 on Cloud console.

Select SETTINGS > Manage Users.

Click Add. This opens a panel on the right-hand side.

Select Add IBMid User at the top.

- User ID: FHIRSERVER

- IBMid: paste the ServiceId-... value from the Service Id created previously.

The page forces the value to lower-case, so `ServiceId` becomes `serviceid`. Don't be alarmed, it still works.

Do NOT select Administrator. One should follow the least-privelege principal for the FHIRSERVER user.

Click Create.

You should now be able to connect to the database as the FHIRSERVER user using only the API key created above.

### Testing the Connection

The Db2 driver jar contains a main which can be executed to test the connection - very convenient for checking the API-key/Service-Id/Db2-User-Id configuration is correct.

``` bash
java -cp /path/to/db2jcc4.jar com.ibm.db2.jcc.DB2Jcc  -url "jdbc:db2://<DB2-HOSTNAME>:50001/BLUDB:apiKey=<API-KEY>;securityMechanism=15;sslConnection=true;sslTrustStoreLocation=/path/to/truststore.jks;sslTrustStorePassword=<TRUSTSTORE-PASSWORD>;"
```

- `<DB-HOSTNAME>`: the hostname of your Db2 service from the Service Credentials page
- `<API-KEY>`: the API key value created in the previous section
- `<TRUSTSTORE-PASSWORD>`: the password for your truststore

Notes:
  1. Don't forget the trailing `;` in the URL. Some of the documented examples don't include it, but it is required in order for the connection to work, although this may be fixed in a future driver release. This only affects this test URL, not the actual FHIR server configuration.
  2. When using an API Key, no username needs to be provided. This is because the API Key maps to a ServiceId, and that ServiceId is mapped to the Db2 user.

### Configuring a Liberty Datasource with API Key

The IBM FHIR Server Import/Export feature utilizes Java Batch (JSR-352) provided by the batch-1.0 feature in Liberty Profile. The persistence layer can be configured to use Db2 as follows:

Create a Db2 user (e.g. FHIRBATCH) and associate it with a ServiceId (no need to create an Administration user, a simple user has sufficient privileges). Using a valid API-KEY for the given ServiceId, configure a new datasource and the Java Batch persistence layer as follows:

``` xml
    <dataSource id="fhirbatchDS" jndiName="jdbc/fhirbatchDB">
        <jdbcDriver libraryRef="fhirSharedLib"/>
        <properties.db2.jcc
            serverName="dashdb-txn-flex-************.services.dal.bluemix.net"
            portNumber="50001"
            apiKey="<API-KEY>"
            securityMechanism="15"
            pluginName="IBMIAMauth"
            databaseName="BLUDB"
            currentSchema="JBATCH"
            driverType="4" sslConnection="true" sslTrustStoreLocation="resources/security/dbTruststore.jks" sslTrustStorePassword="<TRUSTSTORE-PASSWORD>"/>
    </dataSource>

    <batchPersistence jobStoreRef="BatchDatabaseStore" />
    <databaseStore id="BatchDatabaseStore" dataSourceRef="fhirbatchDS" schema="JBATCH" tablePrefix="" />
```

Note, the Java Batch is configured in batchDs.xml  and included from the default server.xml that gets installed to the `{wlp}/usr/server/fhir-server`.

### Configuring FHIR Datasource

The FHIR server uses a proxy datasource mechanism, allowing new datasources to be added at runtime without requiring a (Liberty Profile) server restart. To configure a FHIR tenant datasource using an API-KEY, use the following template:

``` json
        "persistence": {
            "datasources": {
                "default": {
                    "tenantKey": "",
                    "type": "db2",
                    "connectionProperties": {
                        "serverName": "dashdb-txn-flex-************.services.dal.bluemix.net",
                        "portNumber": 50001,
                        "databaseName": "BLUDB",
                        "apiKey": "<API-KEY>",
                        "securityMechanism": 15,
                        "pluginName": "IBMIAMauth",
                        "currentSchema": "FHIRDATA",
                        "driverType": 4,
                        "sslConnection": true,
                        "sslTrustStoreLocation": "resources/security/dbTruststore.jks",
                        "sslTrustStorePassword": "change-password"
                    }
                }
            }
        }
```

#### Mapping from IBM Db2 on Cloud Endpoint Credentials

This section explains how to populate the FHIR datasource from IBM Db2 on Cloud configuration details from an example configuration: 

Use the following table to populate your datasource. 

| IBM FHIR Server Configuration  | Description |
|----|----|
| serverName | from the credential object select `hostname` |
| portNumber | from the credential object select  `port` |
| databaseName | from the credential object select  `db`, generally always `BLUDB` |
| apiKey | from the created user in the assigned to the `fhiruser` group. Reference Section **FHIRSERVER User and API Key** |
| securityMechanism | `15` generally set to 15 to trigger the `apiKey` use with IBM Cloud |
| pluginName | `IBMIAMauth` fixed for use with the IBM Cloud|
| currentSchema | the schema name of your deployed tenant Schema, for instance `FHIRDATA` | 
| driverType | `4`, always JDBC Type-4|
| sslConnection | `true`, if you are using IBM Cloud | 
| sslTrustStoreLocation | Local server path to the truststore, `resources/security/dbTruststore.jks` |
| sslTrustStorePassword | The password to the truststore |

* Reference the section **Create the Administrator Credential** to see an example. 

Note, no username properties are given, because the authentication module only requires the API-KEY.

### SSL Certificate

The Db2 certificate should be added to the Liberty Profile truststore. *Be sure to use the same Java runtime that Liberty Profile uses when manipulating any keystores.*


### Encrypt Secrets

All passwords including apiKey values should be encrypted using the Liberty Profile securityUtility.

# References
- [Db2 on Cloud: Database details and connection credentials](https://cloud.ibm.com/docs/services/Db2onCloud?topic=Db2onCloud-db_details_cxn_creds)
