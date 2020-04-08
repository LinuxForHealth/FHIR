-- ##############################################################################
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-- ###############################################################################
CREATE USER fhirbatch;
CREATE USER fhirserver;
CREATE USER fhiradmin;
CREATE DATABASE fhirdb;
GRANT ALL PRIVILEGES ON DATABASE fhirdb TO fhiradmin;