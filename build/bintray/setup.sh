#!/usr/bin/env bash

# Sets up the details for bintray modules/packages.

BINTRAY_USER=change-me
BINTRAY_PASSWORD=change-me

BINTRAY_REPO_OWNER=ibm-watson-health
BINTRAY_REPO_NAME=ibm-fhir-server-snapshots
BINTRAY_VERSION=4.0.0

BINTRAY_PKG_NAME=fhir-tools

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


  global:
    - BINTRAY_REPO_OWNER=ibm-cloud-sdks
    - BINTRAY_REPO_NAME=ibm-cloud-sdk-repo
    - BINTRAY_PKG_NAME=com.ibm.cloud:sdk-core

GROUP_ID:artifactId

BINTRAY_PATH=<groupid/artifactId>
com/ibm/fhir${BINTRAY_VERSION}

