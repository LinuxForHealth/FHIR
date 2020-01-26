/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.task.core.service.TaskService;

/**
 * --prop-file /Users/paulbastide/git/wffh/FHIR/fhir-persistence-schema/db2.properties
--pool-size 2
--schema-name FHIRDATA
--update-schema
--dry-run
 */
public class UpdateSchemaAction implements ISchemaAction {

    public UpdateSchemaAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(actionBean.getAdminSchemaName(), actionBean.getSchemaName());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // The objects are applied in parallel, which relies on each object
        // expressing its dependencies correctly. Changes are only applied
        // if their version is greater than the current version.
        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(actionBean.getMaxConnectionPoolSize());
        actionBean.setCollector(taskService.makeTaskCollector(pool));
    }
}