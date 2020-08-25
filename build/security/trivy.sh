#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Check to see if the docker image is already built
if [ `docker images --filter "reference=ibmcom/ibm-fhir-server:latest" --format "{{.ID}}: {{.Repository}}" | wc -l` -eq 0 ]
then 
    echo "Image is Built..."
else 
    echo "Image is NOT Built... REBUILD IT."
fi

# Builds the image, and extracts the TRIVY_START and TRIVY_END
docker build ${WORKSPACE}/build/security/resources/trivy/ --tag ibm-fhir-server-vulnerability-trivy --rm --force-rm --no-cache | tee ${WORKSPACE}/build/security/logs/output/trivy-build.log
sed -n '/^TRIVY_START$/,/^TRIVY_END$/p' ${WORKSPACE}/build/security/logs/output/trivy-build.log | sed 's|TRIVY_START||g' | sed 's|TRIVY_END||g' > ${WORKSPACE}/build/security/logs/output/trivy.json

cat ${WORKSPACE}/build/security/logs/output/trivy.json | jq -r '.[].Vulnerabilities[] | "\(.VulnerabilityID),\(.PkgName)\(.Title)"' > ${WORKSPACE}/build/security/logs/output/trivy.log
# EOF