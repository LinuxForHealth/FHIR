#!/usr/bin/env bash

set -o errexit
set -o nounset
set -Eeuo pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# From Postgres https://github.com/docker-library/postgres/blob/master/docker-entrypoint.sh
# while not used right now, there are some useful features. 
source /docker-entrypoint.sh

# Check and create the database if there is no data
create_data_directory() {
    if [ ! -f /fhir/data/pg_hba.conf ]
    then 
        su - postgres -c "/usr/lib/postgresql/12/bin/initdb -D /fhir/data"
    fi
}

# Start it up
start_and_wait() {
    echo "[Postgres] - Starting"
    su postgres -c "pg_ctl -D /fhir/data --wait --timeout=120 start"
    RC=$?
    if [ "${RC}" == "0" ]
    then
        echo "[Postgres] - Start Success"
    else 
        echo "[Postgres] - Start Failed - [${RC}]"
    fi
}

##### 

create_data_directory

start_and_wait

while sleep 120; do echo heartbeat; done
# End of File