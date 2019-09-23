/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.common.PrintTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.core.service.TaskService;

/**
 * @author rarnold
 *
 */
public class TestParallelBuild {
    private static final Logger logger = Logger.getLogger(TestParallelBuild.class.getName());
    private static final String SCHEMA_NAME = "PTNG";
    private static final String ADMIN_SCHEMA_NAME = "FHIRADMIN";


    @Test
    public void testParallelTableCreation() {
        logger.info("Testing DB2 parallel schema build");

        // Create an instance of the service and use it to test creation
        // of the FHIR schema
        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME);
        PhysicalDataModel model = new PhysicalDataModel();
        gen.buildSchema(model);

        TestVersionHistoryService vhs = new TestVersionHistoryService();

        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(40);
        ITaskCollector collector = taskService.makeTaskCollector(pool);
        PrintTarget tgt = new PrintTarget(null, logger.isLoggable(Level.FINE));
        Db2Adapter adapter = new Db2Adapter(tgt);
        model.collect(collector, adapter, new TestTransactionProvider(), vhs);

        // FHIR in the hole!
        collector.startAndWait();
    }
}
