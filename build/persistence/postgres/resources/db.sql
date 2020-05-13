-- ##############################################################################
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-- ###############################################################################

-- Create the roles used
CREATE ROLE fhirbatch;
CREATE ROLE fhirserver;
CREATE ROLE fhiradmin;

-- Create the users
CREATE USER fhirbatch with login encrypted password 'change-password' IN ROLE fhirbatch;
CREATE USER fhirserver with login encrypted password 'change-password' IN ROLE fhirserver;
CREATE USER fhiradmin with login encrypted password 'change-password' IN ROLE fhiradmin;

CREATE DATABASE FHIRDB;

CONNECT TO FHIRDB;
CREATE SCHEMA FHIRDATA;
CREATE SCHEMA FHIR_ADMIN;
CREATE SCHEMA FHIR_OAUTH;
TERMINATE;

-- EOF