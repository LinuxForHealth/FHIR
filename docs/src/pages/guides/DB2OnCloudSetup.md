---
layout: post
title:  Setup for IBM Db2 on Cloud
description: Setup for IBM Db2 on Cloud
date:   2021-06-01
permalink: /FHIR/guides/DB2OnCloudSetup
---

## Using Db2 for persistence

This document guides a developer or administrator through the steps necessary to setup and configure [IBM Db2 on IBM Cloud](https://cloud.ibm.com/catalog/services/db2) as a persistence layer for the IBM FHIR Server.

### **Create a Db2 instance**

1. Log in to your IBM Cloud account [Link](https://cloud.ibm.com/).

1. Click `Create resource`.

1. Choose [Db2](https://cloud.ibm.com/catalog/services/db2).

1. Select the Pricing Plan - `Standard` or `Enterprise`
    - Note, the IBM FHIR Server does not support using the *Lite* plan.

1. Create `Create`

Your instance is now creating or created.

### **Scale the instance**

Depending on the plan you choose, you may want to scale the instance after it has been created (e.g. 4 cores, 16GB). The instance can be scaled more than once, so it doesn't matter if you don't get the sizing right first time.

Note:
1. The scaling of the instance requires a service restart.
2. The instance CPU/Memory are scalable up and down. The storage in only scaled up.

### **Create the administrator credential**

1. Navigate to the Db2 instance you created.

1. Click on the `Service credentials` panel.
    - If the list of service credentials is empty, create a new one.
        1. Click `New credential (+)`
        1. Enter the Name. Any name works for example, `ibm-fhir-server-db2`.
        1. Click Add

1. Access the credential, select View Credentials. The entry you just created looks like the following block of JSON(the secrets are blanked out here):

``` json
{
  "apikey": "******",
  "connection": {
    ...
    "db2": {
      "authentication": {
        "method": "direct",
        "password": "P1234",
        "username": "username1"
      },
      "certificate": {
        "certificate_base64": "secret=",
        "name": "9e529eec-97df-4574-8839-12345"
      },
      "composed": [
        "db2://username1:P1234@1-2-3-4.databases.appdomain.cloud:31366/bludb?authSource=admin&replicaSet=replset"
      ],
      "database": "bludb",
      "host_ros": [
        "1-2-3-4.databases.appdomain.cloud:32116"
      ],
      "hosts": [
        {
          "hostname": "1-2-3-4.databases.appdomain.cloud",
          "port": 31366
        }
      ],
      "jdbc_url": [
        "jdbc:db2://1-2-3-4.databases.appdomain.cloud:31366/bludb:user=<userid>;password=<your_password>;sslConnection=true;"
      ],
      "path": "/bludb",
      "query_options": {
        "authSource": "admin",
        "replicaSet": "replset"
      },
      "replica_set": "replset",
      "scheme": "db2",
      "type": "uri"
    }
  },
  "iam_apikey_description": "Auto-generated for key username1-fc89-48e1-a1ce-569ca6c6e3d5",
  "iam_apikey_name": "Service credentials-1",
  "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Manager",
  "iam_serviceid_crn": "secret",
  "instance_administration_api": {
    "deployment_id": "crn:v1:bluemix:public:dashdb-for-transactions:us-east:a/secret:",
    "instance_id": "crn:v1:bluemix:public:dashdb-for-transactions:us-east:a/secret::",
    "root": "https://api.us-east.db2.cloud.ibm.com/v4/ibm"
  }
}
```

4. Save these details for later, as these properties are needed to deploy and manage the IBM FHIR Server schema.

For improved security, the **admin** user should only be used to deploy the schema objects (tables, indexes, stored procedures etc) and administer the database, NOT for connecting from the IBM FHIR Server application.

### **Adding the FHIRSERVER user**

Following the least-privilege principle, the IBM FHIR server itself recommends running as db user, not db administrator.

#### Using IAM apiKey
The IBM FHIR Server uses an API Key associated with an [IAM Service Id](https://cloud.ibm.com/iam/serviceids). The IAM Service Id is mapped to a Db2 user which is granted explicit privileges to the tables and stored procedures.

The IBM FHIR Server uses the access flow:

1. Read API Key and the tenant key from the fhir-server-config.json

2. Connect to Db2 to access authenticated data using IAM

3. Confirm tenant-key to access authorized data

The steps to create the API key are:

1. On the IBM Cloud console, upper right corner, select `Manage` > `Access (IAM)`.

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

1. Click Administration > User Management

    - To confirm the IAM instance:
        1. Click Run Sql (click create new if not brought into SQL edit session)
        1. Enter the SQL
            ``` sql
            SELECT CASE WHEN VALUE = 'IBMIAMauth' THEN 1 ELSE 0 END AS IAM_ENABLED FROM SYSIBMADM.DBMCFG WHERE NAME = 'srvcon_gssplugin_list'
            ```
        1. You see `0` if not IAM enabled.

1. Click Add. A panel opens on the right-hand side.

1. Select Add IBMid User at the top.

    - User ID: FHIRSERVER

    - IBMid: paste the service id (not service id name) from the Service Id created previously. To get that navigate to the service id you created earlier and click on `Details` link (top right side next to Actions Menu). A panel opens on the right-hand side which contains ID. This ID needs to be used as IBMid value.

    - Note (1): The page forces the value to lower-case, so `ServiceId` becomes `serviceid`. Don't be alarmed, it still works.  Same for the User ID.

    - Note (2): Do NOT select Administrator. One should follow the least-privelege principal for the FHIRSERVER user.

1. Click `Create`.

You are now able to connect to the database as the FHIRSERVER user using only the API key created above.

#### Using Db2 Auth

1. On the Db2 Resource, click `Manage`

2. Click `Open Console`

3. Click Administration > User Management

4. Click Add User, and enter the user details with a sufficiently complex password. Set the `User Privilege` as `User`.

5. Save these details for the datasource.xml.

### **Testing the connection**

The [Db2 driver (click here to download)](https://repo1.maven.org/maven2/com/ibm/db2/jcc/11.5.6.0/jcc-11.5.6.0.jar) is able to execute a connectivity test to check the configuration of the combo of API-key/Service-Id/Db2-User-Id.

#### Test IAM Access
1. Copy the command to your code editor

    ``` bash
    java -cp /path/to/db2jcc4.jar com.ibm.db2.jcc.DB2Jcc  -url "jdbc:db2://<DB2-HOSTNAME>:<DB2-HOST-PORT>/BLUDB:apiKey=<API-KEY>;securityMechanism=15;sslConnection=true;"
    ```

    - Note: Don't forget the trailing `;` in the URL. Some of the documented examples don't include it, but it is required in order for the connection to work, although this may be fixed in a future driver release. This only affects this test URL, not the actual FHIR server configuration.

1. Replace the following values with your service details:
    - `/path/to/db2jcc4.jar` : replace with the path to your driver jar.
    - `<DB-HOSTNAME>`: the hostname of your Db2 service from the Service Credentials page
    - `<DB-HOST-PORT>`: the port of your Db2 service from the Service Credentials page
    - `<API-KEY>`: the API key value created in the previous section

    - Note: When using an API Key, no username needs to be provided. This is because the API Key maps to a ServiceId, and that ServiceId is mapped to the Db2 user.

1. Run in your favorite terminal, and you should see no errors in the output. You should see output like:

    ```
    [jcc][10516][13709]Test Connection Successful.
    ```

#### Test Db2 Auth Access
1. Copy the command to your code editor

    ``` bash
    java -cp /path/to/db2jcc4.jar com.ibm.db2.jcc.DB2Jcc  -url "jdbc:db2://<DB2-HOSTNAME>:<DB2-HOST-PORT>/bludb:user=<userid>;password=<your_password>;sslConnection=true;"
    ```

    - Note: Don't forget the trailing `;` in the URL. Some of the documented examples don't include it, but it is required in order for the connection to work, although this may be fixed in a future driver release. This only affects this test URL, not the actual FHIR server configuration.

1. Replace the following values with your service details:
    - `/path/to/db2jcc4.jar` : replace with the path to your driver jar.
    - `<DB-HOSTNAME>`: the hostname of your Db2 service from the Service Credentials page
    - `<DB-HOST-PORT>`: the port of your Db2 service from the Service Credentials page
    - `<userid>`: The userid to acecss the db
    - `<your_password>`: The password to access the db with

1. Run in your favorite terminal, and you should see no errors in the output. You should see output like:

    ``` bash
    [jcc][10516][13709]Test Connection Successful.
    ```

### **Deploy the IBM FHIR Server schema**

Now that you've created the database and credentials, use the `fhir-persistence-schema` utility to deploy the IBM FHIR Server schema:

1. download the `fhir-persistence-schema` cli jar from the corresponding project release: https://github.com/LinuxForHealth/FHIR/releases

2. create a properties file named db2.properties with the Db2 Admin connection info from IBM Cloud; for example:

    ``` sh
    db.host=<DB-HOSTNAME>
    db.port=<DB-HOST-PORT>
    db.database=bludb
    user=<USERID>
    password=<PASSWORD>
    sslConnection=true
    ```

3. execute the following commands:

    ``` sh
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --schema-name FHIRDATA --create-schemas
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --schema-name FHIRDATA --update-schema
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --schema-name FHIRDATA --grant-to FHIRSERVER
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --schema-name FHIRDATA --allocate-tenant default
    ```

4. save the tenantKey from the `allocate-tenant` step above; this is needed to configure the IBM FHIR Server datasource in the next step

For more information on using the fhir-persistence-schema cli jar, see https://github.com/LinuxForHealth/FHIR/tree/main/fhir-persistence-schema/docs/SchemaToolUsageGuide.md.

### **Configuring an IBM FHIR Server datasource**

The IBM FHIR Server uses the native Open Liberty datasources. To configure a FHIR tenant datasource for a tenantKey, use the following template in the fhir-server-config.json:

``` json
        "persistence": {
            "datasources": {
                "default": {
                    "tenantKey": "myTenantKey",
                    "currentSchema": "${DB_SCHEMA}",
                    "hints" : {
                        "search.reopt": "ONCE"
                    },
                    "type": "db2",
                }
            }
        }
```

Since release 4.3.2 you can use the `search.reopt` query optimizer hint (shown above) to improve the performance of certain search queries involving multiple search parameters. This optimization is currently only available for Db2. Valid values are "ALWAYS" and "ONCE". See Db2 documentation for `REOPT` for more details.

To configure the datasource.xml for db2 create a datasource.xml for the configDropins folder.  Note: CurrentSchema is case sensative to what you used in the fhir-persistance-schema tool used above.

#### For IAM User

Create a file as the following

``` xml
<server>
    <!-- ============================================================== -->
    <!-- TENANT: default; DSID: default; TYPE: read-write               -->
    <!-- ============================================================== -->
    <dataSource id="fhirDefaultDefault" jndiName="jdbc/fhir_default_default" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true" validationTimeout="30s" isolationLevel="TRANSACTION_READ_COMMITTED">
        <jdbcDriver javax.sql.XADataSource="com.ibm.db2.jcc.DB2XADataSource" libraryRef="sharedLibDb2"/>
        <properties.db2.jcc
            apiKey="${DB_APIKEY}"
            serverName="${DB_HOSTNAME}"
            currentSchema="${DB_SCHEMA}"
            databaseName="${DB_NAME}"
            driverType="4"
            pluginName="IBMIAMauth"
            portNumber="${DB_PORT}"
            securityMechanism="15"
            sslConnection="true" />
        <connectionManager maxPoolSize="200" minPoolSize="20" connectionTimeout="60s" maxIdleTime="2m" numConnectionsPerThreadLocal="0"/>
    </dataSource>
</server>
```

#### For Db2 Auth user

Create a file as the following:

``` xml
<server>
    <!-- ============================================================== -->
    <!-- TENANT: default; DSID: default; TYPE: read-write               -->
    <!-- ============================================================== -->
    <dataSource id="fhirDefaultDefault" jndiName="jdbc/fhir_default_default" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true" validationTimeout="30s" isolationLevel="TRANSACTION_READ_COMMITTED">
        <jdbcDriver javax.sql.XADataSource="com.ibm.db2.jcc.DB2XADataSource" libraryRef="sharedLibDb2"/>
        <properties.db2.jcc
            serverName="${DB_HOSTNAME}"
            currentSchema="${DB_SCHEMA}"
            databaseName="${DB_NAME}"
            driverType="4"
            portNumber="${DB_PORT}"
            sslConnection="true"
            user="fhirserver"
            password="mypassword"
         />
        <connectionManager maxPoolSize="200" minPoolSize="20" connectionTimeout="60s" maxIdleTime="2m" numConnectionsPerThreadLocal="0"/>
    </dataSource>
</server>
```

Each tenant datastore must have a corresponding dataSource definition and these dataSources must either follow the default jndiName pattern (`jndi/fhir_[tenantid]_[dsid]`) or the name must be explicitly configured in the corresponding section of `fhir-server-config.json`.


#### Mapping from IBM Db2 on Cloud endpoint credentials

This section explains how to populate the FHIR datasource from IBM Db2 on Cloud configuration details from an example configuration:

Use the following table to populate your datasource.

| IBM FHIR Server Configuration  | Description |
|----|----|
| serverName | from the credential object select `hostname` |
| portNumber | from the credential object select  `port` |
| databaseName | from the credential object select  `db`, generally always `BLUDB` |
| apiKey | from the created user in the assigned to the `fhiruser` group. Reference Section **FHIRSERVER User and API Key** |
| securityMechanism | If using IAM, set to `15` to trigger the use of IAM-based `apiKey` authentication|
| pluginName | If using IAM, set to `IBMIAMauth`|
| currentSchema | the schema name of your deployed tenant Schema, for instance `FHIRDATA` |
| user| the userid |
| password | the password for the user|
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

1. Create the datasource
    1. Db2 with IAM https://github.com/LinuxForHealth/FHIR/blob/main/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/db2-cloud/bulkdata.xml
    1. Db2 with Db2Auth https://github.com/LinuxForHealth/FHIR/blob/main/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/db2/bulkdata.xml

- Note: The Java Batch is configured in `bulkdata.xml` and included from the IBM FHIR Server's `server.xml` which is installed to `{wlp}/usr/server/defaultServer`. (fhir-server is installed locally)
- Note: While this feature is not required, it's best to configure this datasource while configuring the main datasource.

### **SSL Certificate**

The Db2 certificate should be added to the Liberty Profile truststore. *Be sure to use the same Java runtime that Liberty Profile uses when manipulating any keystores.*

### **Encrypt Secrets**

All passwords including apiKey values should be encrypted using the Liberty Profile securityUtility.

# Appendix: Db2 Lite Plan

The Lite plan is not supported for development and evaluation.
- The schema size is larger than the available space.
- The instance is not enabled with IAM, and you may use the `Service Credentials` that are created to connect to configure the datasource.

# **References**
- [Db2 on Cloud: Database details and connection credentials](https://cloud.ibm.com/docs/services/Db2onCloud?topic=Db2onCloud-db_details_cxn_creds)
