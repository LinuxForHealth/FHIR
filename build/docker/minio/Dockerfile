# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2019, 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# CI/CD Note: This used to use "latest" but occassionally MinIO would break us.
# We should try to keep it recent, but lets continue pinning to a particular version.
# RELEASE.2022-06-03T01-40-53Z is not working.
FROM minio/minio:RELEASE.2022-05-26T05-48-41Z

# Indicate that we expect to connect to the minio service on port 9000
EXPOSE 9000

# Set up SSL for minio
RUN mkdir -p /root/.minio/certs
COPY public.crt /root/.minio/certs
COPY private.key /root/.minio/certs
