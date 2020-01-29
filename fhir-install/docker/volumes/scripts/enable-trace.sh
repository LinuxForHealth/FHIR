#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# We'll use sed to add the appropriate "logging" element after the "httpEndpoint" element
# within the server.xml file.

server_xml="/opt/ol/wlp/usr/servers/fhir-server/server.xml"

sed -i'' -e '/<httpEndpoint.*>/a <logging traceSpecification="*=info:com.ibm.fhir.*=finer" traceFormat="BASIC"/>' $server_xml
