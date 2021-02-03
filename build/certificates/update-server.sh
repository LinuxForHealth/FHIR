#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------


##############################################################################################################
# TYPE   : SERVER-TRUSTSTORE
# FILE   : fhir-server/liberty-config/resources/security/fhirTrustStore.p12
# ALIAS  : libertyop
#          minio
#          client-auth
# SHASUM : 9D:94:C2:F8:C1:51:9B:0F:21:50:4F:BB:60:A4:8A:3F:AF:C0:F0:13:C4:80:BE:A3:94:42:04:46:56:DB:D9:7B
#          5F:44:32:A1:5D:DD:06:66:81:42:84:73:36:BC:A0:B5:CA:31:F1:F0:E7:00:1F:5F:47:87:3C:CB:D6:54:14:F2
#          6B:B5:42:1E:99:A1:92:51:EB:6B:7C:27:A6:2C:07:4B:DB:0F:09:47:D3:67:E5:A3:1E:63:89:D2:EA:B7:58:0D
# LINKS  : 
#          

# TYPE   : SERVER-KEYSTORE
# FILE   : fhir-server/liberty-config/resources/security/fhirKeyStore.p12
# ALIAS  : default
# SHASUM : 9D:94:C2:F8:C1:51:9B:0F:21:50:4F:BB:60:A4:8A:3F:AF:C0:F0:13:C4:80:BE:A3:94:42:04:46:56:DB:D9:7B
# LINKS  : 
#          

keytool -list -keystore ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirTrustStore.p12 -storepass ${CHANGE_PASSWORD} -v
keytool -list -keystore ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirKeyStore.p12 -storepass ${CHANGE_PASSWORD} -v

# Root CA
openssl req -new -newkey rsa:4096 -x509  -keyout fhir.key -out fhir.crt -days 10960 -subj '/C=US/O=IBM Corporation/OU=IBM Watson Health' -passin pass:change-password -passout pass:change-password

# Create the Keystore with a temporary key
keytool -genkey -noprompt \
    -alias default \
    -dname "CN=localhost, OU=defaultServer, O=ibm, C=us" \
    -keystore fhirKeyStore.p12 \
    -keyalg RSA \
    -storetype PKCS12 \
    -keysize 4096 \
    -validity 10960 \
    -storepass change-password \
    -keypass change-password

# Create the Cert Request 
keytool -keystore fhirKeyStore.p12 -alias default -certreq -file server.csr -storepass change-password -keypass change-password

# Sign the Certificate Request
openssl x509 -req -CA fhir.crt -CAkey fhir.key -in server.csr -out signed.crt -days 10960 -CAcreateserial -passin pass:change-password

# Add the Certificate Authority that signed it
keytool -keystore fhirKeyStore.p12 -alias libertyop -import -file fhir.crt -storepass change-password -keypass change-password -noprompt

# Add the Signed Certificate as the chain is now in place
keytool -keystore fhirKeyStore.p12 -alias default -import -file signed.crt -storepass change-password -keypass change-password -noprompt

# Create the Truststore
keytool -keystore fhirTrustStore.p12 -alias libertyop -import -file fhir.crt -storepass change-password -keypass change-password -noprompt

####
# Import Minio
keytool -keystore fhirTrustStore.p12 -alias minio -import -file ${WORKSPACE}/build/docker/minio/public.crt -storepass change-password -keypass change-password -noprompt

####
# Create fhiruser

# Generate the Temporary Certificate
keytool -genkey -noprompt \
    -alias client-auth \
    -dname "CN=fhiruser, OU=IBM Watson Health, O=IBM Corporation, C=US" \
    -keystore fhiruserKeyStore.p12 \
    -keyalg RSA \
    -storetype PKCS12 \
    -keysize 4096 \
    -validity 10960 \
    -storepass change-password \
    -keypass change-password

# Create the Certificate Request
keytool -keystore fhiruserKeyStore.p12 -alias client-auth -certreq -file client-auth.csr -storepass change-password -keypass change-password

# Sign the Cert with the fhir CA
openssl x509 -req -CA fhir.crt -CAkey fhir.key -in client-auth.csr -out client-signed.crt -days 10960 -CAcreateserial -passin pass:change-password

# Add the CA
keytool -keystore fhiruserKeyStore.p12 -alias ca-root -import -file fhir.crt -storepass change-password -keypass change-password -noprompt

# Add the Signed Cert
keytool -keystore fhiruserKeyStore.p12 -alias client-auth -import -file client-signed.crt -storepass change-password -keypass change-password -noprompt

# Create the Trust Store
keytool -keystore fhiruserTrustStore.p12 -alias ca-root -import -file fhir.crt -storepass change-password -keypass change-password -noprompt

# keytool -list -keystore fhiruserTrustStore.p12 -storepass ${CHANGE_PASSWORD} -v
# keytool -list -keystore fhiruserKeyStore.p12 -storepass ${CHANGE_PASSWORD} -v

mv ${WORKSPACE}/build/certificates/tmp/fhirKeyStore.p12 ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirKeyStore.p12
mv ${WORKSPACE}/build/certificates/tmp/fhirTrustStore.p12 ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirTrustStore.p12

# EOF