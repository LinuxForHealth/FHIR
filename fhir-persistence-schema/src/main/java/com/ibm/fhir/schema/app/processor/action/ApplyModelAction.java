/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.schema.app.feature.ExitFeature;
import com.ibm.fhir.schema.app.processor.SchemaUtil;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.task.api.ITaskGroup;

public class ApplyModelAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(ApplyModelAction.class.getName());

    public ApplyModelAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        logger.info("Collecting model update tasks");
        actionBean.getPhysicalDataModel().collect(actionBean.getCollector(), adapter, transactionProvider,
                actionBean.getVersionHistoryService());

        // FHIR in the hole!
        logger.info("Starting model updates");
        actionBean.getCollector().startAndWait();

        Collection<ITaskGroup> failedTaskGroups = actionBean.getCollector().getFailedTaskGroups();
        if (!failedTaskGroups.isEmpty()) {
            actionBean.setExitStatus(ExitFeature.EXIT_RUNTIME_ERROR);

            final String failedStr =
                    failedTaskGroups.stream().map(SchemaUtil::mapToId).collect(Collectors.joining(","));
            logger.severe("List of failed task groups: " + failedStr);
        }
    }
}