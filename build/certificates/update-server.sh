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

openssl req -new -newkey rsa:4096 -x509  -keyout fhir.key -out fhir.crt -days 10960 -subj '/C=us/O=ibm/OU=defaultServer/cn=localhost' -passin pass:change-password -passout pass:change-password

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

keytool -keystore fhirKeyStore.p12 -alias default -certreq -file fhir.csr -storepass change-password -keypass change-password
openssl x509 -req -CA fhir.crt -CAkey fhir.key -in fhir.csr -out fhir-signed.crt -days 10960 -CAcreateserial -passin pass:change-password
keytool -keystore fhirKeyStore.p12 -alias libertyop -import -file fhir.crt -storepass change-password -keypass change-password -noprompt
keytool -keystore fhirKeyStore.p12 -alias default -import -file fhir-signed.crt -storepass change-password -keypass change-password -noprompt
keytool -keystore fhirTrustStore.p12 -alias libertyop -import -file fhir.crt -storepass change-password -keypass change-password -noprompt

# Import Minio
keytool -keystore fhirTrustStore.p12 -alias minio -import -file ${WORKSPACE}/build/docker/minio/public.crt -storepass change-password -keypass change-password -noprompt

# Create fhiruser
openssl req -new -newkey rsa:4096 -x509  -keyout fhiruser.key -out fhiruser.crt -days 10960 -subj '/C=US/OID.2.5.4.17=78758/ST=Texas/L=Austin/O=IBM Corporation/OU=IBM WatsonHealth/CN=fhiruser' -passin pass:change-password -passout pass:change-password

keytool -genkey -noprompt \
    -alias client-auth \
    -dname "CN=fhiruser, OU=IBM WatsonHealth, O=IBM Corporation, L=Austin, ST=Texas, OID.2.5.4.17=78758, C=US" \
    -keystore fhiruserKeyStore.p12 \
    -keyalg RSA \
    -storetype PKCS12 \
    -keysize 4096 \
    -validity 10960 \
    -storepass change-password \
    -keypass change-password

keytool -keystore fhiruserKeyStore.p12 -alias client-auth -certreq -file client-auth.csr -storepass change-password -keypass change-password
openssl x509 -req -CA fhiruser.crt -CAkey fhiruser.key -in client-auth.csr -out client-signed.crt -days 10960 -CAcreateserial -passin pass:change-password
keytool -keystore fhiruserKeyStore.p12 -alias CARoot -import -file fhiruser.crt -storepass change-password -keypass change-password -noprompt
keytool -keystore fhiruserKeyStore.p12 -alias client-auth -import -file client-signed.crt -storepass change-password -keypass change-password -noprompt
keytool -keystore fhiruserTrustStore.p12 -alias CARoot -import -file fhiruser.crt -storepass change-password -keypass change-password -noprompt

# keytool -list -keystore fhiruserTrustStore.p12 -storepass ${CHANGE_PASSWORD} -v
# keytool -list -keystore fhiruserKeyStore.p12 -storepass ${CHANGE_PASSWORD} -v

# Import client-auth
keytool -keystore fhirTrustStore.p12 -alias client-auth -import -file fhiruser.crt -storepass change-password -keypass change-password -noprompt

# Clean up existing
keytool -delete -keystore fhirKeyStore.p12 -storepass ${CHANGE_PASSWORD} -alias libertyop

mv ${WORKSPACE}/build/certificates/tmp/fhirKeyStore.p12 ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirKeyStore.p12
mv ${WORKSPACE}/build/certificates/tmp/fhirTrustStore.p12 ${WORKSPACE}/fhir-server/liberty-config/resources/security/fhirTrustStore.p12

# EOF