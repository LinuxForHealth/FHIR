<!--
  
---

Copyright:

  years: 2019
lastupdated: "2019-09-10"

---

-->

## Using DB2 For Persistence

### Create a DB2 Instance

Log in to your IBM Cloud account https://cloud.ibm.com/.

Click `Create resource`.

Choose DB2 (transactional, formerly DashDB).

Select the instance size. Flex is the best choice for a basic production workload.

You can use the Lite plan for development or evaluation workloads. This plan has a limit of 5 concurrent connections, so the FHIR server connection-pool will need to be sized accordingly to avoid failures.

### Scale the Instance

If you chose Flex, you may want to scale the instance after it has been created (e.g. 4 cores, 16GB). The instance can be scaled more than once, so it doesn't matter if you don't get the sizing right first time. Scaling the instance requires a reboot.

### Create the Administrator Credential

The administrator will be BLUADMIN but you need to create a credential. Open the `Service credentials` panel for the DB2 instance resource you just created. If you don't yet have any service credentials in the table at the bottom of the page, create a new one by clicking the `New credential (+)` button. Any name will do.

To access the credential, select View Credentials for the entry you just created. The result will be a block of JSON full of secrets (blanked out here):

```
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

These properties will be needed to deploy and manage the FHIR schema.


### FHIRSERVER User and API Key

The BLUADMIN user is used to deploy the initial schema objects (tables, indexes, stored procedures etc). Following the least-privilege principle, the FHIR server itself does not use BLUADMIN. The FHIR server uses an API Key associated with an IAM Service Id. This Service Id is mapped to a DB2 user which is granted explicit privileges to the tables and stored procedures.

```
    API KEY -------> Service Id -------> DB2 User ------> SELECT/UPDATE/EXECUTE etc Privileges
 (FHIR Config)          (IAM)              (DB2)                          (DB2)
```

On the IBM Cloud console, select Manage / Access (IAM).

Select the Service IDs panel: https://cloud.ibm.com/iam/serviceids.

Wait for the table to populate. If you do not yet have an appropriate service id, click `Create (+)`

Specify a meaningful name and description. After creating the Service ID, record the ID value which will start with `ServiceId-...`.

Select the API keys tab on the Service ID management page. Note that this is not the `IBM Cloud API keys` on the left.

Select Create (+) to add a new API key. Copy or download the key. If you mess up and don't retain a copy, don't panic - you can simply delete the API key and create a new one. API keys are designed to support rolling so you can add as many as you need and delete others if you need to revoke access for any reason.

The API key will be used in the database configuration section of the fhir-server-config.json file.

Before the API key can be used, we need to create a DB2 user and associate it with the new ServiceId.

Navigate to the IBM Cloud DB2 resource page for your instance and select Manage. Click Open Console to access the IBM Db2 on Cloud console.

Select SETTINGS > Manage Users.

Click Add. This opens a panel on the right-hand side.

Select Add IBMid User at the top.

User ID: FHIRSERVER
IBMid: paste the ServiceId-... value from the Service Id created previously.

The page forces the value to lower-case, so `ServiceId` becomes `serviceid`. Don't be alarmed, it still works.

Do NOT select Administrator. We want the FHIRSERVER user to be a plain user with minimal privileges.

Click Create.

You should now be able to connect to the database as the FHIRSERVER user using only the API key created above.

### Testing the Connection

The DB2 driver jar contains a main which can be executed to test the connection - very convenient for checking the API-key/Service-Id/DB2-User-Id configuration is correct.

```
java -cp /path/to/db2jcc4.jar com.ibm.db2.jcc.DB2Jcc  -url "jdbc:db2://<DB2-HOSTNAME>:50001/BLUDB:apiKey=<API-KEY>;securityMechanism=15;sslConnection=true;sslTrustStoreLocation=/path/to/truststore.jks;sslTrustStorePassword=<TRUSTSTORE-PASSWORD>;"


        <DB-HOSTNAME>: the hostname of your DB2 service from the Service Credentials page
            <API-KEY>: the API key value created in the previous section
<TRUSTSTORE-PASSWORD>: the password for your truststore

```

Notes:
  1. Don't forget the trailing `;` in the URL. Some of the documented examples don't include it, but it is required in order for the connection to work, although this may be fixed in a future driver release. This only affects this test URL, not the actual FHIR server configuration.
  2. When using an API Key, no username needs to be provided. This is because the API Key maps to a ServiceId, and that ServiceId is mapped to the DB2 user.

### Configuring a Liberty Datasource with API Key

The FHIR export feature utilizes Java Batch (JSR-352) provided by the batch-1.0 feature in Liberty Profile. The JPA persistence layer can be configured to use DB2 as follows:

Create a DB2 user (e.g. FHIRBATCH) and associate it with a ServiceId (no need to create an Administration user, a simple user has sufficient privileges). Using a valid API-KEY for the given ServiceId, configure a new datasource and the Java Batch persistence layer as follows:

```

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



### Configuring FHIR Datasource

The FHIR server uses a proxy datasource mechanism, allowing new datasources to be added at runtime without requiring a (Liberty Profile) server restart. To configure a FHIR tenant datasource using an API-KEY, use the following template:

```
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

Note that no username properties are given, because the authentication module only requires the API-KEY.



### SSL Certificate

The DB2 certificate should be added to the Liberty Profile truststore. *Be sure to use the same Java runtime that Liberty Profile uses when manipulating any keystores.*


### Encrypt Secrets

All passwords including apiKey values should be encrypted using the Liberty Profile securityUtility.



