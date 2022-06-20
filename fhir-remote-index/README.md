# Project: fhir-remote-index

Project fhir-remote-index is a stand-alone application to support asynchronous storage of FHIR search parameters published by the IBM FHIR Server. When configured to do so, the IBM FHIR Server extracts search parameters from the incoming resource and instead of storing the parameters as part of the create/update transaction, it packages the parameters into a message which is then published to Kafka. The fhir-remote-index application consumes the messages from Kafka and stores the value in the resource parameter tables using efficient batch-based inserts.

This pattern supports higher ingestion rates because:

1. Any locking related to inserting normalized common parameter values is decoupled from the locking on logical resources (`logical_resource_ident` table) used to ensure correct versioning of the resource. Create and Update interactions should never see any contention unless the same logical resource is updated in parallel requests;
2. If the same logical resource is updated in parallel, the contention will be reduced compared to synchronous parameter value storage because less work is performed while the lock is held, allowing the transaction to be completed sooner;
3. The remote index consumer can build larger batches, with transactions not tied to the semantics of the original FHIR interaction;
4. The remote index consumer uses batches for all the search parameter value inserts, making it more efficient than the current JDBC persistence layer implementation;
5. The remote index consumer handles normalized value cache-miss lookups using bulk queries. This reduces the total number of database round-trips required to find the required foreign key values.

In addition, this implementation eliminates any possibility of deadlocks occurring during the Insert/Update interaction. Deadlocks may still occur when processing the asynchronous remote index messages. As these will only occur in a backend process they will not be visible to IBM FHIR Server clients. Deadlocks are handle automatically using a rollback and retry mechanism. Care is taken to reduce the likelihood of deadlocks from occurring in the first place by sorting all record lists before they are processed.

It is worth noting that using multiple Kafka topic partitions can increase throughput by allowing more resource parameter messages to be processed in parallel. If sufficient threads are allocated across all the fhir-remote-index consumer instances, each thread will read data from a single partition. There is no point allocating more total threads than the number of configured partitions. The partition key is a function of the {resource-type, logical-id} tuple which guarantees that changes related to a particular logical resource will be pushed to the same partition. This guarantees that these changes will be processed in order.

## Processing Is Asynchronous

Old search parameters are deleted whenever a resource is updated. When remote indexing is enabled, this means that a resource will not be searchable until the remote index service has received and processed the message. Carefully examine your interaction scenarios with the IBM FHIR Server to determine if this behavior is suitable. In particular, conditional updates are unlikely to work as expected because the search may not return the expected value, depending on timing.

## Status

At this time, fhir-remote-index should be considered experimental.

## Build

To build fhir-remote-index, clone the git repository and build as follows:

```
git clone git@github.com:IBM/FHIR.git
cd FHIR
mvn clean install -f fhir-examples
mvn clean install -f fhir-parent
mvn clean install -f fhir-remote-index
```

Note that at this time, fhir-remote-index is not built by default so its project must be built explicitly as shown above.

## IBM FHIR Server Configuration

To enable remote indexing of search parameters, add the following `remoteIndexService` entry to the default FHIR server configuration file `config/default/fhir-server-config.json`. This entry identifies the Kafka service and topic to use for sending remote index messages. When this entry is present, the JDBC persistence layer will skip storing the parameters and instead will package the parameters into a message and publish it to Kafka.

```
{
    "__comment": "FHIR Server configuration",
    "fhirServer": {
       ...
       "remoteIndexService": {
            "type": "kafka",
            "instanceIdenfier": "a-random-uuid-value",
            "kafka": {
                "mode": "ACTIVE",
                "topicName": "FHIR_REMOTE_INDEX",
                "connectionProperties": {
                    "bootstrap.servers": "broker-0:9093, broker-1:9093, broker-2:9093, broker-3:9093, broker-4:9093, broker-5:9093",
                    "sasl.jaas.config": "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"token\" password=\"change-password\";",
                    "sasl.mechanism": "PLAIN",
                    "security.protocol": "SASL_SSL",
                    "ssl.protocol": "TLSv1.2",
                    "ssl.enabled.protocols": "TLSv1.2",
                    "ssl.endpoint.identification.algorithm": "HTTPS"
                }
            }
        },
        ...
```

## Running the fhir-remote index consumer

The fhir-remote-index application accepts two properties files defining access to Kafka and the target database:

1. `--kafka-properties` the properties describing connection information for the upstream Kafka topic containing the resource search parameter messages sent by the upstream IBM FHIR Server;
2. `--database-properties` the properties describing the location of the target database to which we will insert the search parameter records generated by the upstream IBM FHIR Server.

```
java -Djava.util.logging.config.file=logging.properties \
  -jar /path/to/git/FHIR/fhir-remote-index/target/fhir-remote-index-*-cli.jar \
  --db-type postgresql \
  --database-properties database.properties \
  --kafka-properties kafka.properties \
  --topic-name FHIR_REMOTE_INDEX \
  --consumer-count 3 \
  --instance-identifier "a-random-uuid-value"
```

Logging uses standard `java.util.logging` (JUL) and can be configured as follows:

