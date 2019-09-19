#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Manages the fhir build

# Global Settings are: 
# BUILD_TYPE for logging / execution purposes
# THREAD_COUNT = (described in more detail below)
# ACTION = Action Specific settings are covered in the case statement
# ACTION_PARAMS = list of parameters (described in case statement)

# BUILD_DATETIME
BUILD_DATETIME=`date +%FT%T`

# BUILD_TYPES
# - BRANCH - builds a specific branch (no-release)
# - PR - builds a specific pull_request (no-release)
# - TAG - builds from current tag to prior tag (release)
#       - MUST be since last SUCCESSFUL TAG build
# - REBUILD_RELEASE
BUILD_TYPE="$1"

# SOURCE DIRS CAPTURES THE DIRECTORIES WHICH ARE GOING TO BE USED 
# in deployment
SOURCE_DIRS=()

###############################################################################
# Functions: 
# build_clean - Build source from a Project PATH
# Reference https://maven.apache.org/plugins/maven-clean-plugin/
function build_clean { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-clean.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    mvn ${THREAD_COUNT} clean -f ${PROJECT_PATH} --log-file ${LOG_DIR}/${PROJECT_NAME}-clean.log
    check_and_fail $? "build_clean - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-clean.log
}

# build_source - Build source from a Project PATH
# Reference https://maven.apache.org/plugins/maven-source-plugin/
function build_source { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_source.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    mvn ${THREAD_COUNT} source:jar source:test-jar -f ${PROJECT_PATH} --log-file ${LOG_DIR}/${PROJECT_NAME}-build_source.log
    check_and_fail $? "build_source - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_source.log
}

# build_javadoc_jar - Build javadoc jar from a Project PATH
# Reference https://maven.apache.org/plugins/maven-javadoc-plugin/
function build_javadoc_jar { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_javadoc_jar.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    # intentionally not stopping on warnings... 
    mvn ${THREAD_COUNT} javadoc:jar javadoc:test-jar -f ${PROJECT_PATH} -DadditionalJOption=-Xdoclint:none --log-file ${LOG_DIR}/${PROJECT_NAME}-build_javadoc_jar.log
    check_and_fail $? "build_javadoc_jar - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_javadoc_jar.log
}

# build_javadoc_site - Build javadoc site aggregate from a Project PATH
# Reference https://maven.apache.org/plugins/maven-javadoc-plugin/
# 
# This function can take a long while (greater than 10 mintues)
# Add this to Travis - travis_wait 30 
function build_javadoc_site { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_javadoc_site.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    mvn ${THREAD_COUNT} javadoc:javadoc javadoc:test-javadoc -f ${PROJECT_PATH} -DadditionalJOption=-Xdoclint:none --log-file ${LOG_DIR}/${PROJECT_NAME}-build_javadoc_site.log
    check_and_fail $? "build_javadoc_jar - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_javadoc_site.log
}

