#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

cd build/security/resources/contrast/
cat << EOF > contrast_security.yaml
api:
  url: https://app.contrastsecurity.com/Contrast
  api_key: ${CONTRAST_API_KEY}
  service_key: ${CONTRAST_SERVICE_KEY}
  user_name: ${CONTRAST_AGENT_NAME}
application:
  metadata: bU="${CONTRAST_BU}"
EOF 

docker build . -t ibm-fhir-server-contrast:latest --squash
CONTAINER_ID=`docker create -p9443:9443 --env CONTRAST_API_KEY --env CONTRAST_AUTH --env CONTRAST_AGENT_NAME ibm-fhir-server-contrast:latest`
docker container start -a ${CONTAINER_ID}

mvn test -f ${WORKSPACE}/fhir-server-tests -DskipTests=true

docker container stop ${CONTAINER_ID}
# EOF