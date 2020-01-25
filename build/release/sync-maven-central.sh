#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# sync_to_maven_central - syncs from bintray to maven central 
# Parameters: 
#   USER
#   API_KEY
#   SUBJECT
#   REPO_NAME
#   PKG_NAME
#   PKG_VERSION
# 
# Reference https://github.com/IBM/java-sdk-core/blob/master/build/sync2MC.sh
function sync_to_maven_central { 
    announce "${FUNCNAME[0]}" "sync_to_maven_central.log"

    USER=""
    API_KEY=""
    SUBJECT=""
    REPO_NAME=""
    PKG_NAME=""
    PKG_VERSION=""

    URL_STRING="https://api.bintray.com/maven_central_sync/${SUBJECT}/${REPO_NAME}/${PKG_NAME}/versions/${PKG_VERSION}"
    if [ $# -lt 6 ]
    then
        echo "
            Syntax:  
            $0 <bintray-user> <bintray-apikey> <bintray-repo-owner> <bintray-reponame> <bintray-packagename> <bintray-packageversion>>
            Example:
            $0 user1 A1098765 my-bintray-org my-bintray-repo1 my-bintray-package 0.0.1
        "
    else 
        BASIC_AUTH="${USER}:${API_KEY}"
        echo "Executing curl command..."
        curl -X POST --data '{ "close": "1" }' -H "Content-Type: application/json" -L -k --user ${BASIC_AUTH} ${URL_STRING}
    fi
}

# Not Executing the above commands ... yet

# EOF