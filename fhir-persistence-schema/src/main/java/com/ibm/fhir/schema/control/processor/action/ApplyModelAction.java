/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control.processor.action;

import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.app.Main;
import com.ibm.fhir.schema.app.util.SchemaUtil;
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskGroup;

public class ApplyModelAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(ApplyModelAction.class.getName());

    private PhysicalDataModel pdm;
    private int exitStatus = 0;
    private VersionHistoryService vhs;
    private ITaskCollector collector;

    public ApplyModelAction(PhysicalDataModel pdm, VersionHistoryService vhs, ITaskCollector collector) {
        this.pdm       = pdm;
        this.vhs       = vhs;
        this.collector = collector;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        logger.info("Collecting model update tasks");
        pdm.collect(collector, adapter, transactionProvider, vhs);

        // FHIR in the hole!
        logger.info("Starting model updates");
        collector.startAndWait();

        Collection<ITaskGroup> failedTaskGroups = collector.getFailedTaskGroups();
        if (!failedTaskGroups.isEmpty()) {
            this.exitStatus = Main.EXIT_RUNTIME_ERROR;

            final String failedStr =
                    failedTaskGroups.stream().map(SchemaUtil::mapToId).collect(Collectors.joining(","));
            logger.severe("List of failed task groups: " + failedStr);
        }
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        logger.info("Collecting model update tasks");
        pdm.collect(collector, adapter, transactionProvider, vhs);

        // FHIR in the hole!
        logger.info("Starting model updates");
        collector.startAndWait();

        Collection<ITaskGroup> failedTaskGroups = collector.getFailedTaskGroups();
        if (!failedTaskGroups.isEmpty()) {
            this.exitStatus = Main.EXIT_RUNTIME_ERROR;

            final String failedStr =
                    failedTaskGroups.stream().map(SchemaUtil::mapToId).collect(Collectors.joining(","));
            logger.severe("List of failed task groups: " + failedStr);
        }
    }

    public int getExitStatus() {
        return exitStatus;
    }
}
