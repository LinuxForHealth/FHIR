#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -o errexit
set -o nounset
set -Eeuo pipefail

# From Postgres https://github.com/docker-library/postgres/blob/master/docker-entrypoint.sh
# while not used right now, there are some useful features. 
if [ -f /docker-entrypoint.sh ]
then 
    source /docker-entrypoint.sh
fi

# Check and create the database if there is no data
create_data_directory() {
    if [ ! -f /db/data/pg_hba.conf ]
    then 
        su - postgres -c "/usr/local/bin/initdb -D /db/data"
        # Let it listen on all ports
        sed -ri "s!^#?(listen_addresses)\s*=\s*\S+.*!\1 = '*'!" /db/data/postgresql.conf
        # Performance Tuning
        sed -i -e"s/^max_connections = 100.*$/max_connections = 100/" /db/data/postgresql.conf
        sed -i -e"s/^shared_buffers =.*$/shared_buffers = 4GB/" /db/data/postgresql.conf
        sed -i -e"s/^#effective_cache_size = 128MB.*$/effective_cache_size = 2GB/" /db/data/postgresql.conf
        sed -i -e"s/^#work_mem = 1MB.*$/work_mem = 1MB/" /db/data/postgresql.conf
        sed -i -e"s/^#maintenance_work_mem = 16MB.*$/maintenance_work_mem = 512MB/" /db/data/postgresql.conf
        sed -i -e"s/^#checkpoint_segments = .*$/checkpoint_segments = 32/" /db/data/postgresql.conf
        sed -i -e"s/^#checkpoint_completion_target = 0.5.*$/checkpoint_completion_target = 0.7/" /db/data/postgresql.conf
        sed -i -e"s/^#wal_buffers =.*$/wal_buffers = 16MB/" /db/data/postgresql.conf
        sed -i -e"s/^#default_statistics_target = 100.*$/default_statistics_target = 100/" /db/data/postgresql.conf
    fi
}

# Start it up
start_and_wait() {
    echo "[Postgres] - Starting"
    su postgres -c "pg_ctl -D /db/data --wait --timeout=120 start"
    RC=$?
    if [ "${RC}" == "0" ]
    then
        echo "[Postgres] - Start Success"
    else 
        echo "[Postgres] - Start Failed - [${RC}]"
    fi
}

# Trap the to create a clean shutdown.
trap clean_shutdown SIGINT SIGTERM SIGKILL
clean_shutdown() {
    echo "Shutting down postgres"
    su postgres -c "pg_ctl -D /db/data --wait --timeout=120 stop"
    trap - SIGINT SIGTERM SIGKILL
}

##########################################################################################
# 
create_data_directory

#start_and_wait

# We're up
#while true
#do 
#    sleep 60
# done 
# EOF
##########################################################################################