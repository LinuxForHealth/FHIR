#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Sets up the details for bintray modules/packages on BinTray
# You must set to use your API Key and API user in the curl request. 
# BINTRAY_USER=change-me
# BINTRAY_PASSWORD=change-me

BINTRAY_REPO_OWNER=ibm-watson-health
BINTRAY_REPO_NAME=ibm-fhir-server-snapshots
BINTRAY_VERSION=4.0.0

PKGS=("fhir-tools" "fhir-examples" "fhir-core" "fhir-task-api" "fhir-task-core" "fhir-database-utils" "fhir-config" "fhir-audit" "fhir-model" "fhir-validation" "fhir-registry" "fhir-ig-us-core" "fhir-persistence" "fhir-replication-api" "fhir-persistence-proxy" "fhir-persistence-schema" "fhir-persistence-jdbc" "fhir-provider" "fhir-operation" "fhir-operation-validate" "fhir-operation-document" "fhir-operation-healthcheck" "fhir-operation-apply" "fhir-operation-bulkdata" "fhir-bulkimportexport-webapp" "fhir-search" "fhir-client" "fhir-cli" "fhir-notification" "fhir-notification-kafka" "fhir-notification-websocket" "fhir-server" "fhir-server-webapp" "fhir-server-test" "fhir-swagger-generator" "fhir-openapi" "fhir-install" "fhir-coverage-reports" "fhir-parent")

# If you need to create a single module, then do something like the following. 
# PKGS=( "fhir-parent" )
for BINTRAY_PKG_NAME in ${PKGS[@]}
do 
echo ${BINTRAY_PKG_NAME}

# Setup the JSON Data
JSON_DATA=`
cat << EOF
{
  "name": "${BINTRAY_PKG_NAME}",
  "desc": "This package is part of the IBM FHIR Server project.",
  "labels": ["ibm-fhir-server", "fhir"],
  "licenses": ["Apache-2.0"],
  "custom_licenses": [],
  "vcs_url": "https://github.com/IBM/FHIR.git",
  "website_url": "http://ibm.github.io/FHIR",
  "issue_tracker_url": "https://github.com/IBM/FHIR/issues",
  "github_repo": "IBM/FHIR",
  "public_download_numbers": false,
  "public_stats": false
}
EOF
`

curl -XPOST -u${BINTRAY_USER}:${BINTRAY_PASSWORD} https://api.bintray.com/packages/ibm-watson-health/ibm-fhir-server-snapshots/ --data "${JSON_DATA}" -H "Content-Type: application/json"

done 
