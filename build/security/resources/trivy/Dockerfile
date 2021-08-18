# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM ibmcom/ibm-fhir-server:latest as trivy

USER root

# Set the working directory to the fhir-server liberty server
ENV FHIR /opt/ol/wlp/usr/servers/defaultServer
WORKDIR ${FHIR}

RUN yum install curl \
    && yum update -y \
    && curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/master/contrib/install.sh | sh -s -- -b /usr/local/bin \
    && trivy filesystem --format json -o results.json --exit-code 0 --no-progress / 

RUN echo -e "TRIVY_START\n\n\n" && cat results.json && echo  -e "\n\n\nTRIVY_END"

# We NEVER EVER want a container to succeed.  this is for pure reporting.
RUN exit -1
# EOF