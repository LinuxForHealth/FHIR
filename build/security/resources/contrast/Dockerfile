# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM ibmcom/ibm-fhir-server:latest as contrast

USER root

# Set the working directory to the fhir-server liberty server
ENV FHIR /opt/ol/wlp/usr/servers/defaultServer
ENV CONTRAST_AGENT_NAME ${CONTRAST_AGENT_NAME}
ENV CONTRAST_API_KEY ${ACONTRAST_API_KEYPI_KEY}
ENV CONTRAST_AUTH ${CONTRAST_AUTH}
WORKDIR ${FHIR}

RUN yum install curl 

RUN cd userlib/ && curl -X GET https://app.contrastsecurity.com/Contrast/api/ng/${AGENT_NAME}/agents/default/JAVA \
    -H 'Authorization: ${AUTH}' \
    -H 'API-Key: ${API_KEY}' -H 'Accept: application/json' -OJ

RUN echo '-Dcontrast.application.name=ibm-fhir-server' >> /opt/ol/wlp/usr/servers/defaultServer/jvm.options
RUN echo '-Dcontrast.application.version=99-SNAPSHOT' >> /opt/ol/wlp/usr/servers/defaultServer/jvm.options
RUN echo '-Dcontrast.enable=true' >> /opt/ol/wlp/usr/servers/defaultServer/jvm.options
RUN echo '-Dcontrast.auto.license.assessment=true' >> /opt/ol/wlp/usr/servers/defaultServer/jvm.options
RUN echo '-Dcontrast.agent.logger.stdout=true' >> /opt/ol/wlp/usr/servers/defaultServer/jvm.options
RUN echo "-javaagent:`pwd`/userlib/contrast.jar" >> /opt/ol/wlp/usr/servers/defaultServer/jvm.options
RUN echo '-Dcontrast.level=DEBUG' >> /opt/ol/wlp/usr/servers/defaultServer/jvm.options

COPY contrast_security.yaml /etc/contrast/java/contrast_security.yaml

# EOF