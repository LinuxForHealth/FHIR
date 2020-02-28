# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2016, 2020
# 
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM websphere-liberty:kernel as base

ENV LICENSE accept

USER root
RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get upgrade -y \
    && apt-get install -y --no-install-recommends unzip \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
RUN mkdir /fhir-installer && chown -R 1001:0 /fhir-installer
USER 1001

ADD --chown=1001:0 target/fhir-server-distribution.zip /fhir-installer/
RUN unzip -qq /fhir-installer/fhir-server-distribution.zip -d /fhir-installer/ && \
    /fhir-installer/fhir-server-dist/install-fhir.sh /opt/ibm/wlp && \
    /opt/ibm/wlp/bin/installUtility install fhir-server --acceptLicense


FROM websphere-liberty:kernel
COPY --chown=1001:0 --from=base /opt/ibm/wlp/ /opt/ibm/wlp

MAINTAINER John T.E. Timm <johntimm@us.ibm.com>

ENV FHIR /opt/ibm/wlp/usr/servers/fhir-server

# Set the working directory to the fhir-server liberty server
WORKDIR ${FHIR}

# Set the output directory so that output will appear under the working directory
ENV WLP_OUTPUT_DIR=/opt/ibm/wlp/usr/servers

# Replace links to defaultServer with links to fhir-server
RUN ln -sf ${FHIR} /output && \
    ln -sf ${FHIR} /config && \
    rm -rf /opt/ibm/wlp/output && \
    rm -rf /opt/ibm/wlp/usr/servers/defaultServer

# Tell liberty not to worry about a keystore since we provide our own at a different path
ENV KEYSTORE_REQUIRED false

CMD ["/opt/ibm/wlp/bin/server", "run", "fhir-server"]
