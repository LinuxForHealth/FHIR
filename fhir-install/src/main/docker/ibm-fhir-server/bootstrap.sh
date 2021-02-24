#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

set -e -o pipefail

# When debugging -x is an accepted practice, when not debugging, please comment out.
# set -x

# The full path to the directory of this script, no matter where its called from
# DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

##############################################################################
# The global variables used are: 

SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"
#FHIR_PERSISTENCE_SCHEMA_CLI_LOCATION="/opt/fhirserver"

PERFORM_BOOTSTRAP_DB=${BOOTSTRAP_DB}
[ -z "${BOOTSTRAP_DB}" ] && PERFORM_BOOTSTRAP_DB="false"

# info - labels messages as info
function info {  
    echo "${SCRIPT_NAME} - [INFO]: $(date +"%Y-%m-%d_%T") - ${1}" 
}

# error_warn - labels messages as error
function error_warn {  
    echo "${SCRIPT_NAME} - [ERROR]: $(date +"%Y-%m-%d_%T") - ${1}" 
}

##############################################################################
# The logic is activated in the following calls:

info "PERFORM_BOOTSTRAP_DB is ${PERFORM_BOOTSTRAP_DB}"

## TODO: CALL CLI tool
#    DIR/path to cli
## DISABLE the bootstrap done within server...
### Call createSchema/updateSchema db_type derby  

# Run the IBM FHIR Server
/opt/ol/wlp/bin/server run fhir-server

# EOF