-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
-- Create the row and array types. Needs execute immediate so that it can be
-- run from liquibase in a non-default schema
-------------------------------------------------------------------------------
BEGIN

    -- ----------------------------------------------------------------------------
    -- ROW and ARRAY types for str parameter values
    -- ----------------------------------------------------------------------------
    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_str_values AS ROW (parameter_name_id INTEGER, str_value VARCHAR(511 OCTETS), str_value_lcase   VARCHAR(511 OCTETS))';

    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_str_values_arr AS ' || CURRENT SCHEMA || '.t_str_values ARRAY[256]';

    -- ----------------------------------------------------------------------------
    -- ROW and ARRAY types for token parameter values
    -- ----------------------------------------------------------------------------
    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_token_values AS ROW ( parameter_name_id INTEGER, code_system_id    INTEGER, token_value       VARCHAR(255 OCTETS))';

    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_token_values_arr AS ' || CURRENT SCHEMA || '.t_token_values ARRAY[256]';


    -- ----------------------------------------------------------------------------
    -- ROW and ARRAY types for date parameter values
    -- ----------------------------------------------------------------------------
    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_date_values AS ROW ( parameter_name_id         INT, date_value          TIMESTAMP, date_start          TIMESTAMP, date_end            TIMESTAMP)';

    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_date_values_arr AS ' || CURRENT SCHEMA || '.t_date_values ARRAY[256]';


    -- ----------------------------------------------------------------------------
    -- ROW and ARRAY types for number parameter values
    -- ----------------------------------------------------------------------------
    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_number_values AS ROW ( parameter_name_id      INT, number_value        DOUBLE)';

    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_number_values_arr AS ' || CURRENT SCHEMA || '.t_number_values ARRAY[256]';



    -- ----------------------------------------------------------------------------
    -- ROW and ARRAY types for latlng parameter values
    -- ----------------------------------------------------------------------------
    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_latlng_values AS ROW ( parameter_name_id      INT, latitude_value      DOUBLE, longitude_value     DOUBLE)'; 
    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_latlng_values_arr AS ' || CURRENT SCHEMA || '.t_latlng_values ARRAY[256]';


    -- ----------------------------------------------------------------------------
    -- ROW and ARRAY types for quantity parameter values
    -- ----------------------------------------------------------------------------
    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_quantity_values AS ROW ( parameter_name_id        INT, code                 VARCHAR(255 OCTETS), quantity_value        DOUBLE, quantity_value_low    DOUBLE, quantity_value_high   DOUBLE, code_system_id           INT)';

    EXECUTE IMMEDIATE 'CREATE OR REPLACE TYPE ' || CURRENT SCHEMA || '.t_quantity_values_arr AS ' || CURRENT SCHEMA || '.t_quantity_values ARRAY[256]';

END
@

COMMIT WORK@
-- ----------------------------------------------------------------------------
-- END OF PARAMETER TYPES PROC
-- ----------------------------------------------------------------------------
