#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# The script enables the uploading source and javadoc to artifactory resources. 
# **** Don't forget to publish afterwards. ****

# Output should look like: 
# PROJECT: fhir-notification-websocket
# - Uploading jar: fhir-notification-websocket/target/fhir-notification-websocket-4.0.0-sources.jar
# {"message":"success"}
# - Uploading jar: fhir-notification-websocket/target/fhir-notification-websocket-4.0.0-javadoc.jar
# {"message":"success"}

BINTRAY_USER="<REPLACE_ME>"
BINTRAY_API_KEY="<REPLACE_ME>"
BINTRAY_VERSION_NAME="4.0.0"

# Switch to the Release Version 
mvn -Pdeploy-version-release clean -f fhir-examples/pom.xml
mvn -Pdeploy-version-release clean -f fhir-tools/pom.xml
mvn -Pdeploy-version-release clean -f fhir-parent/pom.xml -N

# install examples
mvn -Pdeploy-version-release install -f fhir-examples/pom.xml

# Create source and javadoc
mvn install source:jar javadoc:jar -f fhir-parent/ -DskipTests

# Upload to BinTray
for PROJ in `find . -type d -maxdepth 1 | grep -v '.git' | grep -v 'build' | grep -v '/docs' | sed 's|.\/||g' | grep -v '\.'`
do 
    echo "PROJECT: ${PROJ}"

    # Upload SOURCES Jar
    SOURCES_JAR=`find ${PROJ} -iname "*${BINTRAY_VERSION_NAME}-sources.jar"`
    if [ ! -z "${SOURCES_JAR}" ]
    then 
        echo " - Uploading jar: ${SOURCES_JAR}"
        FILE_TARGET_PATH="/com/ibm/fhir/${PROJ}/${BINTRAY_VERSION_NAME}/${PROJ}-${BINTRAY_VERSION_NAME}-sources.jar"
        curl -T ${SOURCES_JAR} -u${BINTRAY_USER}:${BINTRAY_API_KEY} -H "X-Bintray-Package:${PROJ}" -H "X-Bintray-Version:${BINTRAY_VERSION_NAME}" https://api.bintray.com/content/ibm-watson-health/ibm-fhir-server-releases${FILE_TARGET_PATH}
        echo ""
    fi
    
    # Upload JAVADOC Jar
    JAVADOC_JAR=`find ${PROJ} -iname "*${BINTRAY_VERSION_NAME}-javadoc.jar"`
    if [ ! -z "${JAVADOC_JAR}" ]
    then 
        echo " - Uploading jar: ${JAVADOC_JAR}"
        FILE_TARGET_PATH="/com/ibm/fhir/${PROJ}/${BINTRAY_VERSION_NAME}/${PROJ}-${BINTRAY_VERSION_NAME}-javadoc.jar"
        curl -T ${SOURCES_JAR} -u${BINTRAY_USER}:${BINTRAY_API_KEY} -H "X-Bintray-Package:${PROJ}" -H "X-Bintray-Version:${BINTRAY_VERSION_NAME}" https://api.bintray.com/content/ibm-watson-health/ibm-fhir-server-releases${FILE_TARGET_PATH}
        echo ""
    fi
done

# End of Script