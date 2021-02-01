#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# Updates the minio certificates 

openssl x509 -in ${WORKSPACE}/build/docker/minio/public.crt -text -noout
openssl req -new -newkey rsa:4096 -x509 -keyout ${WORKSPACE}/build/docker/minio/private.key -out ${WORKSPACE}/build/docker/minio/public.crt -days 10960 -subj '/CN=minio/OU=IBM WATSON HEALTH/O=IBM/L=Boston/ST=MASS/C=US' -passin pass:change-password -passout pass:change-password

# EOF