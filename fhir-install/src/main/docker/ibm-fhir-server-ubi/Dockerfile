# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2016, 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM openliberty/open-liberty:20.0.0.9-full-java8-openj9-ubi as base

ENV LICENSE accept

USER root
RUN yum update -y --nobest && \
    yum install -y unzip && \
    yum clean all -y

USER 1001
COPY target/fhir-server-distribution.zip /tmp/
RUN unzip -qq /tmp/fhir-server-distribution.zip -d /tmp && \
    /tmp/fhir-server-dist/install-fhir.sh /opt/ol/wlp

# ----------------------------------------------------------------------------
FROM adoptopenjdk/openjdk11:ubi-minimal

COPY --chown=1001:0 --from=base /opt/ol/wlp /opt/ol/wlp

# Arguments

ARG VERBOSE=false
ARG OPENJ9_SCC=true

# Environment Variables for Liberty
ENV LIBERTY_VERSION=20.0.0.9
ENV PATH=/opt/ibm/wlp/bin:/opt/ibm/helpers/build:/opt/java/openjdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
ENV LOG_DIR=/logs WLP_OUTPUT_DIR=/opt/ibm/wlp/output OPENJ9_SCC=true
ENV RANDFILE=/tmp/.rnd OPENJ9_JAVA_OPTIONS=-Xshareclasses:name=liberty,nonfatal,cacheDir=/output/.classCache/
ENV OPENJ9_JAVA_OPTIONS=-Xshareclasses:name=openj9_system_scc,cacheDir=/opt/java/.scc,readonly,nonFatal

# Set the FHIR Server Directory Environment variable
ENV FHIR /opt/ol/wlp/usr/servers/fhir-server

# Set the output directory so that output will appear under the working directory
ENV WLP_OUTPUT_DIR=/opt/ol/wlp/usr/servers

# Tell liberty not to worry about a keystore since we provide our own at a different path
ENV KEYSTORE_REQUIRED "false"

# Set the working directory to the fhir-server liberty server
WORKDIR ${FHIR}

# Replace links to defaultServer with links to fhir-server
USER root
RUN ln -sfn ${FHIR} /output && \
    ln -sfn ${FHIR} /config && \
    rm -rf /opt/ol/wlp/output && \
    rm -rf /opt/ol/wlp/usr/servers/defaultServer && \ 
    microdnf update -y && \
    microdnf clean all

USER 1001

EXPOSE 9443

# Create the configDropins directories for the fhir-server
RUN mkdir -p /config/configDropins/defaults /config/configDropins/overrides

# Entrypoint followed by a command
ENTRYPOINT ["/opt/ibm/helpers/runtime/docker-server.sh"]
CMD ["/opt/ol/wlp/bin/server", "run", "fhir-server"]