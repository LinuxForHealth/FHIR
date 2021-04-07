#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# build_security_check - Security check from a Project PATH
# Reference https://github.com/victims/maven-security-versions
function build_security_check { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_security_check.log"

    PROJECT_PATH="$1"
    LOGS="${LOG_DIR}/${PROJECT_NAME}-build_security_check.log"
    echo "[Starting the check of security-versions] [`date`]"
    echo "- logging to ${LOGS}"
    mvn ${THREAD_COUNT} com.redhat.victims.maven:security-versions:check -f ${PROJECT_PATH} --log-file ${LOGS} -Dmaven.wagon.http.retryHandler.count=3
    check_and_fail $? "build_security_check - ${PROJECT_PATH}" ${LOGS}

    echo "[Finished the check of security-versions] [`date`]"
    echo "[Report]: "
    for VULNERABLE in `cat ${LOGS} |  grep "is vulnerable to"`
    do
        echo "REPORTED VULNERABLITY"
        echo "${VULNERABLE}"
    done
    echo "[Done Report]"
}

# EOF