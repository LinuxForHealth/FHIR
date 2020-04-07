# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2016, 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM openliberty/open-liberty as base

ENV LICENSE accept

USER root
RUN \
  yum update -y && \
  yum install -y unzip nmap-ncat && \
  yum -y clean all

USER 1001
COPY dist /tmp/dist
COPY config /tmp/config
COPY userlib /tmp/userlib
RUN unzip -qq /tmp/dist/fhir-server-distribution.zip -d /tmp && \
    /tmp/fhir-server-dist/install-fhir.sh /opt/ol/wlp && \
    rm -rf /opt/ol/wlp/usr/servers/fhir-server/config/* && \
    cp -pr /tmp/config/* /opt/ol/wlp/usr/servers/fhir-server/config/ && \
    cp -pr /tmp/userlib/* /opt/ol/wlp/usr/servers/fhir-server/userlib/

# -----------------------------------------------------------------------------

FROM openliberty/open-liberty
COPY --chown=1001:0 --from=base /opt/ol/wlp /opt/ol/wlp

MAINTAINER Lee Surprenant <lmsurpre@us.ibm.com>

ENV FHIR /opt/ol/wlp/usr/servers/fhir-server

# Set the working directory to the fhir-server liberty server
WORKDIR ${FHIR}

# Set the output directory so that output will appear under the working directory
ENV WLP_OUTPUT_DIR=/opt/ol/wlp/usr/servers

# Tell liberty not to worry about a keystore since we provide our own at a different path
ENV KEYSTORE_REQUIRED "false"

# Replace links to defaultServer with links to fhir-server
USER root
RUN ln -sfn ${FHIR} /output && \
    ln -sfn ${FHIR} /config && \
    rm -rf /opt/ol/wlp/output && \
    rm -rf /opt/ol/wlp/usr/servers/defaultServer

USER 1001
# Create the configDropins directories for the fhir-server
RUN mkdir -p /config/configDropins/defaults /config/configDropins/overrides
CMD ["/opt/ol/wlp/bin/server", "run", "fhir-server"]
