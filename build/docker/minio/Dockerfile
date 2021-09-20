# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2019, 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM minio/minio:RELEASE.2021-08-31T05-46-54Z

# Indicate that we expect to connect to the minio service on port 9000
EXPOSE 9000

# Set up SSL for minio
RUN mkdir -p /root/.minio/certs
COPY public.crt /root/.minio/certs
COPY private.key /root/.minio/certs
