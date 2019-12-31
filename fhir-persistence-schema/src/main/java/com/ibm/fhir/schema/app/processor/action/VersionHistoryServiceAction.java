/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.version.VersionHistoryService;

public class VersionHistoryServiceAction implements ISchemaAction {
    private String schemaName;
    private String adminSchemaName;
    private VersionHistoryService vhs;

    public VersionHistoryServiceAction(String schemaName, String adminSchemaName) {
        this.schemaName            = schemaName;
        this.adminSchemaName       = adminSchemaName;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        vhs = new VersionHistoryService(adminSchemaName, schemaName);
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(adapter);
        vhs.init();
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        vhs = new VersionHistoryService(adminSchemaName, schemaName);
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(adapter);
        vhs.init();
    }
    
    public VersionHistoryService getVersionHistoryService() {
        return vhs;
    }
}
