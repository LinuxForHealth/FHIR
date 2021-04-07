#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# build_security_check - Security check from a Project PATH
# Reference https://github.com/victims/maven-security-versions
function build_security_check { 
    PROJECT_PATH="${1}"
    mkdir -p build/release/workarea/release_files/
    SEC_LOGS="build/release/workarea/release_files/${PROJECT_NAME}-build_security_check.log"
    echo " --> logging to ${LOGS}"
    mvn -T2C com.redhat.victims.maven:security-versions:check -f "${PROJECT_PATH}" --log-file "${SEC_LOGS}"

    echo "[Report]: "
    for VULNERABLE in $(cat "${SEC_LOGS}" |  grep "is vulnerable to")
    do
        echo "REPORTED VULNERABLITY"
        echo "${VULNERABLE}"
    done
    echo "[Done Report]"
}

build_security_check "fhir-parent"

# EOF