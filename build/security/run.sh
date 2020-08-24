#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Runs through the SCANNERS

# Menu system for each scan
function scan_it { 
    SCANNER_IT=$1
    case "${SCANNER_IT}" in
        sonarcube)
            bash build/security/sonarcube.sh
            ;;
        dockle)
            bash build/security/dockle.sh
            ;;
        asoc)
            bash build/security/asoc.sh
            ;;
        trivy)
            bash build/security/trivy.sh
            ;;
        contrast)
            bash build/security/contrast.sh
            ;;
        *)
            echo $"Usage: $0 {sonarcube|dockle|asoc|trivy|contrast}"
        esac
}

# Menu
if [ -z "${WORKSPACE}" ]
then 
    echo 'Export Variable is not set "${WORKSPACE}"'
else
    if [ -z "${SCANNERS}" ]
    then 
        echo 'SCANNERS Variable is not set "${SCANNERS}"'
    else
        # Only run if scanner and workplace are set
        # Quick conversion to an array
        declare -a SCANNERS_ARRAY=($SCANNERS)

        pushd `pwd`
        cd ${WORKSPACE}
        echo "Starting the run"
        for SCANNER in ${SCANNERS}
        do 
            scan_it ${SCANNER}
        done

        popd `pwd`
        echo "Finished the setup"
    fi
fi
# EOF