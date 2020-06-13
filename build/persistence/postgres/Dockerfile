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
ENV LANG en_US.utf8

# Create a working data directory
RUN mkdir -p /db/data && chown -R postgres:postgres /db
VOLUME /db/data
WORKDIR /db

# Open up the port
EXPOSE 5432

# Load Build Information
COPY --chown=postgres:postgres resources/db.sql /docker-entrypoint-initdb.d/db.sql
COPY --chown=postgres:postgres resources/docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod 755 /docker-entrypoint.sh

#USER postgres
#CMD ["postgres"]

ENTRYPOINT ["/docker-entrypoint.sh"]