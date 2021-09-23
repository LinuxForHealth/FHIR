# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2019, 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# CICD Note: If there are issues with the CICD, it's worth checking the release, and setting the previous release
# such as minio/minio:RELEASE.2021-08-31T05-46-54Z 
FROM minio/minio

# Indicate that we expect to connect to the minio service on port 9000
EXPOSE 9000

# Set up SSL for minio
RUN mkdir -p /root/.minio/certs
COPY public.crt /root/.minio/certs
COPY private.key /root/.minio/certs
