## Synthetic Data Loader
Scans cloud object storage buckets and uploads data using the FHIR REST API

### Background

Synthea is a project for generating "synthetic" patient/population data for healthcare applications.
It generates realistic data based on census statistics and a lot of configuration.
It supports generating data in FHIR R4 

This "fhir-bucket" project will help you upload Synthea-generated data to a FHIR R4 server.

To facilitate high-volume load scenarios, multiple instances of the application can be run and their work coordinated so that each file is loaded exactly once.

The loader records the identities of the created resources. These identities can be used in other test and load generator applications to access the data.

### Steps

1. Follow the steps at https://github.com/synthetichealth/synthea to clone and install Synthea
2. Configure Synthea to generate FHIR R4 resources
3. Generate a bunch of patients
4. Clone this repo and set it up with Maven/Eclipse
5. Configure the truststore with root certs required to trust connections to your FHIR server, cloud object store and tracking database
5. Tweak fhir.properties to point at your target FHIR server
6. Tweak cos.properties to point at your COS bucket
7. Tweak db.properties to point at your tracking database
8. Execute Main.class as described in the Running section below


### Configuration

#### The cos.properties file

```
# the IBM COS API key or S3 access key.
cos.api.key=MCnKzrs_JoQ0DZGKihvTYA6osHqetN_7K5wb2-g8_X5x

# the IBM COS service instance id or S3 secret key.
cos.srvinstid=

# the IBM COS or S3 End point URL.
cos.endpoint.url=

# the IBM COS or S3 location.
cos.location=

# the IBM COS or S3 bucket name to import from.
cos.bucket.name=

# if use IBM credential(Y/N), default(Y).
cos.credential.ibm=Y

# COS network timeouts
cos.request.timeout=60000
cos.socket.timeout=60000

# The number of COS keys (items) to fetch per read
cos.max.keys=1000
```

#### The db2.properties file

```
db.host=<DB2-HOST-NAME>
db.port=<DB2-PORT>
db.database=<DB2-DATABASE>
user=<DB2-USER>
password=<DB2-PASSWORD>
sslConnection=true
sslTrustStoreLocation=/path/to/dbTruststore.p12
sslTrustStorePassword=<TRUSTSTORE-PASSWORD>
currentSchema=FHIRBUCKET
```

#### The derby.properties file

Db2 is the preferred database for hosting the fhir-bucket schema. Derby can, however, be used for development. The derby.properties file must be configured as follows:

```
db.database=derby/bucketDB
db.create=Y
```

The name of the Derby database can be anything (without spaces) but must be contained within a folder called "derby".

### Schema Deployment

As a one-time activity, create the schema objects using the following command:

```
#!/bin/bash

JAR="~/git/FHIR/fhir-bucket/target/fhir-bucket-4.4.0-SNAPSHOT-cli.jar"
CP="${JAR}:/path/to/db2/jcc-11.5.0.0.jar"

java -classpath "${CP}"          \
  com.ibm.fhir.bucket.app.Main   \
  --db-type db2                  \
  --db-properties db2.properties \
  --create-schema
```

Or alernatively, if using a local Derby instance:

```
#!/bin/bash

JAR="~/git/FHIR/fhir-bucket/target/fhir-bucket-4.4.0-SNAPSHOT-cli.jar"
CP="${JAR}:/path/to/db2/jcc-11.5.0.0.jar"

java -classpath "${CP}"            \
  com.ibm.fhir.bucket.app.Main     \
  --db-type derby                  \
  --db-properties derby.properties \
  --create-schema
```




### Running

The following script can be used to run the bucket loader from a local build:

```
#!/bin/bash

JAR="~/git/FHIR/fhir-bucket/target/fhir-bucket-4.4.0-SNAPSHOT-cli.jar"
CP="${JAR}:/path/to/db2/jcc-11.5.0.0.jar"

java -classpath "${CP}"             \
  com.ibm.fhir.bucket.app.Main      \
  --db-type db2                     \
  --db-properties db2.properties    \
  --cos-properties cos.properties   \
  --fhir-properties fhir.properties \
  --bucket fhir-performance         \
  --tenant-name performance         \
  --file-type JSON                  \
  --reader-pool-size 1              \
  --handler-pool-size 10
```

To run using Derby, change the relevant arguments to:

```
...
  --db-type derby                   \
  --db-properties derby.properties  \
...
```


