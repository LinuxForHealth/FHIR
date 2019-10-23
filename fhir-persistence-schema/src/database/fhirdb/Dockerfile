#
# (C) Copyright IBM Corp. 2019
#
# SPDX-License-Identifier: Apache-2.0
#

FROM ibmcom/db2

# Create a non-admin user for the FHIR server
RUN groupadd -g 1002 fhir
RUN useradd -u 1002 -g fhir -M -d /database/config/fhiruser fhiruser 
RUN echo "change-password" | passwd --stdin fhiruser

# Indicate that we expect to connect to the DB2 service on port 50001
EXPOSE 50001

# Set up the database configuration and build to be run by the main DB2 container configuration
# per https://hub.docker.com/r/ibmcom/db2
RUN mkdir /var/custom
COPY create-database.sh /var/custom
RUN chmod a+x /var/custom/create-database.sh
