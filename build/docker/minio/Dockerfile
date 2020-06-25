# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2019, 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM minio/minio

# Indicate that we expect to connect to the minio service on port 8443
EXPOSE 9000

# Set up SSL for minio
RUN mkdir -p /root/.minio/certs
COPY public.crt /root/.minio/certs
COPY private.key /root/.minio/certs
