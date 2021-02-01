# IBM FHIR Server - Certificates 

The certificates are used in development and CI/CD.
These certificates are not configured for production use.

The following keystores and truststores are used in the project.

# Audit: Continuous Integration Certificates

The following certificates are used with the audit tests/integrations.

```
build/audit/kafka/resources/kafka.broker1.keystore.jks
build/audit/kafka/resources/kafka.broker1.truststore.jks
build/audit/kafka/resources/kafka.broker2.keystore.jks
build/audit/kafka/resources/kafka.broker2.truststore.jks
build/audit/kafka/resources/kafka.consumer.truststore.jks
build/audit/kafka/resources/kafka.consumer.keystore.jks
build/audit/kafka/resources/kafka.producer.keystore.jks
build/audit/kafka/resources/kafka.producer.truststore.jks
```

1. Set `export CHANGE_PASSWORD=change-password`

2. Set `export WORKSPACE=$(pwd)`

3. Run `bash update-audit-kafka.sh` to update.

4. Copy the jks and creds files over to the build/audit/kafka directory

Note, the `tmp` folder should exist and be empty.

# Kafka Client

The following certifiates are used with the kafka-client. 

```
fhir-server/liberty-config/resources/security/kafka.client.keystore.p12
fhir-server/liberty-config/resources/security/kafka.client.truststore.p12
```

1. Set `export CHANGE_PASSWORD=change-password`

2. Set `export WORKSPACE=$(pwd)`

3. Run `bash update-kafka-client.sh` to update.

Note, the `tmp` folder should exist and be empty.

# Minio: Continuous Integration Certificates

The following certifiates are used with minio. 

```
build/docker/minio/private.key
build/docker/minio/public.crt
```

1. Set `export CHANGE_PASSWORD=change-password`

2. Set `export WORKSPACE=$(pwd)`

3. Run `bash update-minio.sh` to update.

# FHIR Client and Server Test - Trust and Key Stores

The following certifiates are used with the fhir-client module. 

```
fhir-client/src/test/resources/fhirClientTrustStore.p12
fhir-client/src/test/resources/fhirClientKeyStore.p12
fhir-server-test/src/test/resources/fhirClientTrustStore.p12
fhir-server-test/src/test/resources/fhirClientKeyStore.p12
```

1. Set `export CHANGE_PASSWORD=change-password`

2. Set `export WORKSPACE=$(pwd)`

3. Run `bash update-fhir-client.sh` to update.

Note, the `tmp` folder should exist and be empty.

# Server: Continuous Integration Certificates

The following certifiates are used with the IBM FHIR Server. 
This should be run before the update-fhir-client.sh (second to last).

```
fhir-server/liberty-config/resources/security/fhirKeyStore.p12
fhir-server/liberty-config/resources/security/fhirTrustStore.p12
```

1. Set `export CHANGE_PASSWORD=change-password`

2. Set `export WORKSPACE=$(pwd)`

3. Run `bash update-server.sh` to update.

Note, the `tmp` folder should exist and be empty.