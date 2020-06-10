/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.schema.control;

import java.util.Set;
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
public class DisableForeignKey extends DataModelVisitorBase {
    private static final Logger logger = Logger.getLogger(DisableForeignKey.class.getName());
    
    // The database adapter used to issue changes to the database
    private final IDatabaseAdapter adapter;
    
    // the set of childTables we collect along the way
    private final Set<Table> childTables;

    /**
     * Public constructor
     * @param adapter
     * @param childTables set of child {@link Table} objects collected along the way
     */
    public DisableForeignKey(IDatabaseAdapter adapter, Set<Table> childTables) {
        this.adapter = adapter;
        this.childTables = childTables;
    }

    @Override
    public void visited(Table fromChildTable, ForeignKeyConstraint fk) {
        // Only disable the FK if the table is partitioned
        if (fk.isEnforced() && fromChildTable.getTenantColumnName() != null) {
            logger.info(String.format("Disabling foreign key: %s.%s[%s]", fromChildTable.getSchemaName(), fromChildTable.getObjectName(), fk.getConstraintName()));
            adapter.disableForeignKey(fromChildTable.getSchemaName(), fromChildTable.getObjectName(), fk.getConstraintName());
            
            if (!childTables.contains(fromChildTable)) {
                this.childTables.add(fromChildTable);
            }
        }
    }
}
