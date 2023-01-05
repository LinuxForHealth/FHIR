# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2022
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------
# Stage: Base

FROM openliberty/open-liberty:22.0.0.12-kernel-slim-java11-openj9-ubi as base

USER root
RUN yum install -y unzip
RUN install -d -o 1001 /opt/fhir-bulkdata-server
USER 1001

COPY target/fhir-bulkdata-server-distribution.zip /tmp/
RUN unzip -qq /tmp/fhir-bulkdata-server-distribution.zip -d /tmp && \
    /tmp/fhir-bulkdata-server-dist/install.sh /opt/ol && \
    mv /tmp/fhir-bulkdata-server-dist/tools /opt/fhir-bulkdata-server/tools
COPY src/main/docker/fhir-bulkdata-server/bootstrap.properties /opt/ol/wlp/usr/servers/defaultServer/
COPY src/main/docker/fhir-bulkdata-server/bootstrap.sh /opt/fhir-bulkdata-server/
# ----------------------------------------------------------------------------
# Stage: Runnable

FROM openliberty/open-liberty:22.0.0.12-kernel-slim-java11-openj9-ubi

ARG VERBOSE=true
ARG FHIR_SERVER_VERSION=5.0.0-SNAPSHOT

# The following labels are required:
LABEL name='LinuxForHealth FHIR Bulkdata Server'
LABEL version="$FHIR_SERVER_VERSION"
LABEL summary="LinuxForHealth FHIR Bulkdata Server with OpenJ9 and UBI 8"
LABEL description="The LinuxForHealth FHIR Bulkdata Server is a Standalone web application to process bulk data requests as JSR352 Java Batch jobs"

ENV FHIR_CONFIG_HOME=/opt/ol/wlp/usr/servers/defaultServer \
    WLP_LOGGING_CONSOLE_SOURCE=message,trace,accessLog,ffdc,audit \
    WLP_LOGGING_CONSOLE_LOGLEVEL=info \
    WLP_LOGGING_CONSOLE_FORMAT=SIMPLE \
    WLP_LOGGING_MESSAGE_SOURCE="" \
    WLP_LOGGING_MESSAGE_FORMAT=JSON \
    TRACE_FILE=stdout \
    TRACE_FORMAT=BASIC

COPY target/LICENSE /licenses/

COPY --chown=1001:0 --from=base /opt/ol/wlp/usr/servers/defaultServer/server.xml /opt/ol/wlp/usr/servers/defaultServer/
COPY --chown=1001:0 --from=base /opt/ol/wlp/usr/servers/defaultServer/configDropins /opt/ol/wlp/usr/servers/defaultServer/configDropins

RUN features.sh

COPY --chown=1001:0 --from=base /opt/ol/wlp/usr /opt/ol/wlp/usr

RUN configure.sh && \
    mkdir -p /output/bulkdata

COPY --chown=1001:0 --from=base /opt/fhir-bulkdata-server /opt/fhir-bulkdata-server

RUN mkdir -p /config/configDropins/overrides && \
    chmod -R 775 /config/configDropins/overrides && \
    chmod -R 775 /opt/ol/wlp/usr/servers/defaultServer/configDropins/defaults

# This block ensures the latest software is picked up.
USER root
RUN yum update -y && \
    yum clean all && \
    rm -rf /var/cache/yum
USER 1001

# Set the working directory to the liberty defaultServer
WORKDIR ${FHIR_CONFIG_HOME}

ENTRYPOINT ["/opt/fhir-bulkdata-server/bootstrap.sh"]
CMD ["/opt/ol/wlp/bin/server", "run", "defaultServer"]