#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2017
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

rm -fr ~/fhir-installer
rm -fr /opt/ibm/fhir-server
unzip ~/dist/fhir-server-distribution.zip -d ~/fhir-installer
export JAVA_HOME=/opt/ibm/fhir-server/wlp/ibm-java-x86_64-80
export PATH=$JAVA_HOME/bin:$PATH
~/fhir-installer/fhir-server-dist/install.sh /opt/ibm/fhir-server
