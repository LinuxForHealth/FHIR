#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2019, 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# functions to manage git/release/build
# source them, or call using the case statement

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

# Import Scripts
source "$(dirname '$0')/logging.sh"
source "$(dirname '$0')/release.properties"

# Basic information
SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"
debugging "Script Name is ${SCRIPT_NAME}"

# Reset to Original Directory
popd > /dev/null

###############################################################################
# function declarations:  

# SOURCE DIRS CAPTURES THE DIRECTORIES WHICH ARE GOING TO BE USED 
# in deployment
SOURCE_DIRS=()

# files_changed - between two branches or two tags
# Parameters: 
#   TARGET_BRANCH
#   SOURCE_BRANCH 
function files_changed { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-files_changed.log"

    TARGET_BRANCH="${1}"
    SOURCE_BRANCH="${2}"
    CHANGED_FILES=`git log ${TARGET_BRANCH}..${SOURCE_BRANCH} --name-only --pretty=format:`
    for CHANGED_FILE in `echo ${CHANGED_FILES}`
    do 
        echo "Changed File: ${CHANGED_FILE}"
    done
}

# files_changed_by_source_folder - between two branches or two tags
# Parameters: 
#   TARGET_BRANCH
#   SOURCE_BRANCH 
function files_changed_by_source_folder { 
    echo "[Identifying the changed files][START]: "
    TARGET_BRANCH="${1}"
    SOURCE_BRANCH="${2}"
    
    pushd `pwd`
    cd ${TRAVIS_BUILD_DIR}
    git fetch origin ${SOURCE_BRANCH}:${SOURCE_BRANCH}
    for SOURCE_DIR in `find . -type d ${BUILD_ENV_TYPE_DEPTH} | grep -v '.git' | grep -v 'build' | grep -v '/docs'`
    do 
        #echo "Processing ${SOURCE_DIR}"
        SOURCE_DIR=`echo ${SOURCE_DIR} | sed 's|./||g'`
        FILES=`git log --relative=${SOURCE_DIR}/ --name-only ${TARGET_BRANCH}..${SOURCE_BRANCH} --pretty=format:`
        
        COUNT=0
        for FILE in ${FILES}
        do 
            #echo "- ${FILE}"
            COUNT=$((COUNT+1))
        done

        if [ ${COUNT} -ne 0 ]
        then 
            # populate the source_dirs 
            echo "[SOURCE_DIR] -> [${SOURCE_DIR}][FILES: ${COUNT}]"
            SOURCE_DIRS+=("${SOURCE_DIR}")
        fi 
    done 
    popd

    echo "[Identifying the changed files][DONE] "
    echo "[SOURCE_DIRS] changed : [${SOURCE_DIRS[@]}]"
    
}

# comment_on_pull_request - adds a comment on the pull request.
# the token is encrypted following https://medium.com/@preslavrachev/using-travis-for-secure-building-and-deployment-to-github-5a97afcac113
function comment_on_pull_request {
    COMMENT_IN=$1

    # quick switch between github.com and ibm.com domains.
    BASE_URL="api.github.com"
    
    COMMENT="{\"body\": \"${COMMENT_IN}\"}"
    API_URL="https://${BASE_URL}/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/comments"

    if [[ "${TRAVIS_PULL_REQUEST_BRANCH}" != "" && "${TRAVIS_EVENT_TYPE}" == "pull_request" && "${TRAVIS_PULL_REQUEST}" != "false" ]]
    then
        curl -H "Authorization: token ${GITHUB_OAUTH_TOKEN}" -X POST -d "${COMMENT}" "${API_URL}"
    fi
}

# label_pr_with_status - labels the build with a current status
#
# URL: https://developer.github.com/v3/issues/labels/#add-labels-to-an-issue
# POST /repos/:owner/:repo/issues/:issue_number/labels
#
# Parameter: 
# - BUILD_LABEL 
function label_pr_with_status {
    BUILD_LABEL=$1

    # quick switch between github.com and ibm.com domains.
    BASE_URL="api.github.com"
    if [[ "${TRAVIS_APP_HOST}" == *"ibm.com"* ]]
    then
        BASE_URL="api.github.com"
    fi

    LABEL="{  \
            \"labels\": [ \
                ${BUILD_LABEL}
                ] \
        }"
    API_URL="https://${BASE_URL}/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/labels"

    if [[ "${TRAVIS_EVENT_TYPE}" == "pull_request" ]]
    then
        echo "API_URL: ${API_URL}"
        echo "LABEL: ${LABEL}"

        curl -H "Authorization: token ${GITHUB_OAUTH_TOKEN}" -H "User-Agent: ibm-fhir-cicd" -X POST -d "${LABEL}" "${API_URL}"
    fi
}

# comment_on_pull_request_with_log - adds a comment on the pull request.
# the token is encrypted following https://medium.com/@preslavrachev/using-travis-for-secure-building-and-deployment-to-github-5a97afcac113
function comment_on_pull_request_with_log {
    COMMENT_IN=$1

    # quick switch between github.com and ibm.com domains.
    BASE_URL="api.github.com"

    # Log File -> Comment is wrapped. 
    # 50K chars seem enough
    LOG_FILE="build/logs/$(ls -A1tr build/logs | grep -v diag.log | tail -n 1)"

    # Expressions
    # - -e 's|"|\&#39;|g'
    # - -e 's|'|\&#39;|g'
    #LOG_DETAIL=`tail -c 50000 ${LOG_FILE} | sed -e 's|&|\&amp;|g'  -e 's|<|\&lt;|g' -e 's|>|\&gt;|g' -e 's|\"|\&quot;|g' -e "s|\'|\&quot;|g" -e 's|\n|\\\\r\\\\n|g' -e "s|'||g" -e 's|"||g' | tr '\t' '    ' | sed 's|$|\\\\r\\\\n|g'| awk ' /$/ {print}' ORS=' '`
    LOG_DETAIL="Please refer to build log. "
    COMMENT_UPDATE="${COMMENT_IN}<br><hr>\r\n\`\`\`${LOG_DETAIL}\`\`\`"
    COMMENT=`echo "{\"body\": \"${COMMENT_UPDATE}\"}"  `
    
    # for debug - use --- 
    # echo "--> [DEBUG JSON --> " 
    # echo "${COMMENT}"
    
    API_URL="https://${BASE_URL}/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/comments"

    if [[ "${TRAVIS_PULL_REQUEST_BRANCH}" != "" && "${TRAVIS_EVENT_TYPE}" == "pull_request" && "${TRAVIS_PULL_REQUEST}" != "false" ]]
    then
        echo "API_URL: ${API_URL}"
        echo "LABEL: ${COMMENT}"
        curl -H "Authorization: token ${GITHUB_OAUTH_TOKEN}" -H "User-Agent: ibm-fhir-cicd" -X POST -d "${COMMENT}" "${API_URL}"
    fi
}

###############################################################################
# Menu
ACTION="$1"
case $ACTION in
    comment_on_pull_request) 
        comment_on_pull_request "${2}"
        header_line
    ;;
    comment_on_pull_request_with_log)
        comment_on_pull_request_with_log "${2}"
        header_line
    ;;
    label_pr_with_status)
        LABELS="${3}"
        label_pr_with_status 
    ;;
    *)
        echo "invalid function called, dropping through "
    ;;
esac 
# EOF 