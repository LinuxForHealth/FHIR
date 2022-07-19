#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

echo "
Executing $0 to deploy the fhir-server web application...
"

# Determine the location of this script.
cd $(dirname $0); basedir="$(pwd)/"

# Default liberty install location
UNZIP_LOC=`unset CDPATH; cd "$basedir/.." && pwd`
unset WLP_ROOT

# Allow user to override default install location
if [ $# -gt 0 ]; then
    WLP_ROOT=$1
else
    echo "Syntax: $0 <WLP_ROOT>"
    exit 1
fi

echo "Deploying fhir-server in existing Liberty runtime location: ${WLP_ROOT}"

# Validate the WLP root directory.
if [ ! -d "${WLP_ROOT}" ]; then
    echo -n "
The specified Liberty installation directory does not exist: ${WLP_ROOT}
Please specify the location of an existing WLP runtime. "
    exit 2
fi

# Make sure we actually have Liberty installed there.
if [ ! -f "${WLP_ROOT}/bin/server" ]; then
    echo -n "
Invalid installation directory specified for Liberty runtime: ${WLP_ROOT}
Please specify the location of an existing WLP runtime."
    exit 3
fi

# Create the defaultServer if necessary.
if [ ! -d "${WLP_ROOT}/usr/servers/defaultServer" ]; then
    echo -n "
Creating the Liberty defaultServer... "
    ${WLP_ROOT}/bin/server create defaultServer
    rc=$?
    if [ $rc != 0 ]; then
        echo "Error creating server definition: $rc"
        exit $rc
    else
        echo "done!"
    fi
fi

# Copy our server assets
echo -n "
Deploying fhir-server assets to the server runtime environment... "
cp -pr ${basedir}/artifacts/servers/defaultServer/* ${WLP_ROOT}/usr/servers/defaultServer/
rc=$?
if [ $rc != 0 ]; then
    echo "Error deploying fhir-server assets to server runtime environment: $rc"
    exit $rc
else
    echo "done!"
fi

# Copy our db drivers
echo -n "
Deploying database drivers to the server runtime environment... "
cp -pr ${basedir}/artifacts/shared/* ${WLP_ROOT}/usr/shared/
rc=$?
if [ $rc != 0 ]; then
    echo "Error deploying db drivers to server runtime environment: $rc"
    exit $rc
else
    echo "done!"
fi


echo "

The FHIR Server has been successfully deployed to the
existing Liberty runtime located at: ${WLP_ROOT}

1) You can start and stop the server with these commands:
   ${WLP_ROOT}/bin/server start
   ${WLP_ROOT}/bin/server stop
2) The FHIR Server User's Guide can be found at https://github.com/LinuxForHealth/FHIR.
"

exit 0
