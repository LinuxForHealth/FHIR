# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2016,2017,2018,2019
# 
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

FROM centos:centos6.7

MAINTAINER Filipe Avelino Corrêa <filipecorrea@br.ibm.com>

# Add fhir-server-distribution to Docker container
RUN mkdir /fhir-installer
ADD target/fhir-server-distribution.zip /fhir-installer/
RUN yum install -y --nogpg unzip
RUN unzip -qq /fhir-installer/fhir-server-distribution.zip -d /fhir-installer/

# Install IBM Java SDK
RUN mkdir /opt/java/
RUN yum install -y --nogpg tar
RUN unzip /fhir-installer/fhir-server-dist/java/ibm-java-sdk-8.0-3.10-linux-x86_64-ibmjcefips.zip -d /opt/java/

ENV JAVA_HOME /opt/java/ibm-java-x86_64-80/
ENV PATH $JAVA_HOME/bin:$PATH

# Install and configure WebSphere Liberty
RUN ./fhir-installer/fhir-server-dist/install.sh /opt/ibm/liberty

EXPOSE 9080 9443

# Run FHIR server
CMD ["/opt/ibm/liberty/wlp/bin/server", "run", "fhir-server"]
