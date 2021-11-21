#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

echo "Starting AppScan Preparation" 

mkdir -p ${WORKSPACE}/build/security/logs/tmp
mkdir -p ${WORKSPACE}/build/security/logs/output/

# Find the Jars, Ignore Tests and Implementation Guides (ig) and uber jars
find ${WORKSPACE} -iname 'fhir-*.jar' -not -iname 'fhir*-tests.jar' -not -iname 'fhir*-test-*.jar' -not -iname 'fhir-persistence-schema-*-cli.jar' -not -iname 'fhir-swagger-generator-*-cli.jar' \
  -not -iname 'fhir-examples-*.jar' -not -name 'fhir-bulkdata-webapp-*-client.jar' -not -iname 'fhir*-ig-*.jar' -not -iname 'fhir-bucket-*-cli.jar' -not -path '*/target/fhir-server-webapp-*' \
  -not -iname 'fhir-operation-cqf-*-shaded.jar' -not -iname 'fhir-operation-cpg-*-shaded.jar' -not -iname 'fhir-term-graph-loader-*-cli.jar' \
  -not -path '*/target/fhir-bulkdata*' -exec cp -f {} ${WORKSPACE}/build/security/logs/tmp \;

cd ${WORKSPACE}/build/security/logs/

# Generate the configuration file for each of the fhir projects in the tmp folder 
echo '<?xml version="1.0" encoding="UTF-8" standalone="no"?>' > ${WORKSPACE}/build/security/logs/appscan-config.xml
echo '<Configuration thirdParty="false">' >> ${WORKSPACE}/build/security/logs/appscan-config.xml
echo '  <Targets>' >> ${WORKSPACE}/build/security/logs/appscan-config.xml

for NAME in `find ${WORKSPACE}/build/security/logs/tmp -iname '*.jar'`
do 
  echo "<Target path=\"${NAME}\"></Target>" >> ${WORKSPACE}/build/security/logs/appscan-config.xml
done

echo '  </Targets>' >> ${WORKSPACE}/build/security/logs/appscan-config.xml
echo '</Configuration>' >> ${WORKSPACE}/build/security/logs/appscan-config.xml

# Setup and Configure Static Analyzer
if [ ! -f SAClientUtil.zip ]
then 
  curl -L -o SAClientUtil.zip "https://cloud.appscan.com/api/SCX/StaticAnalyzer/SAClientUtil?os=linux" -o /dev/null
  unzip -o -qq SAClientUtil.zip
  chmod -R +x ./SAClientUtil*/
fi

export PATH=`pwd`/SAClientUtil*/bin/:$PATH

# Quick Check of the Secrets
if [ -z "${APPSCAN_SECRET}" ]
then 
  echo "APPSCAN_SECRET must be set"
  exit -1
fi

if [ -z "${APPSCAN_KEY}" ]
then 
  echo "APPSCAN_SECRET must be set"
  exit -1
fi

if [ -z "${APPSCAN_APPID}" ]
then 
  echo "APPSCAN_APPID must be set"
  exit -1
fi

# Prepare appscan.sh 
APPSCAN_SCAN_NAME="ibm-fhir-server"
SAClientUtil*/bin/appscan.sh prepare -c ${WORKSPACE}/build/security/logs/appscan-config.xml -n "$APPSCAN_SCAN_NAME.irx"

export JAVA_TOOL_OPTIONS=""
# Login
SAClientUtil*/bin/appscan.sh api_login -P ${APPSCAN_SECRET} -persist -u ${APPSCAN_KEY}

# queue analysis 
SAClientUtil*/bin/appscan.sh queue_analysis -a ${APPSCAN_APPID} -f "$APPSCAN_SCAN_NAME.irx" -n $APPSCAN_SCAN_NAME 

# Copy over to output folder
cp -f $APPSCAN_SCAN_NAME.irx ${WORKSPACE}/build/security/logs/output/
# EOF
