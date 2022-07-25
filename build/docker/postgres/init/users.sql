-- ##############################################################################
-- (C) Copyright IBM Corp. 2021
--
-- SPDX-License-Identifier: Apache-2.0
-- ###############################################################################

-- Create the users
CREATE USER fhirserver WITH LOGIN encrypted password 'change-password';
CREATE USER fhirbatch  WITH LOGIN encrypted password 'change-password';

GRANT CONNECT ON DATABASE fhirdb TO fhirserver;
GRANT CONNECT ON DATABASE fhirdb TO fhirbatch;