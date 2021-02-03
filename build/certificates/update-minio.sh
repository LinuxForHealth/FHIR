#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# Updates the minio certificates 

openssl x509 -in ${WORKSPACE}/build/docker/minio/public.crt -text -noout

cp ${WORKSPACE}/build/certificates/tmp/fhir.key ${WORKSPACE}/build/docker/minio/private.key
openssl rsa -passin pass:change-password -in ${WORKSPACE}/build/certificates/tmp/fhir.key  -out ${WORKSPACE}/build/docker/minio/private.key

cp ${WORKSPACE}/build/certificates/tmp/fhir.crt ${WORKSPACE}/build/docker/minio/public.crt

# EOF