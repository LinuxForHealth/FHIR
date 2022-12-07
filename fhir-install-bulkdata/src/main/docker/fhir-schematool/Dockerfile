# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2022
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------
# Stage: Base

# IBM Semeru Runtimes provides Non-official docker images as part of this repo. These are maintained by IBM.
# The link to Semeru is at https://hub.docker.com/r/ibmsemeruruntime/open-11-jdk
FROM ibmsemeruruntime/open-11-jdk:ubi_min-jre as base

# Create the base working directory
RUN mkdir -p /opt/schematool/workarea

# Copy in the relevant artifacts in a single command
COPY ./run.sh ./target/fhir-persistence-schema-*-cli.jar ./target/LICENSE /opt/schematool/

RUN curl -L -o /opt/schematool/jq https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64 && \
    chmod +x /opt/schematool/jq

# ----------------------------------------------------------------------------
# Stage: Runnable

FROM registry.access.redhat.com/ubi8/ubi-minimal

ARG FHIR_VERSION=4.8.0

# The following labels are required: 
LABEL name='IBM FHIR Schema Tool'
LABEL vendor='IBM'
LABEL version="$FHIR_VERSION"
LABEL summary="Image for IBM FHIR Server Schema Tool with OpenJ9 and UBI 8"
LABEL description="The IBM FHIR Server Schema Tool is designed to create and update the IBM FHIR Server's schema."

# Environment variables
ENV SKIP false
ENV TOOL_INPUT false

ENV LANG='en_US.UTF-8'
ENV LANGUAGE='en_US:en'
ENV LC_ALL='en_US.UTF-8'
ENV TZ 'UTC'

WORKDIR /opt/schematool

COPY --chown=1001:0 --from=base /opt/ /opt/

RUN chmod -R 755 /opt/schematool/run.sh && \
    mkdir -p /licenses && \
    mv /opt/schematool/LICENSE /licenses && \
    microdnf update -y && \
    microdnf install -y nc tzdata openssl curl ca-certificates fontconfig glibc-langpack-en gzip tar findutils shadow-utils && \
    groupadd -r fhirschemaadmin -g 1001 && \
    useradd -u 1001 -r -g 1001 -m -d /opt/schematool/home -s /sbin/nologin fhirschemaadmin && \
    chmod -R 755 /opt/schematool && \
    rm -rf /var/cache/yum && \
    rm -f /@System.solv && \
    microdnf clean all && \
    rm -rf /var/tmp/* && \
    rm -rf /tmp/* && \
    mkdir -p /opt/schematool/workarea && \
    chmod -R 775 /opt/schematool/workarea

USER 1001

ENTRYPOINT ["/opt/schematool/run.sh"]