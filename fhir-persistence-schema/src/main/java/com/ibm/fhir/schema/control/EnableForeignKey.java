/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.schema.control;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.model.DataModelVisitorBase;
import com.ibm.fhir.database.utils.model.ForeignKeyConstraint;
import com.ibm.fhir.database.utils.model.Table;


/**
 * Visitor adapter used to disable all the foreign key constraints
 * associated with partitioned tables in the schema. 
 * 
 * Expects any transaction handling to be performed outside this class.
 */
public class EnableForeignKey extends DataModelVisitorBase {
    private static final Logger logger = Logger.getLogger(EnableForeignKey.class.getName());
    
    // The database adapter used to issue changes to the database
    private final IDatabaseAdapter adapter;

    /**
     * Public constructor
     * @param adapter
     * @param pdm the PhysicalDataModel used to identify the target tables of FK constraints
     */
    public EnableForeignKey(IDatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void visited(Table fromChildTable, ForeignKeyConstraint fk) {
        // Only enable the FK if the table is partitioned and the model
        // wants the constraint to be enforced
        if (fk.isEnforced() && fromChildTable.getTenantColumnName() != null) {
            logger.info(String.format("Enabling foreign key: %s.%s[%s]", fromChildTable.getSchemaName(), fromChildTable.getObjectName(), fk.getConstraintName()));
            adapter.enableForeignKey(fromChildTable.getSchemaName(), fromChildTable.getObjectName(), fk.getConstraintName());
        }
    }
}
