-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
( IN p_tenant_name VARCHAR(36),
  IN p_tenant_key  VARCHAR(44)
)
  LANGUAGE SQL
BEGIN
  DECLARE v_not_found INT DEFAULT 0;
  DECLARE v_tenant_status VARCHAR(16);
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_not_found = 1;

  IF p_tenant_name IS NULL
  THEN
      -- a null tenant name is asking us to clear the old tenant id value
      -- this is usually done at the end of an interaction, but can also
      -- be called by the datasource provider before it returns a connection,
      -- preventing accidental exposure of tenant-specific data
      SET fhir_admin.sv_tenant_id = NULL;
  ELSE
      -- set the sv_tenant_id session variable for the 
      -- given tenant_name, but only if the correct key
      -- is provided
      SELECT t.mt_id, t.tenant_status INTO fhir_admin.sv_tenant_id, v_tenant_status
      FROM fhir_admin.tenants t
      WHERE t.tenant_name = p_tenant_name
            AND EXISTS (SELECT 1 FROM fhir_admin.tenant_keys tk WHERE tk.mt_id = t.mt_id
            AND tk.tenant_hash = sysibm.hash(tk.tenant_salt || p_tenant_key, 2));

      IF v_not_found = 1
      THEN
        -- If v_not_found, we check only one of the conditions above to see if it is valid
        -- applications should not be passing invalid keys, so treat this as
        -- an exception

        -- Check the cause... bad tenant_name
        SELECT -1 INTO v_not_found
        FROM fhir_admin.tenants t
            WHERE t.tenant_name = p_tenant_name;
        
        IF v_not_found = -1
        THEN
           -- The name was found, therefore the key must be bad.
           SIGNAL SQLSTATE '99401' SET MESSAGE_TEXT = 'NOT AUTHORIZED: MISSING OR INVALID TENANT KEY';
        ELSE
           -- The name was not found, therefore the name must be bad.
           SIGNAL SQLSTATE '99401' SET MESSAGE_TEXT = 'NOT AUTHORIZED: MISSING OR INVALID TENANT NAME';
        END IF;
      ELSEIF v_tenant_status = 'FROZEN'
      THEN
        -- the tenant_key is valid, but the status of tenant has been set to frozen
        -- which means the tenant data should not be accessible
        SIGNAL SQLSTATE '99401' SET MESSAGE_TEXT = 'NOT AUTHORIZED: TENANT IS FROZEN';
      END IF;
  END IF;
END