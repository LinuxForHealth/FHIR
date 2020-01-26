/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * Checks the Compatibility with the Schema
 * <br>
 * Configuration:
 * 
 * <pre>
 * --prop-file db2.properties
 * --schema-name FHIRDATA
 * --pool-size 2
 * --check-compatibility
 * </pre>
 * 
 * Add dry run
 * 
 * <pre>
 * --dry - run
 * </pre>
 */
public class CheckCompatibilityAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(CheckCompatibilityAction.class.getName());

    public CheckCompatibilityAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        boolean compatible = false;
        if (adapter instanceof Db2Adapter) {
            Db2Adapter db2Adapter = (Db2Adapter) adapter;
            try {
                compatible = db2Adapter.checkCompatibility(actionBean.getAdminSchemaName());
            } catch (com.ibm.fhir.database.utils.api.UndefinedNameException une) {
                logger.warning("The version history table is not found");
            }
        }
        actionBean.setCheckCompatibility(compatible);
        actionBean.setCompatible(compatible);
    }
}