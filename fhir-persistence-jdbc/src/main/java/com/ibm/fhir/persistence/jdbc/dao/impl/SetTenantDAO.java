/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

/**
 * Sets the tenant session variable via the fhir_admin.set_tenant stored procedure. The
 * user we are authenticated as is granted execute privilege on the procedure and read
 * privilege on the variable. This means that the variable can't be updated directly
 * and can therefore only be set via the procedure, which authenticates the call
 * using a hash of the tenant_key.
 * 
 * DB2 only.
 */
public class SetTenantDAO {

}