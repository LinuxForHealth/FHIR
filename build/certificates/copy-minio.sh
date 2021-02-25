#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# Updates the minio certificates 

cp -f ${WORKSPACE}/build/certificates/tmp/fhir-minio-key.pem ${WORKSPACE}/build/docker/minio/private.key
cp -f ${WORKSPACE}/build/certificates/tmp/fhir-minio-chained.pem ${WORKSPACE}/build/docker/minio/public.crt

# EOF