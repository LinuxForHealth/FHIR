# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2019, 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM minio/minio

# Create a non-admin user for the FHIR server to use to access db2
RUN groupadd -g 1002 fhir && \
    useradd -u 1002 -g fhir -M -d /database/config/fhirserver fhirserver && \
    echo "change-password" | passwd --stdin fhirserver

# Indicate that we expect to connect to the minio service on port 8443
EXPOSE 8443

# Set up SSL for minio
RUN mkdir /root/.minio/certs
COPY public.crt /root/.minio/certs
COPY private.key /root/.minio/certs
