# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2016, 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------
# Stage: Base

FROM openliberty/open-liberty:21.0.0.3-kernel-slim-java8-openj9-ubi as base

ENV LICENSE accept

USER root

RUN yum install -y unzip

RUN mkdir -p /opt/ibm-fhir-server

COPY target/fhir-server-distribution.zip /tmp/
RUN unzip -qq /tmp/fhir-server-distribution.zip -d /tmp && \
    /tmp/fhir-server-dist/install-fhir.sh /opt/ol/wlp && \
    mv /tmp/fhir-server-dist/tools /opt/ibm-fhir-server/tools
COPY src/main/docker/ibm-fhir-server/bootstrap.properties /opt/ol/wlp/usr/servers/defaultServer/
COPY src/main/docker/ibm-fhir-server/bootstrap.sh /opt/ibm-fhir-server/

USER 1001

# ----------------------------------------------------------------------------
# Stage: Runnable

FROM openliberty/open-liberty:21.0.0.3-kernel-slim-java8-openj9-ubi

ARG FHIR_VERSION=4.7.0-SNAPSHOT

# The following labels are required: 
LABEL name='IBM FHIR Server'
LABEL vendor='IBM'
LABEL version="$FHIR_VERSION"
LABEL summary="Image for IBM FHIR Server with OpenJ9 and UBI 8"
LABEL description="The IBM FHIR Server is a modular Java implementation of version 4 of the HL7 FHIR specification with a focus on performance and configurability."

COPY --chown=1001:0 --from=base /opt/ol/wlp/usr /opt/ol/wlp/usr
COPY --chown=1001:0 --from=base /opt/ibm-fhir-server /opt/ibm-fhir-server

COPY target/LICENSE /licenses/

ENV FHIR_CONFIG_HOME=/opt/ol/wlp/usr/servers/defaultServer \
    WLP_LOGGING_CONSOLE_SOURCE=message,trace,accessLog,ffdc,audit \
    WLP_LOGGING_CONSOLE_LOGLEVEL=info \
    WLP_LOGGING_CONSOLE_FORMAT=SIMPLE \
    WLP_LOGGING_MESSAGE_SOURCE="" \
    WLP_LOGGING_MESSAGE_FORMAT=JSON \
    TRACE_FILE=stdout \
    TRACE_FORMAT=BASIC

# Set the working directory to the liberty defaultServer
WORKDIR ${FHIR_CONFIG_HOME}

RUN features.sh && \
    VERBOSE=true configure.sh

ENTRYPOINT ["/opt/ibm-fhir-server/bootstrap.sh"]
CMD ["/opt/ol/wlp/bin/server", "run", "defaultServer"]