/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.Map.Entry;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * MigrateServiceAction - Migrates Service Action
 */
public class MigrateSchemaAction implements ISchemaAction {
    public MigrateSchemaAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        VersionHistoryService vhs = actionBean.getVersionHistoryService();
        PhysicalDataModel pdm = actionBean.getPhysicalDataModel();
        for (Entry<String, Table> tableEntry : pdm.getTables().entrySet()) {
            Table applyTable = tableEntry.getValue();
            String objectSchema = applyTable.getSchemaName();
            String objectName = applyTable.getObjectName();
            String objectType = applyTable.getObjectName();
            int changeVersion = applyTable.getVersion();
            if (vhs.applies(objectSchema, objectType, objectName, changeVersion)) {
                
            }
        }
    }
}