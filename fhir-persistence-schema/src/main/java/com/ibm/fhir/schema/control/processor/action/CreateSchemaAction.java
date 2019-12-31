/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control.processor.action;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;

public class CreateSchemaAction implements ISchemaAction{
    
    private String schemaName; 
    private String adminSchemaName;
    
    public CreateSchemaAction(String schemaName, String adminSchemaName) {
        this.schemaName = schemaName;
        this.adminSchemaName = adminSchemaName;
    }
    
    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        adapter.createFhirSchemas(schemaName, adminSchemaName);
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        adapter.createFhirSchemas(schemaName, adminSchemaName);
    }
}
