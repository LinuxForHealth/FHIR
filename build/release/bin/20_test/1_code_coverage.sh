#!/usr/bin/env bash

set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# verify and generate code coverage jacoco.exec and xml files

# helper method for generating the comma-separated list of tests to run / exclude
tests(){
  # tests to skip to ensure we can rebuild older images
  TESTS_TO_SKIP_ARR=(
    # the SPL IG has a time-sensitive constraint that causes validation failures in 4.10.2 and prior
    org.linuxforhealth.fhir.ig.us.spl.ExamplesValidationTest
  )

  # convert TESTS_TO_SKIP_ARR to a comma-delimited string of negated tests on a single line
  delim='!'
  for test in ${TESTS_TO_SKIP_ARR}; do
    echo -n \'${delim}${test}\'
    delim=',!'
  done
}

# fhir-tools
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-tools").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn -T2C test org.jacoco:jacoco-maven-plugin:0.8.7:report-aggregate -f fhir-tools

# fhir-examples
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-examples").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn -T2C test org.jacoco:jacoco-maven-plugin:0.8.7:report-aggregate -f fhir-examples

# fhir-parent
export BUILD_PROFILES=" $(jq -r '.build[] | select(.type == "fhir-parent").profiles | map(.) | join(",")' build/release/config/release.json)"
mvn -T2C test org.jacoco:jacoco-maven-plugin:0.8.7:report-aggregate -f fhir-parent \
        -P "${BUILD_PROFILES}" -Dsurefire.failIfNoSpecifiedTests=false -Dtest=$(tests)

# EOF
