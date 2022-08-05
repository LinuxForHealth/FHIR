#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

run_tests(){
    # The integration tests may be overriden completely, or fall through to the default.
    migration="${1}"

    if [ ! -z "${migration}" ] && [ -f "build/migration/${migration}/2_previous-integration-test.sh" ]
    then
        echo "Running [${migration}] previouos specific integration tests"
        bash ${WORKSPACE}/fhir/build/migration/${migration}/2_previous-integration-test.sh
    else
        # Runs the migration tests
        echo "Running containers are:"
        docker container ls -a
        echo ""

        echo "fhir container are:"
        docker container inspect $(docker container ls | grep fhir_1 | awk '{print $NF}' ) | jq -r '.[]'
        echo ""

        mkdir -p ${WORKSPACE}/fhir/build/migration/integration-test-results
        echo "Running Integration tests: "
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false | tee ${WORKSPACE}/fhir/build/migration/integration-test-results/prev-integration-tests.log
        # Add || docker container logs "$(docker container ls | grep fhir_1 | awk '{print $NF}' )"
        echo "Done Running Tests"
        echo ""
    fi
}

clean_up(){
    # Remove problematic resources
    fhir_base_url=https://localhost:9443/fhir-server/api/v4
    # previous CompartmentDefinition instances from fhir-examples cause trouble for our updated compartment handling
    # Evidence and EvidenceVariable have backwards-breaking changes in FHIR 4.3.0
    problem_types=CompartmentDefinition,Evidence,EvidenceVariable
    for r in $(curl --fail -k -u fhiruser:change-password "${fhir_base_url}?_type=${problem_types}&_count=1000" | jq -r '.entry[].fullUrl'); do
      echo "erasing ${r}"
      curl --fail -i -k -u fhiruser:change-password -H "Content-Type: application/json" ${r}'/$erase' -d '
        {
          "resourceType": "Parameters",
          "parameter": [
            {
              "name": "patient",
              "valueString": "test"
            },
            {
              "name": "reason",
              "valueString": "remove problematic resources before migrating"
            }
          ]
        }'
    done

}

###############################################################################
export BASE="$(pwd)"
# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the migration/bin directory
cd "prev/"

bash ${WORKSPACE}/fhir/build/common/wait-for-it.sh
run_tests "${1}"
clean_up

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################
