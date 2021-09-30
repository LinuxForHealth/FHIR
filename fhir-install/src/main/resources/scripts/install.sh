#!/usr/bin/env sh
###############################################################################
# (C) Copyright IBM Corp. 2016, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

export LIBERTY_VERSION="21.0.0.9"

echo "
Executing $0 to deploy the fhir-server web application...
"

# Determine the location of this script.
# basedir=`dirname "$0"`
cd $(dirname $0); basedir="$(pwd)/"

# Default liberty install location
UNZIP_LOC=`unset CDPATH; cd "$basedir/.." && pwd`
LIBERTY_INSTALL_DIR="${UNZIP_LOC}/liberty-runtime"

# Allow user to override default install location
if [ $# -gt 0 ]
then
    LIBERTY_INSTALL_DIR=$1
fi

echo "Deploying fhir-server in location: ${LIBERTY_INSTALL_DIR}"

# If the liberty install directory doesn't exist, then create it.
if [ ! -d "$LIBERTY_INSTALL_DIR" ]; then
    echo -n "
The Liberty installation directory does not exist; will attempt to create it... "
    mkdir -p $LIBERTY_INSTALL_DIR
    rc=$?
    if [ $rc != 0 ]; then
    echo "Error creating installation directory: $rc"
    exit $rc
    else
    echo "done!"
    fi
fi

# Unzip liberty runtime zip
echo -n "
Extracting the Liberty runtime... "
unzip -qq ${basedir}/server-runtime/openliberty-runtime-${LIBERTY_VERSION}.zip -d ${LIBERTY_INSTALL_DIR}
rc=$?
if [ $rc != 0 ]; then
    echo "Error extracting liberty runtime: $rc"
    exit $rc
else
    echo "done!"
fi

# Save the liberty home directory.
LIBERTY_ROOT=${LIBERTY_INSTALL_DIR}/wlp

# Determine java command to be used.
if [ -z "$JAVA_HOME" ]; then
    echo "
Warning: JAVA_HOME not set; Java 8 or above is required for proper execution."
fi

# Create our server
echo -n "
Creating Liberty server definition for fhir-server... "
${LIBERTY_ROOT}/bin/server create fhir-server
rc=$?
if [ $rc != 0 ]; then
    echo "Error creating server definition: $rc"
    exit $rc
else
    echo "done!"
fi

# Copy our server assets
echo -n "
Deploying fhir-server assets to server runtime environment... "
cp -pr ${basedir}/artifacts/* ${LIBERTY_ROOT}/usr/
rc=$?
if [ $rc != 0 ]; then
    echo "Error deploying fhir-server assets to server runtime environment: $rc"
    exit $rc
else
    echo "done!"
fi


echo "

The FHIR Server has been successfully deployed to the
Liberty runtime located at: ${LIBERTY_ROOT}

The following manual steps must be completed before the server can be started:

1) Make sure that your selected database (e.g. Derby, DB2) is active and
   ready to accept requests.

2) Modify the server.xml and fhir-server-config.json files located at
   ${LIBERTY_ROOT}/usr/servers/fhir-server to properly configure the server according
   to your requirements.
   This includes the definition of the ports, the configuration
   of datastore(s), and other associated configuration.

3) The fhir-server application requires Java 8 or above.
   If you do not have one, a copy of the Java 8 SDK can be obtained at https://adoptopenjdk.net.
   Be sure to set the JAVA_HOME environment variable to point to your Java 8 installation
   before starting the server:
       export JAVA_HOME=${LIBERTY_ROOT}/ibm-java-x86_64-80

4) You can start and stop the server with these commands:
   ${LIBERTY_ROOT}/bin/server start fhir-server
   ${LIBERTY_ROOT}/bin/server stop fhir-server
"

exit 0