# build_source_javadoc - Build javadoc, site and source aggregate from a Project PATH
# Reference https://maven.apache.org/plugins/maven-javadoc-plugin/
function build_source_javadoc { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    # running fixup. 
    mvn ${THREAD_COUNT} javadoc:fix javadoc:test-fix -f ${PROJECT_PATH} --log-file ${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc_fixup.log -DadditionalJOption=-Xdoclint:none -DfixTags=link
    check_and_fail $? "build_source_javadoc - fixup - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc_fixup.log

    # goals are combined to simplify the build 
    # should it be necesarry the above functions divide and conquer. 
    mvn ${THREAD_COUNT} source:jar source:test-jar javadoc:javadoc javadoc:test-javadoc javadoc:jar javadoc:test-jar -f ${PROJECT_PATH} -DadditionalJOption=-Xdoclint:none --log-file ${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc.log
    check_and_fail $? "build_source_javadoc - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc.log
}

# build_install - Build install from a Project PATH
# Reference https://maven.apache.org/plugins/maven-install-plugin/
function build_install { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_install.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    # clean and install (do both now)
    mvn ${THREAD_COUNT} clean install -f ${PROJECT_PATH} --log-file ${LOG_DIR}/${PROJECT_NAME}-build_install.log
    check_and_fail $? "build_install - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_install.log
}

# build_security_check - Security check from a Project PATH
# Reference https://github.com/victims/maven-security-versions
function build_security_check { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_security_check.log"

    PROJECT_PATH="$1"
    LOGS="${LOG_DIR}/${PROJECT_NAME}-build_security_check.log"
    echo "[Starting the check of security-versions] [`date`]"
    echo "- logging to ${LOGS}"
    mvn ${THREAD_COUNT} com.redhat.victims.maven:security-versions:check -f ${PROJECT_PATH} --log-file ${LOGS}
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

# tag_release - create a tag 
# Parameters: 
#   VERSION - MAJOR.MINOR.INCREMENTALVERSION
# Reference 
#   https://maven.apache.org/pom.html#Dependency_Version_Requirement_Specification
#   https://www.mojohaus.org/versions-maven-plugin/version-rules.html
#   https://cwiki.apache.org/confluence/display/MAVENOLD/Versioning
function tag_release { 
    announce "${FUNCNAME[0]}" "tag_release"

    VERSION="${1}"
    git tag "release-${VERSION}" -m "Releasing version ${VERSION} - ${BUILD_DATETIME}"
    git push --tags 
}

# set_version - set version per latest tag 
# Parameters: 
#   PROJECT
#   NEW_VERISON
#   LOGS
# Reference https://www.mojohaus.org/versions-maven-plugin/set-mojo.html
# 
# versions:revert - option to revert the change. 
function set_version { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/build_set_version-${2}.log"

    LOGS="${LOG_DIR}/build_set_version-${2}.log"
    PROJECT_PATH="$1"
    OLD_VERSION='*'

    # If we need build_numbers added, uncomment the next. 
    #BUILD_NUMBER="-"`date +%s`
    NEW_VERSION="${3}${BUILD_NUMBER}"
    #echo "[VERSION SET - START] - ${NEW_VERSION} - [`date`]"
    mvn ${THREAD_COUNT} versions:set -f ${PROJECT_PATH} --log-file ${LOGS} -DoldVersion=${OLD_VERSION} -DnewVersion=${NEW_VERSION}
    check_and_fail $? "set_version - ${PROJECT_PATH}"
}

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

# shortcut_doc_only_update - shortcuts the build if there are only files 
function shortcut_doc_only_update {
    echo "Checking if we need to shortcut"
}

# build_type - set the global build type
function build_type { 
    TYPE="${1}"
    echo "Checking Type - ${1}"
    TAGS=`git tag --list`
}

# diagnostic_details - output details
# - putting it into the build logs directory. 
function diagnostic_details {
    echo "Outputing diagnostic detail: "
    env > build/logs/diag.log
    df >> build/logs/diag.log
    ulimit -a >> build/logs/diag.log
}

# mvn_setup - setup for travis only 
function mvn_setup {
    if env | grep TRAVIS 
    then 
        # Not all JVMs support - -XX:MaxPermSize=512m 
        echo "MAVEN_OPTS='-Xmx2G -Xms1G -Djava.awt.headless=true '" > ~/.mavenrc
    fi
}

# check_and_fail - fails if the build is in a bad shape
function check_and_fail { 
    RC="${1}"
    if [ ${RC} == "0" ]
    then 
        echo "Success - [${2}]"
    else 
        # Output log on failure only 
        OUT_LOG="${3}"
        cat "${OUT_LOG}"

        echo "Fail -> METHOD [${2}]"
        exit -1;
    fi 
    
}

# announce - alerts to the start of a project
function announce { 
    PROJECT_NAME="${1}"
    LOG_NAME="${2}"
    
    echo "Starting - [${PROJECT_NAME}]"
    echo " -> logging out to file in '${LOG_NAME}'"

}

# header_line - output a line
function header_line { 
    echo "--------------------------------------------"
}

# regression - on jdks without creating yet another build. 
# - OpenJDK 8, OpenJDK 11 - from https://adoptopenjdk.net/.
#   Skipped - 'openjdk11' is the default in `.travis.yml`
# - IBM SDK, Java Technology Edition, Version 8. 8.0.5.40
#   JDKS_VERS='https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/baseline/baseline_version.txt'
#   JDKS_META='https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/meta/sdk/linux/x86_64/index.yml'
# 
# Reference -> https://github.com/michaelklishin/jdk_switcher
#
# colon is in the following so we can skip (if already done, or provide an unexpected outcome)
JDKS=('openjdk11:skip', 'openjdk8:skip', 'ibmjdk8:ibm' )
function regression { 
    echo "Starting the REGRESSION tests on specified jdks"
    
    FILE='/home/travis/ibm-java/ibm-java-archive.bin'

    for JDK in "${JDKS[@]}"
    do 
        JDK_INTERIM=`echo $JDK | sed 's|:| |g' | awk '{print $1}'`
        JDK_TYPE=`echo $JDK | sed 's|:| |g' | awk '{print $2}'`
    
        if [[ "${JDK_TYPE}" = "default" ]]
        then 
            echo "AdoptOpenJDK JDK -> ${JDK_INTERIM}"
            #jdk_switcher use $JDK_INTERIM
        elif [[ "${JDK_TYPE}" = "ibm" ]]
        then 
            echo "IBM JDK -> ${JDK_INTERIM}"
            
            # Setup Repsonse.properties
            echo "INSTALLER_UI=silent" > /home/travis/response.properties
            echo "USER_INSTALL_DIR=/home/travis/ibm-java/java80" >> /home/travis/response.properties
            echo "LICENSE_ACCEPTED=TRUE" >> /home/travis/response.properties
            
            chmod +x ${FILE}

            # setup directory and install 
            mkdir -p /home/travis/ibm-java/java80
            ${FILE} -i silent -f /home/travis/response.properties 
            
            export JAVA_HOME="${USER_INSTALL_DIR}"
            export PATH="${JAVA_HOME}/bin:${PATH}"
        fi
       
        header_line
    done 
}

# downloads the jdk 
# - SHA_SUM - the sha_sum in the trusted variable that is passed in here. 
# - DOWNLOAD_URI - uri
#
# Example:
# 1.8.0_sr5fp40:
#    uri: https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/8.0.5.40/linux/x86_64/ibm-java-sdk-8.0-5.40-x86_64-archive.bin
#    sha256sum: bc53faf476655e565f965dab3db37f9258bfc16bb8c5352c93d43d53860b79d3
# Originally from https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/meta/sdk/linux/x86_64/index.yml 
function download {
    announce "${FUNCNAME[0]}"

    SHA_SUM="$1"
    DOWNLOAD_URI="$2"
    
    # Create the directory
    mkdir -p /home/travis/ibm-java

    FILE='/home/travis/ibm-java/ibm-java-archive.bin'
    if [ -f "${FILE}" ]
    then 
        # check sha256sum in the cached file
        LOCAL_SHA_SUM="$(sha256sum ${FILE} | awk '{print $1}')"
        echo "ACTUAL_SHA_SUM -> ${LOCAL_SHA_SUM}"
        echo "EXPECTED_SHA_SUM -> ${SHA_SUM}"
        if [ "${SHA_SUM}" != "${LOCAL_SHA_SUM}" ]
        then
            curl -o ${FILE} "${DOWNLOAD_URI}"
        fi

        # make sure it's executable. 
        chmod +x ${FILE}

    else 
        curl -o ${FILE} "${DOWNLOAD_URI}"
    fi 

}

# comment_on_pull_request - adds a comment on the pull request.
# the token is encrypted following https://medium.com/@preslavrachev/using-travis-for-secure-building-and-deployment-to-github-5a97afcac113
function comment_on_pull_request {
    COMMENT_IN=$1

    # quick switch between github.com and ibm.com domains.
    BASE_URL="api.github.ibm.com"
    if [[ "${TRAVIS_APP_HOST}" == *"ibm.com"* ]]
    then
        BASE_URL="api.github.ibm.com"
    fi

    COMMENT="{\"body\": \"${COMMENT_IN}\"}"
    API_URL="https://${BASE_URL}/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/comments"

    if [[ "${TRAVIS_PULL_REQUEST_BRANCH}" != "" && "${TRAVIS_EVENT_TYPE}" == "pull_request" && "${TRAVIS_PULL_REQUEST}" != "false" ]]
    then
        curl -H "Authorization: token ${FHIR_GITHUB_TOKEN}" -X POST -d "${COMMENT}" "${API_URL}"
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
    BASE_URL="api.github.ibm.com"
    if [[ "${TRAVIS_APP_HOST}" == *"ibm.com"* ]]
    then
        BASE_URL="api.github.ibm.com"
    fi

    LABEL="{  \
            \"labels\": [ \
                ${BUILD_LABEL}
                ] \
        }"
    API_URL="https://${BASE_URL}/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/labels"

    if [[ "${TRAVIS_PULL_REQUEST_BRANCH}" != "" && "${TRAVIS_EVENT_TYPE}" == "pull_request" && "${TRAVIS_PULL_REQUEST}" != "false" ]]
    then
        curl -H "Authorization: token ${FHIR_GITHUB_TOKEN}" -H "UserAgent: ibm-fhir-cicd" -X POST -d "${LABEL}" "${API_URL}"
    fi
}

# comment_on_pull_request_with_log - adds a comment on the pull request.
# the token is encrypted following https://medium.com/@preslavrachev/using-travis-for-secure-building-and-deployment-to-github-5a97afcac113
function comment_on_pull_request_with_log {
    COMMENT_IN=$1

    # quick switch between github.com and ibm.com domains.
    BASE_URL="api.github.ibm.com"
    if [[ "${TRAVIS_APP_HOST}" == *"ibm.com"* ]]
    then
        BASE_URL="api.github.ibm.com"
    fi

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
        curl -H "Authorization: token ${FHIR_GITHUB_TOKEN}" -H "UserAgent: ibm-fhir-cicd" -X POST -d "${COMMENT}" "${API_URL}"
    fi
}

###############################################################################
# Setup for the script/build

# makes sure there is a gitignore which puts the log in this folder. 
GIT_IGNORE='build/.gitignore'
if [ ! -f ${GIT_IGNORE} ]
then 
    echo 'logs/' > ${GIT_IGNORE} 
fi

# create the logs directory (which is now ignored)
LOG_DIR='build/logs'
if [ ! -d  $LOG_DIR ]
then 
    mkdir -p ${LOG_DIR}
fi

# Threads that are allowed to run
# which are passed in with BUILD_THREAD
#
# Examples: 
#   1C (1Core)
#   4  (4 Threads)
THREAD_COUNT=
if [ ! -z "${BUILD_THREAD}" ]
then 
    THREAD_COUNT="-T ${BUILD_THREAD}"
fi

# Our original build was on JENKINS
# Jenkins made these variables part of the build context 
# Travis is a bit different, so we clean it up here. 
# - note publish.dir is also a valid 
if [ -z "${BUILD_ID}" ]
then 
    BUILD_ID="${TRAVIS_BUILD_ID}"
fi

if [ -z "${GIT_BRANCH}" ]
then 
    if echo "${TRAVIS_PULL_REQUEST_BRANCH}" | grep false 
    then 
        GIT_BRANCH="${TRAVIS_BRANCH}-"
    else 
        GIT_BRANCH="${TRAVIS_PULL_REQUEST_BRANCH}"
    fi
fi

if [ -z "${GIT_COMMIT}" ]
then 
    GIT_COMMIT="${TRAVIS_COMMIT}"
fi

if [ -z "${GIT_URL}" ]
then 
    # choosing to use the repo slug
    GIT_URL="${TRAVIS_REPO_SLUG}"
fi

if [ -z "${BUILD_DISPLAY_NAME}" ]
then 
    BUILD_DISPLAY_NAME="${TRAVIS_BUILD_ID}"
fi

###############################################################################
# Menu
ACTION="$2"
case $ACTION in
    setup) 
        mvn_setup
    ;;
    diagnostics) 
        diagnostic_details
        header_line
    ;;
    build_install) 
        PROJECT_POM_ARGS="$3"
        PROJECT_NAME="$4"
        build_install "${PROJECT_POM_ARGS}" "${PROJECT_NAME}"
        header_line
    ;;
    build_clean)
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_clean ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    set_version)
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        PROJECT_VERSION="$5"
        set_version ${PROJECT_POM} ${PROJECT_NAME} ${PROJECT_VERSION}
        header_line
    ;;
    build_security_check) 
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_security_check ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    build_javadoc) 
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_javadoc_jar ${PROJECT_POM} ${PROJECT_NAME}
        build_javadoc ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    build_source) 
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_source ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    build_source_javadoc)
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_source_javadoc ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    tag_release) 
        VERSION=$2
        tag_release ${VERSION}
    ;;
    assemble)
        # identify the projects which changed  
        files_changed_by_source_folder "${TRAVIS_BRANCH}" "${TRAVIS_PULL_REQUEST_BRANCH}"
        header_line
    ;;
    release) 
        echo "BIN TRAY"
    ;;
    sync_to_maven_central)
        echo "maven central"
    ;; 
    regression)
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        regression        
        build_clean "${PROJECT_POM}" "${PROJECT_NAME}"
        build_install "${PROJECT_POM}" "${PROJECT_NAME}"
        header_line
    ;;
    download) 
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        download "${PROJECT_POM}" "${PROJECT_NAME}"
        header_line
    ;;
    comment_on_pull_request) 
        comment_on_pull_request "${3}"
        header_line
    ;;
    comment_on_pull_request_with_log)
        comment_on_pull_request_with_log "${3}"
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