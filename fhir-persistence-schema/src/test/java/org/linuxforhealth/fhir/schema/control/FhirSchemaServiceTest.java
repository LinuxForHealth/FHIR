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
import org.linuxforhealth.fhir.database.utils.derby.DerbyAdapter;
import org.linuxforhealth.fhir.database.utils.model.PhysicalDataModel;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresAdapter;
import org.linuxforhealth.fhir.database.utils.version.CreateVersionHistory;
import org.linuxforhealth.fhir.task.api.ITaskCollector;
import org.linuxforhealth.fhir.task.core.service.TaskService;

/**
 * Tests the generation of the various schemas with null output. 
 */
public class FhirSchemaServiceTest {
    private static final Logger logger = Logger.getLogger(FhirSchemaServiceTest.class.getName());
    private static final String SCHEMA_NAME = "PTNG";
    private static final String ADMIN_SCHEMA_NAME = "ADMIN_FHIR";

    @Test
    public void testPostgresTableCreation() {

        logger.info("Testing PostgreSQL schema creation");

        // Create an instance of the service and use it to test creation
        // of the FHIR schema
        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, SchemaType.PLAIN);
        PhysicalDataModel model = new PhysicalDataModel();
        gen.buildSchema(model);

        // Print the statements instead of executing them against a database
        PrintTarget tgt = new PrintTarget(null, logger.isLoggable(Level.FINE));

        // Pretend that our target is a PostgreSQL database
        PostgresAdapter adapter = new PostgresAdapter(tgt);
        ISchemaAdapter schemaAdapter = new PlainSchemaAdapter(adapter);
        SchemaApplyContext context = SchemaApplyContext.getDefault();
        model.apply(schemaAdapter, context);
    }

    @Test
    public void testParallelTableCreation() {
        logger.info("Testing PostgreSQL parallel schema build");

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

    @Test
    public void testDerbyTableCreation() {

        logger.info("Testing Derby schema creation");

        // Create an instance of the service and use it to test creation
        // of the FHIR schema
        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, SchemaType.PLAIN);
        PhysicalDataModel model = new PhysicalDataModel();
        gen.buildSchema(model);

        // Print the statements instead of executing them against a database
        PrintTarget tgt = new PrintTarget(null, logger.isLoggable(Level.FINE));

        // Pretend that our target is a Derby database
        DerbyAdapter adapter = new DerbyAdapter(tgt);
        ISchemaAdapter schemaAdapter = new PlainSchemaAdapter(adapter);
        SchemaApplyContext context = SchemaApplyContext.getDefault();
        model.apply(schemaAdapter, context);
    }

    @Test
    public void testDrop() {

        logger.info("Testing schema drop");

        // Create an instance of the service and use it to test creation
        // of the FHIR schema
        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, SchemaType.PLAIN);
        PhysicalDataModel model = new PhysicalDataModel();
        gen.buildSchema(model);

        // Print the statements instead of executing them against a database
        PrintTarget tgt = new PrintTarget(null, logger.isLoggable(Level.FINE));

        // Pretend that our target is a Derby database
        DerbyAdapter adapter = new DerbyAdapter(tgt) {
            // need to avoid running the actual statement because we don't have a database for this test
            public boolean doesTableExist(String schemaName, String tableName) { return false; };
        };
        ISchemaAdapter schemaAdapter = new PlainSchemaAdapter(adapter);
        model.drop(schemaAdapter);
    }

    @Test
    public void testVersionHistorySchema() {
        // Create a simple print target so all we see is the DDL
        PrintTarget tgt = new PrintTarget(null, logger.isLoggable(Level.FINE));

        // Pretend that our target is a Derby database
        DerbyAdapter adapter = new DerbyAdapter(tgt) {
            // need to avoid running the actual statement because we don't have a database for this test
            public boolean doesTableExist(String schemaName, String tableName) { return false; };
        };
        ISchemaAdapter schemaAdapter = new PlainSchemaAdapter(adapter);
        CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, schemaAdapter);

    }
}
