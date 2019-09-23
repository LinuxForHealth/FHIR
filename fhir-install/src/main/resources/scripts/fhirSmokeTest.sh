#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

[ $# != 2 ] && echo "Usage: $0 server-host server-port" && exit 1

SERVER_HOST=$1
SERVER_PORT=$2

#
# Checking prereqs
#
! which curl > /dev/null 2> /dev/null && echo "curl not available. Please install it before running this script." >&2 && exit 2

echo

echo -n "Checking if server $SERVER_HOST is accessible on port $SERVER_PORT ... "
! nc -w 1 -z $SERVER_HOST $SERVER_PORT > /dev/null && echo "Error! Server not reachable." && exit 3
echo "OK!"

TMP_DIR=$(mktemp -d)
curl -k -u fhiruser:change-password https://$SERVER_HOST:$SERVER_PORT/fhir-server/api/v4/metadata > $TMP_DIR/meta.json 2> /dev/null

echo -n "Checking FHIR server signature... "
[ $(cat $TMP_DIR/meta.json | grep '"description":"IBM Server for HL7 FHIR version ' | wc -l) != "1" ] && echo "Error! Signature not found." && exit 4
echo "OK!"

echo -n "Checking FHIR API version... "
[ $(cat $TMP_DIR/meta.json | grep '"fhirVersion":"4.0.0"' | wc -l) != "1" ] && echo "Error! Wrong FHIR version or version absent." && exit 5
echo "OK!"

echo

#rm -rf $TMP_DIR

echo "Success! All checks passed."
