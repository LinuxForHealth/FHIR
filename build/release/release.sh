
set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# deploys the binaries to bintray

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
# Function Declarations:

# deploy_bintray - executes mvn with a set of goals
function deploy_bintray { 
    announce "${FUNCNAME[0]}"
    PROJECT_PATH="$1"
    PROFILES="-Pdeploy-bintray,fhir-javadocs"
    TYPE="${2}"

    mvn ${THREAD_COUNT} -ntp -B ${PROFILES} deploy -f ${PROJECT_PATH} -Dbintray.repo=ibm-fhir-server-${TYPE} -DskipTests -s build/release/.m2/settings.xml
    check_and_fail $? "${FUNCNAME[0]} - stopped - ${PROJECT_PATH}"
}

# deploy_via_curl - uploads each artifact via curl
function deploy_via_curl { 
    TYPE="${1}"
    # Upload to BinTray
    for PROJ in `find . -type d -maxdepth 1 | grep -v '.git' | grep -v 'build' | grep -v '/docs' | sed 's|.\/||g' | grep -v '\.' `
    do 
        echo "PROJECT: ${PROJ}"
        if [ -d "${PROJ}/target" ]
        then
            # Upload SOURCES Jar
            SOURCES_JAR=`find ${PROJ}/target -iname "*${BUILD_VERSION}-sources.jar" -maxdepth 1 -exec basename {} \;`
            if [ ! -z "${SOURCES_JAR}" ]
            then 
                echo " - Uploading jar: ${SOURCES_JAR}"
                FILE_TARGET_PATH="/com/ibm/fhir/${PROJ}/${BUILD_VERSION}/${PROJ}-${BUILD_VERSION}-sources.jar"
                STATUS=$(curl -T "${PROJ}/target/${SOURCES_JAR}" -u${BINTRAY_USERNAME}:${BINTRAY_PASSWORD} -H "X-Bintray-Package:${PROJ}" -H "X-Bintray-Version:${BUILD_VERSION}" https://api.bintray.com/content/ibm-watson-health/ibm-fhir-server-${TYPE}${FILE_TARGET_PATH} -o /dev/null -w '%{http_code}')
                echo "${STATUS} - Done uploading jar file to ${FILE_TARGET_PATH}"
            fi
            
            # Upload JAVADOC Jar
            JAVADOC_JAR=`find ${PROJ}/target -iname "*${BUILD_VERSION}-javadoc.jar" -maxdepth 1 -exec basename {} \;`
            if [ ! -z "${JAVADOC_JAR}" ]
            then 
                echo " - Uploading jar: ${JAVADOC_JAR}"
                FILE_TARGET_PATH="/com/ibm/fhir/${PROJ}/${BUILD_VERSION}/${PROJ}-${BUILD_VERSION}-javadoc.jar"
                STATUS=$(curl -T "${PROJ}/target/${JAVADOC_JAR}" -u${BINTRAY_USERNAME}:${BINTRAY_PASSWORD} -H "X-Bintray-Package:${PROJ}" -H "X-Bintray-Version:${BUILD_VERSION}" https://api.bintray.com/content/ibm-watson-health/ibm-fhir-server-${TYPE}${FILE_TARGET_PATH} -o /dev/null -w '%{http_code}')
                echo "${STATUS} - Done uploading jar file to ${FILE_TARGET_PATH}"
            fi

            # Upload tests Jar
            TESTS_JAR=`find ${PROJ}/target -iname "*${BUILD_VERSION}-tests.jar" -maxdepth 1 -exec basename {} \;`
            if [ ! -z "${TESTS_JAR}" ]
            then 
                echo " - Uploading jar: ${TESTS_JAR}"
                FILE_TARGET_PATH="/com/ibm/fhir/${PROJ}/${BUILD_VERSION}/${PROJ}-${BUILD_VERSION}-tests.jar"
                STATUS=$(curl -T "${PROJ}/target/${TESTS_JAR}" -u${BINTRAY_USERNAME}:${BINTRAY_PASSWORD} -H "X-Bintray-Package:${PROJ}" -H "X-Bintray-Version:${BUILD_VERSION}" https://api.bintray.com/content/ibm-watson-health/ibm-fhir-server-${TYPE}${FILE_TARGET_PATH} -o /dev/null -w '%{http_code}')
                echo "${STATUS} - Done uploading jar file to ${FILE_TARGET_PATH}"
            fi

            for JAR_FILE in `find ${PROJ}/target -maxdepth 1 -not -name '*-tests.jar' -and -not -name '*-javadoc.jar' -and -not -name '*-sources.jar' -and -not -name '*orginal*.jar' -and -name '*.jar' -exec basename {} \;`
            do 
                echo " - Uploading jar: ${JAR_FILE}"
                FILE_TARGET_PATH="/com/ibm/fhir/${PROJ}/${BUILD_VERSION}/${JAR_FILE}"
                STATUS=$(curl -T "${PROJ}/target/${JAR_FILE}" -u${BINTRAY_USERNAME}:${BINTRAY_PASSWORD} -H "X-Bintray-Package:${PROJ}" -H "X-Bintray-Version:${BUILD_VERSION}" https://api.bintray.com/content/ibm-watson-health/ibm-fhir-server-${TYPE}${FILE_TARGET_PATH} -o /dev/null -w '%{http_code}')
                echo "${STATUS} - Done uploading jar file to ${FILE_TARGET_PATH}"
            done

            # The general zip FILE logic is changed to do fhir-validation-distribution.zip and fhir-cli.zip only
            for ZIP_FILE in `find ${PROJ}/target -name fhir-validation-distribution.zip -or -name fhir-cli.zip -maxdepth 1`
            do 
               ZIP_FILE=`basename ${ZIP_FILE}`
               echo " - Uploading zip: ${ZIP_FILE}"
               FILE_TARGET_PATH="/com/ibm/fhir/${PROJ}/${BUILD_VERSION}/${ZIP_FILE}"
               STATUS=$(curl -T "${PROJ}/target/${ZIP_FILE}" -u${BINTRAY_USERNAME}:${BINTRAY_PASSWORD} -H "X-Bintray-Package:${PROJ}" -H "X-Bintray-Version:${BUILD_VERSION}" https://api.bintray.com/content/ibm-watson-health/ibm-fhir-server-${TYPE}${FILE_TARGET_PATH} -o /dev/null -w '%{http_code}')
               echo "${STATUS} - Done uploading zip file to ${FILE_TARGET_PATH}"
               if [ "${STATUS}" == "413" ]
               then 
                   # File is too big (over 300M)
                   exit -413
               fi
            done

            # Upload the POM file
            for POM_FILE in `find ${PROJ}/ -name 'pom.xml' -maxdepth 1 -exec basename {} \;`
            do 
                echo " - Uploading pom: ${POM_FILE}"
                FILE_TARGET_PATH="/com/ibm/fhir/${PROJ}/${BUILD_VERSION}/${PROJ}-${BUILD_VERSION}.pom"
                STATUS=$(curl -T "${PROJ}/${POM_FILE}" -u${BINTRAY_USERNAME}:${BINTRAY_PASSWORD} -H "X-Bintray-Package:${PROJ}" -H "X-Bintray-Version:${BUILD_VERSION}" https://api.bintray.com/content/ibm-watson-health/ibm-fhir-server-${TYPE}${FILE_TARGET_PATH} -o /dev/null -w '%{http_code}')
                echo "${STATUS} - Done uploading pom file to ${FILE_TARGET_PATH}"
            done
        fi
    done
}

###############################################################################
# check to see if mvn exists
if which mvn | grep -i mvn
then 
    debugging 'mvn is found!'
else 
    warn 'mvn is not found!'
fi

#RELEASE_CANDIDATE or RELEASE or SNAPSHOT or EXISTING
case $BUILD_TYPE in
    RELEASE_CANDIDATE) 
        TYPE="snapshots"
        deploy_via_curl "${TYPE}"
        header_line
    ;;
    RELEASE) 
        TYPE="releases"
        deploy_via_curl "${TYPE}"
        header_line
    ;;
    SNAPSHOT) 
        info "SNAPSHOT build is not set"
        header_line
    ;;
    EXISTING)
        info "EXISTING build is not set"
        header_line
    ;;
    *)
        warn "invalid function called, dropping through "
    ;;
esac

# Synch to Maven Central 

# EOF
