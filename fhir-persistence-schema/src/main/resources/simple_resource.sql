CREATE OR REPLACE PROCEDURE PTNG.simple_resource
-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2019
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
    ( IN p_resource_type                 VARCHAR( 32 OCTETS),
      IN p_logical_id                    VARCHAR(255 OCTETS)
    )
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN

  DECLARE stmt STATEMENT;
  DECLARE v_logical_resource_id BIGINT DEFAULT NULL;
  DECLARE v_current_resource_id BIGINT DEFAULT NULL;

  PREPARE stmt FROM 'SELECT logical_resource_id, current_resource_id FROM PTNG.patient_logical_resources WHERE logical_id = ? FOR UPDATE WITH RS';
  EXECUTE stmt INTO v_logical_resource_id, v_current_resource_id USING p_logical_id;
END
