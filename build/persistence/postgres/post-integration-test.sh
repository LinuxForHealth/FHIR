#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -o errexit
set -o nounset
set -o pipefail

echo "Performing integration test post-processing..."

echo "Bringing down the fhir server docker container(s)..."
cd build/persistence/postgres/
docker-compose down --remove-orphans --rmi local -v --timeout 30

echo "Integration test post-processing completed!"

# EOF 
###############################################################################