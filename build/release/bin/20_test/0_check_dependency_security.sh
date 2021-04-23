#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# build_security_check - Security check from a Project PATH
# Reference https://github.com/victims/maven-security-versions
function build_security_check { 
    mkdir -p ${WORKSPACE}/build/release/workarea/release_files/
    SEC_LOGS="${WORKSPACE}/build/release/workarea/release_files/fhir-parent-build_security_check.log"
    mvn com.redhat.victims.maven:security-versions:check -f "fhir-parent" --log-file "${SEC_LOGS}"

    echo "[Report]: "
    cat "${SEC_LOGS}" |  grep 'is vulnerable to'
    echo "[Done Report]"
}

build_security_check

# EOF