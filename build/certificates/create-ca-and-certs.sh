#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# Root CA
# Instead of using -passin pass:change-password -passout pass:change-password
# opted for no password with nodes

if [ ! -d ${WORKSPACE}/build/certificates/tmp ]
then 
    mkdir ${WORKSPACE}/build/certificates/tmp
fi

cd ${WORKSPACE}/build/certificates/tmp

# Root CA
# -> server
# -> client
# -> minio
DAYS="10960"
BITS="4096"
openssl req -new -newkey rsa:${BITS} -x509 -nodes -sha512 -keyout fhir-ca-key.pem -out fhir-ca-crt.pem -days ${DAYS} -subj '/C=US/O=IBM Corporation/OU=IBM Watson Health/OU=FHIR'

# Minio
echo "FHIR Minio - certs are being generated and signed"
openssl req -newkey rsa:${BITS} -days ${DAYS} -nodes -sha512 -keyout fhir-minio-key.pem -out fhir-minio-req.pem -subj '/C=US/O=IBM/OU=IBM Watson Health/OU=FHIR/CN=minio' -config ../minio.conf -reqexts SAN
openssl x509 -req -in fhir-minio-req.pem -days ${DAYS} -sha512 -CA fhir-ca-crt.pem -CAkey fhir-ca-key.pem -days 10960 -CAcreateserial -out fhir-minio-cert.pem -extfile ../minio.conf -extensions SAN

# Useful for debugging keytool -printcert -v -file  fhir-minio-cert.pem 

# Verify the subject
openssl x509 -in fhir-minio-cert.pem -text | grep 'Subject:'

# Create the chained cert
cat fhir-minio-cert.pem > fhir-minio-chained.pem
cat fhir-ca-crt.pem >> fhir-minio-chained.pem

# Server
echo "FHIR Server - certs are being generated and signed"
openssl req -newkey rsa:${BITS} -days ${DAYS} -nodes -sha512 -keyout fhir-server-key.pem -out fhir-server-req.pem -subj '/C=US/O=IBM/OU=IBM Watson Health/OU=FHIR/CN=localhost' -config ../server.conf -reqexts SAN
openssl x509 -req -in fhir-server-req.pem -days ${DAYS} -sha512 -CA fhir-ca-crt.pem -CAkey fhir-ca-key.pem -days 10960 -CAcreateserial -out fhir-server-cert.pem  -extfile ../server.conf -extensions SAN
openssl x509 -in fhir-server-cert.pem -text | grep 'Subject:'

# Create the chained cert
cat fhir-server-cert.pem > fhir-server-chained.pem
cat fhir-ca-crt.pem >> fhir-server-chained.pem

openssl pkcs12 -export -out fhirKeyStore.p12 -inkey fhir-server-key.pem -in fhir-server-chained.pem -certfile fhir-ca-crt.pem -passout pass:change-password -name default
openssl pkcs12 -export -out fhirTrustStore.p12 -in fhir-server-cert.pem -certfile fhir-ca-crt.pem -passout pass:change-password -nokeys

keytool -keystore fhirTrustStore.p12 -alias ca-root -import -file fhir-ca-crt.pem -storepass change-password -keypass change-password -noprompt

# User
echo "FHIR User - certs are being generated and signed"
openssl req -newkey rsa:${BITS} -days ${DAYS} -nodes -sha512 -keyout fhir-client-key.pem -out fhir-client-req.pem -subj '/C=US/O=IBM/OU=IBM Watson Health/OU=FHIR/CN=fhiruser'
openssl x509 -req -in fhir-client-req.pem -days ${DAYS} -sha512 -CA fhir-ca-crt.pem -CAkey fhir-ca-key.pem -days 10960 -CAcreateserial -out fhir-client-cert.pem
openssl x509 -in fhir-client-cert.pem -text | grep 'Subject:'

openssl pkcs12 -export -out fhirClientKeyStore.p12 -inkey fhir-client-key.pem -in fhir-client-cert.pem -certfile fhir-ca-crt.pem -passout pass:change-password -name client-auth

keytool -keystore fhirTrustStore.p12 -alias client-auth -import -file fhir-ca-cert.pem -storepass change-password -keypass change-password -noprompt

# Kafka Client
echo "FHIR Kafka Client - certs are being generated and signed"
openssl req -newkey rsa:${BITS} -days ${DAYS} -nodes -sha512 -keyout fhir-kafka-client-key.pem -out fhir-kafka-client-req.pem -subj '/C=US/O=IBM/OU=IBM Watson Health/OU=FHIR/CN=kafka-client'
openssl x509 -req -in fhir-kafka-client-req.pem -days ${DAYS} -sha512 -CA fhir-ca-crt.pem -CAkey fhir-ca-key.pem -days 10960 -CAcreateserial -out fhir-kafka-client-cert.pem
openssl x509 -in fhir-kafka-client-cert.pem -text | grep 'Subject:'

openssl pkcs12 -export -out kafka.client.keystore.p12 -inkey fhir-kafka-client-key.pem -in fhir-kafka-client-cert.pem -certfile fhir-ca-crt.pem -passout pass:change-password -name server
openssl pkcs12 -export -out kafka.client.truststore.p12 -in fhir-kafka-client-cert.pem -certfile fhir-ca-crt.pem -passout pass:change-password -nokeys

# Update the Trust Store
keytool -keystore fhirTrustStore.p12 -alias server-ca -import -file fhir-server-cert.pem -storepass change-password -keypass change-password -noprompt

keytool -keystore fhirTrustStore.p12 -alias client-ca -import -file fhir-client-cert.pem -storepass change-password -keypass change-password -noprompt

keytool -keystore fhirTrustStore.p12 -alias minio-ca -import -file fhir-minio-cert.pem -storepass change-password -keypass change-password -noprompt

# EOF