```
handlers=java.util.logging.ConsoleHandler,java.util.logging.FileHandler
.level=INFO

# Console output
java.util.logging.ConsoleHandler.level = INFO
java.util.logging.ConsoleHandler.formatter=com.ibm.fhir.database.utils.common.LogFormatter

# What level do we want to see in the log file
java.util.logging.FileHandler.level=INFO

# Log retention: 50MB * 20 files ~= 1GB
java.util.logging.FileHandler.formatter=com.ibm.fhir.database.utils.common.LogFormatter
java.util.logging.FileHandler.limit=50000000
java.util.logging.FileHandler.count=20
java.util.logging.FileHandler.pattern=remoteindexservice-%u-%g.log
```

To configure the Kafka service, use a properties file as described by the [Kafka documentation](https://kafka.apache.org/documentation/#configuration):

```
bootstrap.servers=broker-0:9093, broker-1:9093, broker-2:9093, broker-3:9093, broker-4:9093, broker-5:9093
sasl.jaas.config=org.apache.kafka.common.security.plain.PlainLoginModule required username="token" password="change-password";
sasl.mechanism=PLAIN
security.protocol=SASL_SSL
ssl.protocol=TLSv1.2
ssl.enabled.protocols=TLSv1.2
ssl.endpoint.identification.algorithm=HTTPS
```

The database properties file:

```
db.host=your-db-host
db.port=5432
db.database=your-db-name
user=fhirserver
password=change-password
currentSchema=fhirdata
ssl=true
sslmode=require
sslcert=
sslrootcert=postgres.crt
sslkey=
```

| Property | DB Type | Description |
| ------ | --- | ----------- |
| db.host | All | The host name of the database containing the IBM FHIR Server schema | 
| db.port | All | The database port number |
| db.database | All | The database name |
| user | All | The database credential user |
| password | All | The database credential password |
| currentSchema | All | The database schema name |
| ssl | PostgreSQL | For PostgreSQL, use SSL (TLS) for the database connection |
| sslmode | PostgreSQL | The PostgreSQL SSL connection mode |
| sslcert | PostgreSQL | The PostgreSQL SSL certificate |
| sslrootcert | PostgreSQL | The PostgreSQL SSL root certificate |
| sslkey | PostgreSQL | The PostgreSQL SSL key |
| sslConnection | Db2 | true or false |
| sslTrustStoreLocation | Db2 | Location of the p12 trust store file containing the database server certificate |
| sslTrustStorePassword | Db2 | Password for the p12 trust store |

Note: Citus configuration is the same as PostgreSQL.

## Command Line Options

| Option | Description |
| ------ | ----------- |
| --consumer-count {n} | The number of Kafka consumer threads to start in this instance. Multiple instances of this service can be started. The total number of consumer threads across all instances should equal the number to the number of Kafka partitions on the topic. This should maximize throughput. |
| --kafka-properties {properties-file} | A Java properties file containing connection details for the upstream Kafka service. |
| --db-type {type} | The type of database. One of `postgresql`, `derby`, `db2` or `citus`. |
| --database-properties {properties-file} | A Java properties file containing connection details for the downstream IBM FHIR Server database. |
| --topic-name {topic} | The name of the Kafka topic to consume. Default `FHIR_REMOTE_INDEX`. |
| --instance-identifier {uuid} | Each IBM FHIR Server cluster should be allocated a unique instance identifier. This identifier is added to each message sent over Kafka. The consumer will ignore messages unless they include the same instance identifier value. This helps to ensure that messages are processed from only intended sources. |
| --consumer-group {grp} | Override the default Kafka consumer group (`group.id` value) for this application. Default `remote-index-service-cg`. |
| --schema-type {type} | Set the schema type. One of `PLAIN` or `DISTRIBUTED`. Default is `PLAIN`. The schema type `DISTRIBUTED` is for use with Citus databases. |
| --max-ready-time-ms {milliseconds} | The maximum number of milliseconds to wait for the database to contain the correct data for a particular set of consumed messages. Should be slightly longer than the configured Liberty transaction timeout value. |

# Asynchronous Message Handling and Transaction Boundaries

To guarantee delivery, the search parameter messages are posted to Kafka by the IBM FHIR Server before the transaction commits. The transaction will only be committed once all messages sent to Kafka have been acknowledged. This is important, because if the message were to be sent after the transaction, we could lose messages if a failure occured immediately after the transaction but before they were received by Kafka.

Because messages are sent to Kafka before the transaction is committed, it is possible that a fhir-remote-index consumer may receive a search parameter message before the corresponding resource version record is visible in the database. The consumer therefore runs a query at the start of a batch to determine if the current resource version record matches the message content. The following logic is then applied:

1. If the resource version doesn't yet exist in the database, the consumer will pause and wait for the transaction to be committed. The consumer will only wait up to the maximum transaction timeout window, at which point it will assume the transaction has failed and the message will be discarded.
2. If the resource version matches, but the lastUpdated time does not match, it assumes the message came from an IBM FHIR Server which failed before the transaction was committed, but the request was processed successfully by another server. The message will be discarded because there will be another message waiting in the queue from the second attempt.
3. If the resource version in the database already exceeds the version in the message, the message will be discarded because the information is already out of date. There will be another message waiting in the queue containing the search parameter values from the most recent resource.
