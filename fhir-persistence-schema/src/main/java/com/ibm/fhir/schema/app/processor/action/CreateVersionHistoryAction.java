/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * creates the version history tables
 */
public class CreateVersionHistoryAction implements ISchemaAction {

    public CreateVersionHistoryAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        // Before we start anything, we need to make sure our schema history
        // tables are in place. There's only a single history table, which
        // resides in the admin schema and handles the history of all objects
        // in any schema being managed.
        CreateVersionHistory.createTableIfNeeded(actionBean.getAdminSchemaName(), adapter);
    }
}