#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

export LIBERTY_VERSION="22.0.0.7"

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
The Liberty installation directory does not exist; attempting to create it... "
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
unzip -qq ${basedir}/openliberty-runtime-${LIBERTY_VERSION}.zip -d ${LIBERTY_INSTALL_DIR}
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
Warning: JAVA_HOME not set; Java 11 or above is required for proper execution."
fi

# Create our server
echo -n "
Creating the Liberty defaultServer... "
${LIBERTY_ROOT}/bin/server create defaultServer
rc=$?
if [ $rc != 0 ]; then
    echo "Error creating server definition: $rc"
    exit $rc
else
    echo "done!"
fi

# Copy our server assets
echo -n "
Deploying fhir-server assets to the server runtime environment... "
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

1) The fhir-server application requires Java 11.
   If you do not have one, a copy of the Java 11 SDK can be obtained at https://adoptium.net.
   Set the JAVA_HOME environment variable to your Java installation
   before starting the server.

2) Make sure that your selected database (e.g. PostgreSQL) is active and
   ready to accept requests.

3) Deploy the schema via the fhir-persistence-schema cli jar under %BASEDIR%\tools
   and grant necessary permissions.

4) Modify the Liberty server config (server.xml) by adding/removing/modifying the xml snippets under
   ${LIBERTY_ROOT}/usr/servers/defaultServer/configDropins to configure datasource definitions, 
   TLS configuration (keystores), authentication, and more.

5) Modify the FHIR server config (fhir-server-config.json) under
   ${LIBERTY_ROOT}/usr/servers/defaultServer/config to configure the persistence, resource endpoints,
   and related FHIR server features.

You can start and stop the server with these commands:
   ${LIBERTY_ROOT}/bin/server start
   ${LIBERTY_ROOT}/bin/server stop
"

exit 0
