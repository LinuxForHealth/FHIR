###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

FROM postgres:12.2-alpine

# Hard Coded environment values
ENV POSTGRES_DB fhirdb
ENV POSTGRES_USER FHIRSERVER
ENV PGDATA /db/data

# Create a working data directory
RUN mkdir -p /db/data && chown -R postgres:postgres /db
VOLUME /db/data
WORKDIR /db

# Open up the port
EXPOSE 5432

# Load Configuration Steps
COPY --chown=postgres:postgres resources/init-user-db.sh /docker-entrypoint-initdb.d/init-user-db.sh
COPY --chown=postgres:postgres resources/db.sql /docker-entrypoint-initdb.d/db.sql

# Startup using the Entrypoint
COPY ./resources/entrypoint.sh /
RUN chmod +x /entrypoint.sh
RUN /entrypoint.sh

USER postgres
CMD ["postgres"]