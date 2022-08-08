/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.control;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.SchemaApplyContext;
import org.linuxforhealth.fhir.database.utils.api.SchemaType;
import org.linuxforhealth.fhir.database.utils.common.PlainSchemaAdapter;
import org.linuxforhealth.fhir.database.utils.common.PrintTarget;
import org.linuxforhealth.fhir.database.utils.model.PhysicalDataModel;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresAdapter;
import org.linuxforhealth.fhir.task.api.ITaskCollector;
import org.linuxforhealth.fhir.task.core.service.TaskService;

/**
 * Tests the parallel build out of the FHIR Schema across multiple threads and connections. 
 */
public class ParallelBuildTest {
    private static final Logger logger = Logger.getLogger(ParallelBuildTest.class.getName());
    private static final String SCHEMA_NAME = "PTNG";
    private static final String ADMIN_SCHEMA_NAME = "FHIR_ADMIN";

    @Test
    public void testParallelTableCreation() {
        logger.info("Testing DB2 parallel schema build");

        // Create an instance of the service and use it to test creation
        // of the FHIR schema
        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, SchemaType.PLAIN);
        PhysicalDataModel model = new PhysicalDataModel();
        gen.buildSchema(model);

        VersionHistoryServiceTest vhs = new VersionHistoryServiceTest();

        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(40);
        ITaskCollector collector = taskService.makeTaskCollector(pool);
        PrintTarget tgt = new PrintTarget(null, logger.isLoggable(Level.FINE));
        PostgresAdapter adapter = new PostgresAdapter(tgt);
        ISchemaAdapter schemaAdapter = new PlainSchemaAdapter(adapter);
        SchemaApplyContext context = SchemaApplyContext.getDefault();
        model.collect(collector, schemaAdapter, context, new TransactionProviderTest(), vhs);

        // FHIR in the hole!
        collector.startAndWait();
    }
}
