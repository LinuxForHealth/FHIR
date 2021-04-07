#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# build_security_check - Security check from a Project PATH
# Reference https://github.com/victims/maven-security-versions
function build_security_check { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_security_check.log"

    PROJECT_PATH="$1"
    mkdir -p build/release/workarea/release_files/
    SEC_LOGS="build/release/workarea/release_files/${PROJECT_NAME}-build_security_check.log"
    echo "[Starting the check of security-versions] [`date`]"
    echo "- logging to ${LOGS}"
    mvn ${THREAD_COUNT} com.redhat.victims.maven:security-versions:check -f ${PROJECT_PATH} --log-file ${SEC_LOGS} -Dmaven.wagon.http.retryHandler.count=3

    echo "[Finished the check of security-versions] [`date`]"
    echo "[Report]: "
    for VULNERABLE in `cat ${SEC_LOGS} |  grep "is vulnerable to"`
    do
        echo "REPORTED VULNERABLITY"
        echo "${VULNERABLE}"
    done
    echo "[Done Report]"
}

build_security_check "fhir-parent"

# EOF