# IBM FHIR Server - Schema Tool

The IBM FHIR Server Schema Tool is designed to create and update the IBM FHIR Server's schema idempotently.

The tool supports the following flows in one image: 

* Onboarding - create SQL schemas, populate the SQL objects, grant permissions to the database user. For db2, the tool sets up a single tenant and tenant key. 
* Offboarding - removes the schema, on Db2, if no other tenants, it deallocates the tenants
* Custom - executes a single fhir-persistence-schema cli action
* Debug - outputs debug information

# **Use**

The solution should set the variables as Environment Variables or mounted into the workarea folder as a volume which contains the persistence.json file.

## **Environment Variables**
The following environment variables:

| Name           | Purpose  |
|----------------|----------|
| ENV_SKIP       | Stop the container from making any changes, and passes through with a successful state change: `[empty\|true\|false]`|
| ENV_TOOL_INPUT | Encoded String, in most circumstances base64 encoded or well escaped text, of the json|
| ENV_TOOL_DEBUG | Flags the debug |

## Configuration Commandline
The following is read from the properties file:

| Name       | Purpose  |
|------------|----------|
| tool.behavior | Switches from the Onboarding BEHAVIOR to Offboarding BEHAVIOR flow: `[onboard\|offboard\|debug\|custom]`. |
| db.type | The database type - `[postgresql\|db2]` |
| db.host | The database server hostname|
| db.port | The database server port|
| db.database | The name of the database|
| user | A username with connect and admin permissions on the target database|
| password | The user password for connecting to the database|
| sslConnection | true or anything else, true triggers JDBC to use ssl, an example --prop sslConnection=true (db2) |
| ssl | true or anything else, true triggers JDBC to use ssl, an example --prop ssl=true (postgres) |
| tenant.key | the tenant key is a generated key, or used to set a specific value |
| tenant.name | the tenant name is typically default |
| schema.name.oauth | uses the default or custom |
| schema.name.fhir | defaults to fhirdata |
| schema.name.batch | uses the default or custom |
| grant.to | grants access to a specific user (which is going to run the application) |
| sslmode | For Postgres, you can set verify-full |
| sslrootcert | For Postgres, you must set as /opt/schematool/workarea/db.cert |
| db.cert | For Postgres, you must set as a base64 encoding of the certificate |

Further, any property supported by the [fhir-persistence-schema](https://github.com/IBM/FHIR/blob/master/fhir-persistence-schema/README.md) module is put into the file and mounted to the system.

## Configuration file - persistence.json
The configuration file is as follows in the examples configuration.

```
{
    "persistence": [
        {
            "db":  {
                "host": "172.17.0.3",
                "port": "50000",
                "database": "fhirdb",
                "user": "db2inst1",
                "password": "change-password",
                "type": "db2",
                "ssl": "false",
                "certificate_base64": "empty"
            },
            "tenant": {
                "name": "default3",
                "key": "custom-key-here"
            },
            "schema": {
                "fhir": "fhirdata",
                "batch": "",
                "oauth": ""
            },
            "grant":  "fhirserver",
            "behavior": "onboard"
        }
    ]
}
```

Note, tenant only applies to db2 implementations. The tenantKey is limited in size, and if not provided is generated. You will have to pick it out of the logs. To generate a tenantKey, you can execute: `openssl rand -base64 20`.

It's converted to base64 using `cat persistence.json | base64`. You can run locally using: 

*Mac*

```
docker run  --env ENV_TOOL_INPUT=$(cat persistence.json | base64) ibmcom/ibm-fhir-schematool:latest
```

*Linux*

```
docker run  --env ENV_TOOL_INPUT=$(cat persistence.json | base64 -w 0) ibmcom/ibm-fhir-schematool:latest
```

An example volume mount: 

```
    volumeMounts:
        - name: binding
          mountPath: "/opt/schematool/workarea"
          readOnly: true
    volumes:
      - name: binding
        secret:
          secretName: binding-persistence
          items:
          - key: binding
            path: persistence.json
```

# Running the Tool

## Running: Debug Behavior

The debug behavior outputs the details of the running image:

Using an encoded persistence.json

``` shell
docker run ibmcom/ibm-fhir-schematool:latest --tool.behavior=debug
```

Output
```
run.sh - [INFO]: 2020-11-09_21:39:27 - The files included with the tool are:
total 37M
drwxr-xr-x 1 root root 4.0K Nov  9 21:39 .
drwxr-xr-x 1 root root 4.0K Nov  9 20:00 ..
-rw-r--r-- 1 root root  37M Nov  6 20:33 fhir-persistence-schema-4.5.0-SNAPSHOT-cli.jar
-rwxr-xr-x 1 root root  632 Nov  9 21:39 jq
-rwxr-xr-x 1 root root  15K Nov  9 21:38 run.sh
drwxr-xr-x 2 root root 4.0K Nov  9 19:23 workarea
The OpenSSL version is:
OpenSSL 1.1.1g FIPS  21 Apr 2020
```

## Running: Offboard Behavior

Using an encoded persistence.json

*Mac*

```
docker run  --env ENV_TOOL_INPUT=`cat examples/db2/persistence-offboard-example.json |base64` \
    ibmcom/ibm-fhir-schematool:latest | tee out.log
```

*Linux*

```
docker run  --env ENV_TOOL_INPUT=`cat examples/db2/persistence-offboard-example.json |base64 -w 0` \
    ibmcom/ibm-fhir-schematool:latest | tee out.log
```

Using arguments on the commandline

``` shell
docker run ibmcom/ibm-fhir-schematool:latest --tool.behavior=offboard --db.host=172.17.0.3 \
    --db.port=50000 --user=db2inst1 --password=change-password --db.database=fhirdb \
    --sslConnection=false --db.type=db2 --schema.name.fhir=fhirdata --grant.to=fhirserver \
    --tenant.name=default2 2>&1 | tee out.log
```

## Running: Onboard Behavior

Using an encoded persistence.json

*Mac*

```
docker run  --env ENV_TOOL_INPUT=`cat examples/db2/persistence-onboard-example.json |base64` \
    ibmcom/ibm-fhir-schematool:latest | tee out.log
```

*Linux*

```
docker run  --env ENV_TOOL_INPUT=`cat examples/db2/persistence-onboard-example.json |base64 -w 0` \
    ibmcom/ibm-fhir-schematool:latest | tee out.log
```

Using arguments on the commandline

``` shell
docker run ibmcom/ibm-fhir-schematool:latest --tool.behavior=onboard --db.host=172.17.0.3 \
    --db.port=50000 --user=db2inst1 --password=change-password --db.database=fhirdb \
    --sslConnection=false --db.type=db2 --schema.name.fhir=fhirdata --grant.to=fhirserver \
    --tenant.name=default2 2>&1 | tee out.log
```

## Running: Custom Behavior

The custom behavior only supports commandline arguments:

``` shell
docker run ibmcom/ibm-fhir-schematool:latest --tool.behavior=onboard --list-tenants \
    --db.host=1000.com --db.password=change-password --db.user=db2inst1
```

************
# **License**

The IBM FHIR Server - Schema Tool is licensed under the Apache 2.0 license. Full license text is available at [LICENSE](https://github.com/IBM/FHIR/blob/master/LICENSE).

FHIR® is the registered trademark of HL7 and is used with the permission of HL7. Use of the FHIR trademark does not constitute endorsement of this product by HL7.
IBM and the IBM logo are trademarks of International Business Machines Corporation, registered in many jurisdictions worldwide. Other product and service names might be trademarks of IBM or other companies. A current list of IBM trademarks is available on [https://ibm.com/trademark](https://ibm.com/trademark).