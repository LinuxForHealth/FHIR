# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2016,2017,2018,2019
# 
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM websphere-liberty:18.0.0.3-microProfile1

MAINTAINER John T.E. Timm <johntimm@us.ibm.com>

ENV LICENSE accept

USER root
RUN mkdir /fhir-installer && chown -R 1001:0 /fhir-installer
USER 1001

ADD --chown=1001:0 target/fhir-server-distribution.zip /fhir-installer/
RUN unzip -qq /fhir-installer/fhir-server-distribution.zip -d /fhir-installer/ && \
    /fhir-installer/fhir-server-dist/install-fhir.sh /opt/ibm/wlp && \
    /opt/ibm/wlp/bin/installUtility install fhir-server --acceptLicense

USER root
RUN rm -rf /fhir-installer
USER 1001

EXPOSE 9080 9443

WORKDIR /opt/ibm/wlp/usr/servers/fhir-server

ENV WLP_OUTPUT_DIR=/opt/ibm/wlp/usr/servers

CMD ["/opt/ibm/wlp/bin/server", "run", "fhir-server"]
