#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

source release.properties

# labels messages as debug
function debugging {  
    if [ ! -z "${DEBUG}" ]
    then 
        echo "[DEBUG]: ${1}" 
    fi
}

# labels messages as info
function info {  
    echo "[INFO]: ${1}" 
}

# labels messages as info
function warn {  
    echo "[WARN]: ${1}" 
}

# header_line - output a line
function header_line { 
    echo "--------------------------------------------"
}

# diagnostic_details - output details relevant to the build environment
function diagnostic_details {
    header_line
    debugging "Output diagnostic details:"
    header_line
    debugging $'Environmental Settings: \n'"$(env)"
    header_line
    debugging $'Disk Free: \n'"$(df)"
    header_line
    debugging $'ulimit settings: \n'"$(ulimit -a)"
    header_line
}

# announce - alerts to the start of a project
# used in concert with long running task
function announce { 
    PROJECT_NAME="${1}"
    info "[${PROJECT_NAME}] - start"
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

        if [ ! -z "${OUT_LOG}" ]
        then 
            if [ -f ${OUT_LOG} ]
            then 
                cat "${OUT_LOG}"
            fi 
        fi 

        echo "Fail -> METHOD [${2}]"
        exit -1;
    fi 
    
}

# EOF