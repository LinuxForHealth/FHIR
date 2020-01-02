/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * Creates the Schema 
 * <br>
 * Configuration to create the tenant schema and if necessary the admin schema:
 * <pre>
 * --prop-file PATH/to/db2.properties
 * --pool-size 2
 * --create-schemas
 * </pre>
 * 
 * Configuration parameter to add dry-run:
 * <pre>
 * --dry-run
 * </pre>
 */
public class CreateSchemaAction implements ISchemaAction {

    public CreateSchemaAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        adapter.createFhirSchemas(actionBean.getSchemaName(), actionBean.getAdminSchemaName());
    }
}