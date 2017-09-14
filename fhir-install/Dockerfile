# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2016,2017,2018,2019
# 
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM websphere-liberty:microProfile

MAINTAINER John T.E. Timm <johntimm@us.ibm.com>

ENV LICENSE accept

RUN mkdir /fhir-installer
ADD target/fhir-server-distribution.zip /fhir-installer/
RUN unzip -qq /fhir-installer/fhir-server-distribution.zip -d /fhir-installer/ && \
    /fhir-installer/fhir-server-dist/install-fhir.sh /opt/ibm/wlp && \
    rm -rf /fhir-installer && \
    /opt/ibm/wlp/bin/installUtility install fhir-server --acceptLicense

EXPOSE 9080 9443

WORKDIR /opt/ibm/wlp/usr/servers/fhir-server

ENV WLP_OUTPUT_DIR=/opt/ibm/wlp/usr/servers

CMD ["/opt/ibm/docker/docker-server", "run", "fhir-server"]
