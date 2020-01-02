/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.UndefinedNameException;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.app.Main;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * Create the Version History Service local objects. 
 */
public class VersionHistoryServiceAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(VersionHistoryServiceAction.class.getName());

    public VersionHistoryServiceAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        VersionHistoryService vhs =
                new VersionHistoryService(actionBean.getAdminSchemaName(), actionBean.getSchemaName());
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(adapter);
        try {
            vhs.init();
            actionBean.setVersionHistoryService(vhs);
        } catch (UndefinedNameException une) {
            logger.warning("The Version History Service is not found");
            actionBean.setExitStatus(Main.EXIT_RUNTIME_ERROR);
            throw new SchemaActionException();
        }
    }
}