#!/usr/bin/env sh
###############################################################################
# (C) Copyright IBM Corp. 2016, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

export LIBERTY_VERSION="22.0.0.12"

echo "
Executing $0 to deploy the fhir-server web application...
"

# Determine the location of this script.
# basedir=`dirname "$0"`
cd $(dirname $0); basedir="$(pwd)"

# Default liberty install location
UNZIP_LOC=`unset CDPATH; cd "$basedir/.." && pwd`
LIBERTY_INSTALL_DIR="${UNZIP_LOC}/liberty-runtime"

# Allow user to override default install location
if [ $# -gt 0 ]
then
    LIBERTY_INSTALL_DIR=$1
fi

echo "Deploying in location: ${LIBERTY_INSTALL_DIR}"

# Save the liberty home directory.
LIBERTY_ROOT=${LIBERTY_INSTALL_DIR}/wlp

if [ -d "$LIBERTY_ROOT" ]; then
    # If the liberty install directory exists, make sure we actually have Liberty installed there.
    if [ -f "${LIBERTY_ROOT}/bin/server" ]; then
        echo "Using the existing Liberty installation:"
        ${LIBERTY_ROOT}/bin/productInfo version
    else
        echo "
Invalid installation directory specified for Liberty runtime: ${LIBERTY_ROOT}
If the installation directory exists then it must contain a valid Liberty runtime."
        exit 1
    fi
else
    # If the liberty install directory doesn't exist, then create it.
    echo "Extracting the Liberty runtime... "
    unzip -qq ${basedir}/openliberty-runtime-${LIBERTY_VERSION}.zip -d ${LIBERTY_INSTALL_DIR}
    rc=$?
    if [ $rc != 0 ]; then
        echo "Error extracting liberty runtime: $rc"
        exit $rc
    else
        echo "done!"
    fi
fi

# Create the defaultServer if necessary.
if [ ! -d "${LIBERTY_ROOT}/usr/servers/defaultServer" ]; then
    echo "Creating the Liberty defaultServer... "
    ${LIBERTY_ROOT}/bin/server create defaultServer
    rc=$?
    if [ $rc != 0 ]; then
        echo "Error creating server definition: $rc"
        exit $rc
    fi
fi

# Copy our server assets
echo "Deploying fhir-server assets to the server runtime environment... "
cp -r ${basedir}/artifacts/* ${LIBERTY_ROOT}/usr/
rc=$?
if [ $rc != 0 ]; then
    echo "Error deploying fhir-server assets to server runtime environment: $rc"
    exit $rc
else
    echo "done!"
fi

echo "

The FHIR Server has been successfully deployed to the Liberty runtime
at: ${LIBERTY_ROOT}

The fhir-server application requires Java 11.
If you do not have one, a copy of the Java 11 SDK can be obtained at https://adoptium.net.
The following steps must be completed before the server can be started:

1) Set the JAVA_HOME environment variable for your Java installation.

2) If using a database other than the embedded derby one, make sure that your selected database
   is active and ready to accept requests.

3) Deploy the database schema via the fhir-persistence-schema cli jar under ${basedir}/tools
   and grant necessary permissions.

4) Modify the Liberty server config (server.xml) by adding/removing/modifying the XML snippets under
   ${LIBERTY_ROOT}/usr/servers/defaultServer/configDropins to configure datasource definitions, 
   TLS configuration (keystores), webapp security, and more.

5) Modify the FHIR server config (fhir-server-config.json) under
   ${LIBERTY_ROOT}/usr/servers/defaultServer/config to configure the persistence, resource endpoints,
   and related FHIR server features.

You can start and stop the server with these commands:
   ${LIBERTY_ROOT}/bin/server start
   ${LIBERTY_ROOT}/bin/server stop
"