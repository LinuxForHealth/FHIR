# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------
# Stage: Base

# IBM Semeru Runtimes provides Non-official docker images. These are maintained by IBM.
# The link to Semeru is at https://hub.docker.com/r/ibmsemeruruntime/open-11-jdk
FROM ibmsemeruruntime/open-11-jdk:ubi_min-jre as base

# Create the base working directory
RUN mkdir -p /opt/ibm/fhir/bucket/workarea

# Copy in the relevant artifacts in a single command
COPY ./run.sh ./target/fhir-bucket-*-cli.jar ./target/LICENSE /opt/ibm/fhir/bucket/

# ----------------------------------------------------------------------------
# Stage: Runnable

FROM registry.access.redhat.com/ubi8/ubi-minimal

ARG FHIR_VERSION=4.10.0

# The following labels are required: 
LABEL name='IBM FHIR Server - Bucket Tool'
LABEL vendor='IBM'
LABEL version="$FHIR_VERSION"
LABEL summary="Image for IBM FHIR Server - Bucket Tool with OpenJ9 and UBI 8"
LABEL description="The IBM FHIR Server - Bucket Tool manages the IBM FHIR Server operations and generates synthetic load."

# Environment variables
ENV SKIP false
ENV TOOL_INPUT false

ENV LANG='en_US.UTF-8'
ENV LANGUAGE='en_US:en'
ENV LC_ALL='en_US.UTF-8'
ENV TZ 'UTC'

WORKDIR /opt/ibm/fhir/bucket/workarea

COPY --chown=1001:0 --from=base /opt/ /opt/

RUN chmod -R 755 /opt/ibm/fhir/bucket/run.sh && \
    mkdir -p /licenses && \
    mv /opt/ibm/fhir/bucket/LICENSE /licenses && \
    microdnf update -y && \
    microdnf install -y nc tzdata openssl curl ca-certificates fontconfig glibc-langpack-en gzip tar findutils shadow-utils && \
    groupadd -r fhirbucketadmin -g 1001 && \
    useradd -u 1001 -r -g 1001 -m -d /opt/ibm/fhir/bucket/home -s /sbin/nologin fhirbucketadmin && \
    chmod -R 755 /opt/ibm/fhir/bucket/ && \
    rm -rf /var/cache/yum && \
    rm -f /@System.solv && \
    microdnf clean all && \
    rm -rf /var/tmp/* && \
    rm -rf /tmp/* && \
    mkdir -p /opt/ibm/fhir/bucket/workarea && \
    chmod -R 775 /opt/ibm/fhir/bucket/workarea

USER 1001

ENTRYPOINT ["/opt/ibm/fhir/bucket/run.sh"]