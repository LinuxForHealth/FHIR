---
layout: post
title:  Setup for IBM Db2 on Cloud
description: Setup for IBM Db2 on Cloud
date:   2020-04-03 09:59:05 -0400
permalink: /FHIR/guides/DB2OnCloudSetup
---

## Using Db2 for persistence

This document guides a developer or administrator through the steps necessary to setup and configure [IBM Db2 on IBM Cloud](https://cloud.ibm.com/catalog/services/db2) as a persistence layer for the IBM FHIR Server.

### **Create a Db2 instance**

1. Log in to your IBM Cloud account [Link](https://cloud.ibm.com/).

1. Click `Create resource`.

1. Choose [Db2](https://cloud.ibm.com/catalog/services/db2).

1. Select the Pricing Plan:
    - The IBM FHIR Server recommends the *Standard* plan for a production workload, development and experiments.

1. Create `Create`

Your instance is now creating or created.

### **Scale the instance**

If you chose the Standard plan, you may want to scale the instance after it has been created (e.g. 4 cores, 16GB). The instance can be scaled more than once, so it doesn't matter if you don't get the sizing right first time.

Note:
1. The scaling of the instance requires a service restart.
2. The instance CPU/Memory are scalable up and down. The storage in only scaled up.

### **Create the administrator credential**

The administrator is `BLUADMIN` but you need to create a credential.

1. Navigate to the Db2 instance you created.

1. Click on the `Service credentials` panel.
    - If the list of service credentials is empty, create a new one.
        1. Click `New credential (+)`
        1. Enter the Name. Any name works for example, `ibm-fhir-server-db2`.
        1. Click Add

1. Access the credential, select View Credentials. The entry you just created looks like the following block of JSON(the secrets are blanked out here):

    ``` json
    {
        "hostname": "dashdb-txn-***-***********.services.dal.bluemix.net",
        "password": "bluadmin-password-very-secret",
        "https_url": "https://dashdb-txn-***-************.services.dal.bluemix.net:8443",
        "port": 50000,
        "ssldsn": "DATABASE=BLUDB;HOSTNAME=dashdb-txn-***-************.services.dal.bluemix.net;PORT=50001;PROTOCOL=TCPIP;UID=bluadmin;PWD=bluadmin-password-very-secret;Security=SSL;",
        "host": "dashdb-txn-***-************.services.dal.bluemix.net",
        "jdbcurl": "jdbc:db2://dashdb-txn-***-************.services.dal.bluemix.net:50000/BLUDB",
        "uri": "db2://bluadmin:bluadmin-password-very-secret@dashdb-txn-***-************.services.dal.bluemix.net:50000/BLUDB",
        "db": "BLUDB",
        "dsn": "DATABASE=BLUDB;HOSTNAME=dashdb-txn-***-************.services.dal.bluemix.net;PORT=50000;PROTOCOL=TCPIP;UID=bluadmin;PWD=bluadmin-password-very-secret;",
        "username": "bluadmin",
        "ssljdbcurl": "jdbc:db2://dashdb-txn-***-************.services.dal.bluemix.net:50001/BLUDB:sslConnection=true;"
    }
    ```

4. Save these details for later, as these properties are needed to deploy and manage the IBM FHIR Server schema.

For improved security, the **BLUADMIN** user should only be used to deploy the schema objects (tables, indexes, stored procedures etc) and administer the database, NOT for connecting from the fhir-server application.

### **Add the FHIRSERVER user and API key**

Following the least-privilege principle, the IBM FHIR server itself does not use **BLUADMIN**. The IBM FHIR server uses an API Key associated with an [IAM Service Id](https://cloud.ibm.com/iam/serviceids). The IAM Service Id is mapped to a Db2 user which is granted explicit privileges to the tables and stored procedures.

The IBM FHIR Server uses the access flow:

1. Read API Key and the tenant key from the fhir-server-config.json
2. Connect to Db2 to access authenticated data using IAM
3. Confirm tenant-key to access authorized data

The steps to create the API key are:

1. On the IBM Cloud console, select `Manage` > `Access (IAM)`.

1. Select the [Service IDs panel](https://cloud.ibm.com/iam/serviceids).

1. Click `Create (+)`

    1. Enter a meaningful name, such as `fhir-service-id`.
    1. Enter a description, such as `for instance db2-ho`.
    1. Click the `Create`.

    - Note (1): If you have already created the entry, click the entry.
    - Note (2): You may have to wait for the table to populate.

1. Select the `API keys` tab.
    - Note: This tab is not the same `IBM Cloud API keys` on the left.

1. To create an API Key, select `Create (+)`.

    1. Enter a name for the api key, for example, `fhir-server-api`.
    1. Click `Create`.
    1. Copy or download the key.

    - Note: if you don't retain a copy, don't panic - you can simply delete the API key and create a new one. API keys are designed to support additions and deletions, so you add as many as you need and delete others for any reason.

This API key is used in the database configuration section of the `fhir-server-config.json` file.

Before the API key can be used, you need to create a Db2 user and associate it with the new ServiceId.

1. Navigate to the [Resources](https://cloud.ibm.com/resources) page

1. Find and click on your IBM Db2 on Cloud instance.

1. Click on Manage.

1. Click on Open Console.

1. Click `SETTINGS` > `Manage Users`.

    - Note: If you do not see `Manage Users`, you are probably using a [non-IAM instance](https://www.ibm.com/support/knowledgecenter/SS6NHC/com.ibm.swg.im.dashdb.security.doc/doc/iam.html). To confirm:
        1. Click Run Sql
        1. Enter the SQL
            ``` sql
            SELECT CASE WHEN VALUE = 'IBMIAMauth' THEN 1 ELSE 0 END AS IAM_ENABLED FROM SYSIBMADM.DBMCFG WHERE NAME = 'srvcon_gssplugin_list'
            ```
        1. You see `0` if not IAM enabled.

1. Click Add. A panel opens on the right-hand side.

1. Select Add IBMid User at the top.

    - User ID: FHIRSERVER

    - IBMid: paste the service id `fhir-server-id` from the Service Id created previously.

    - Note (1): The page forces the value to lower-case, so `ServiceId` becomes `serviceid`. Don't be alarmed, it still works.

    - Note (2): Do NOT select Administrator. One should follow the least-privelege principal for the FHIRSERVER user.

1. Click `Create`.

You are now able to connect to the database as the FHIRSERVER user using only the API key created above.

### **Testing the connection**

The [Db2 driver](https://repo1.maven.org/maven2/com/ibm/db2/jcc/11.5.0.0/jcc-11.5.0.0.jar) is able to execute a connectivity test to check the configuration of the combo of API-key/Service-Id/Db2-User-Id.

1. Copy the command to your code editor

    ``` bash
    java -cp /path/to/db2jcc4.jar com.ibm.db2.jcc.DB2Jcc  -url "jdbc:db2://<DB2-HOSTNAME>:50001/BLUDB:apiKey=<API-KEY>;securityMechanism=15;sslConnection=true;sslTrustStoreLocation=/path/to/truststore.jks;sslTrustStorePassword=<TRUSTSTORE-PASSWORD>;"
    ```

    - Note: Don't forget the trailing `;` in the URL. Some of the documented examples don't include it, but it is required in order for the connection to work, although this may be fixed in a future driver release. This only affects this test URL, not the actual FHIR server configuration.

1. Replace the following values with your service details:
    - `/path/to/db2jcc4.jar` : replace with the path to your driver jar.
    - `<DB-HOSTNAME>`: the hostname of your Db2 service from the Service Credentials page
    - `<API-KEY>`: the API key value created in the previous section
    - `<TRUSTSTORE-PASSWORD>`: the password for your truststore

    - Note: When using an API Key, no username needs to be provided. This is because the API Key maps to a ServiceId, and that ServiceId is mapped to the Db2 user.

1. Run in your favorite terminal, and you should see no errors in the output. You should see output like:

```
[jcc][10516][13709]Test Connection Successful.
```

### **Deploy the IBM FHIR Server schema**

Now that you've created the database and credentials, use the `fhir-persistence-schema` utility to deploy the IBM FHIR Server schema:

1. download the `fhir-persistence-schema` cli jar from the corresponding project release: https://github.com/IBM/FHIR/releases

2. create a properties file named db2.properties with the db2 connection info from IBM Cloud; for example:
    ```
    db.host=dashdb-txn-***-***********.services.dal.bluemix.net
    db.port=50001
    db.database=BLUDB
    user=bluadmin
    password=bluadmin-password-very-secret
    sslConnection=true
    ```

3. execute the following commands:
    ```sh
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --schema-name FHIRDATA --create-schemas
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --schema-name FHIRDATA --update-schema --pool-size 2
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --schema-name FHIRDATA --grant-to FHIRSERVER --pool-size 2
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --schema-name FHIRDATA --allocate-tenant default --pool-size 2
    ```

4. note the tenantKey from the allocate-tenant step above; this will be needed to configure the IBM FHIR Server datasource in the next step

For more information on using the fhir-persistence-schema cli jar, see https://github.com/IBM/FHIR/tree/master/fhir-persistence-schema/README.md.

### **Configuring an IBM FHIR Server datasource**

The IBM FHIR Server uses a proxy datasource mechanism, allowing new datasources to be added at runtime without requiring a (Liberty Profile) server restart. To configure a FHIR tenant datasource using an API-KEY, use the following template:

``` json
        "persistence": {
            "datasources": {
                "default": {
                    "tenantKey": "",
                    "hints" : {
                        "search.reopt": "ONCE"
                    },
                    "type": "db2",
                    "connectionProperties": {
                        "serverName": "dashdb-txn-***-************.services.dal.bluemix.net",
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

The persistence configuration is stored in the `fhir-server-config.json` in the tenant and default configuration folders.

Since release 4.3.2 you can use the `search.reopt` query optimizer hint (shown above) to improve the performance of certain search queries involving multiple search parameters. This optimization is currently only available for Db2. Valid values are "ALWAYS" and "ONCE". See Db2 documentation for `REOPT` for more details.

#### Mapping from IBM Db2 on Cloud endpoint credentials

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

### **Configuring a BulkData's Liberty Datasource with API key**

The IBM FHIR Server Bulk Data modules utilize Java Batch (JSR-352) from the Liberty Profile feature - `batch-1.0`. The batch feature is configured to use Db2 as follows:

1. Create a Db2 user (e.g. FHIRBATCH)

1. Associate it with a ServiceId (no need to create an Administration user, a simple user has sufficient privileges) using the same procedure you followed for the fhir-server ServiceId user.

1. Using a valid API-KEY for the given ServiceId, configure a new datasource and the Java Batch persistence layer as follows:

``` xml
    <dataSource id="fhirbatchDS" jndiName="jdbc/fhirbatchDB">
        <jdbcDriver libraryRef="fhirSharedLib"/>
        <properties.db2.jcc
            serverName="dashdb-txn-***-************.services.dal.bluemix.net"
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

- Note: The Java Batch is configured in `batchDs.xml` and included from the IBM FHIR Server's `server.xml` which is installed to `{wlp}/usr/server/fhir-server`.
- Note: While this feature is not required, it's best to configure this datasource while configuring the main datasource.

### **SSL Certificate**

The Db2 certificate should be added to the Liberty Profile truststore. *Be sure to use the same Java runtime that Liberty Profile uses when manipulating any keystores.*

### **Encrypt Secrets**

All passwords including apiKey values should be encrypted using the Liberty Profile securityUtility.

# Appendix: Db2 Lite Plan

The Lite plan is not supported for development and evaluation.
- The schema size is larger than the available space.
- Concurrent Connections: The Lite plan has a limit of 5 concurrent connections, and the IBM FHIR Server `fhirProxyDataSource` needs to be updated to avoid failures. One should update the server.xml connectionManager with maxPoolSize - `<connectionManager maxPoolSize="5"/>`.  
- The instance is not enabled with IAM, and you may use the `Service Credentials` that are created to connect to configure the datasource.

# **References**
- [Db2 on Cloud: Database details and connection credentials](https://cloud.ibm.com/docs/services/Db2onCloud?topic=Db2onCloud-db_details_cxn_creds)
