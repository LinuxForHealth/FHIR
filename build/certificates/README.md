# IBM FHIR Server - Development Certificates 

The certificates are used in development and CI/CD.
These certificates are not configured for production use.

The sections in this document walk through creation of the certificates in a temporary folder, and then subsequent update inline.  

The Audit Kafka jks are updated separately. 

# Create CA and Certs and P12s (does not replace)

This script should be run first, and subsequently each type of certificate / service must update their cert/key. 

1. Set `export CHANGE_PASSWORD=change-password`

2. Set `export WORKSPACE=$(pwd)`

3. Run `bash ${WORKSPACE}/build/certificates/create-ca-and-certs.sh` to update the certs.

At the end the tmp folder contains a copy of the certs and keys with a CA Issuer of `Issuer: C=US, O=IBM Corporation, OU=IBM Watson Health, OU=FHIR`.

# Minio: Continuous Integration Certificates

The following certificates are used with minio. 

```
build/docker/minio/private.key
build/docker/minio/public.crt
```

1. Set `export WORKSPACE=$(pwd)`

2. Run `bash ${WORKSPACE}/build/certificates/copy-minio.sh` to update.

# Server: Continuous Integration Certificates

The following certificates are used with the LinuxForHealth FHIR Server. 

```
fhir-server-webapp/src/main/liberty/config/resources/security/fhirKeyStore.p12
fhir-server-webapp/src/main/liberty/config/resources/security/fhirTrustStore.p12
```

1. Set `export WORKSPACE=$(pwd)`

2. Run `bash ${WORKSPACE}/build/certificates/copy-server.sh` to update.

Note, the `tmp` folder should exist and be empty.

# Audit and Notification: Continuous Integration Certificates

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
build/notifications/kafka/resources/kafka.broker1.keystore.jks
build/notifications/kafka/resources/kafka.consumer.truststore.jks
build/notifications/kafka/resources/kafka.consumer.keystore.jks
build/notifications/kafka/resources/kafka.broker2.keystore.jks
build/notifications/kafka/resources/kafka.broker2.truststore.jks
build/notifications/kafka/resources/kafka.producer.keystore.jks
build/notifications/kafka/resources/kafka.producer.truststore.jks
build/notifications/kafka/resources/kafka.broker1.truststore.jks
```

1. Set `export CHANGE_PASSWORD=change-password`

2. Set `export WORKSPACE=$(pwd)`

3. Run `bash ${WORKSPACE}/build/certificates/update-audit-kafka.sh` to update.

4. Copy the jks and creds files over to the build/audit/kafka directory

Note, the `tmp` folder should exist and be empty.

# Kafka Client

The following certificates are used with the kafka-client. 

```
fhir-server-webapp/src/main/liberty/config/resources/security/kafka.client.keystore.p12
fhir-server-webapp/src/main/liberty/config/resources/security/kafka.client.truststore.p12
```

1. Set `export WORKSPACE=$(pwd)`

2. Run `bash ${WORKSPACE}/build/certificates/copy-kafka-client.sh` to update.

Note, the `tmp` folder should exist and be empty.

# FHIR Client and Server Test - Trust and Key Stores

The following certificates are used with the fhir-client module. 

```
fhir-client/src/test/resources/fhirClientTrustStore.p12
fhir-client/src/test/resources/fhirClientKeyStore.p12
fhir-server-test/src/test/resources/fhirClientTrustStore.p12
fhir-server-test/src/test/resources/fhirClientKeyStore.p12
```

1. Set `export WORKSPACE=$(pwd)`

2. Run `bash ${WORKSPACE}/build/certificates/copy-fhir-client.sh` to update.

Note, the `tmp` folder should exist and be empty.