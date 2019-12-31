/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control.processor.action;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.core.service.TaskService;

public class UpdateSchemaAction implements ISchemaAction {
    private String schemaName;
    private String adminSchemaName;
    private int maxConnectionPoolSize;
    private ITaskCollector collector;
    private PhysicalDataModel pdm;

    public UpdateSchemaAction(String schemaName, String adminSchemaName, int maxConnectionPoolSize) {
        this.schemaName            = schemaName;
        this.adminSchemaName       = adminSchemaName;
        this.maxConnectionPoolSize = maxConnectionPoolSize;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);
        gen.buildProcedures(pdm);

        // The objects are applied in parallel, which relies on each object
        // expressing its dependencies correctly. Changes are only applied
        // if their version is greater than the current version.
        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(this.maxConnectionPoolSize);
        collector = taskService.makeTaskCollector(pool);
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);
        gen.buildProcedures(pdm);

        // The objects are applied in parallel, which relies on each object
        // expressing its dependencies correctly. Changes are only applied
        // if their version is greater than the current version.
        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(this.maxConnectionPoolSize);
        collector = taskService.makeTaskCollector(pool);
    }

    public ITaskCollector getCollector() {
        return collector;
    }
    
    public PhysicalDataModel getPhysicalDataModel() {
        return pdm;
    }
}