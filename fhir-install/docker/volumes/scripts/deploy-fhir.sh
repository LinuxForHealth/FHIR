#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2017
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

rm -rf /tmp/fhir-installer
unzip /dist/fhir-server-distribution.zip -d /tmp
/tmp/fhir-server-dist/install.sh /opt/ol/fhir-server
