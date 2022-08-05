/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.schema.control;

import java.util.Set;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseAdapter;
import org.linuxforhealth.fhir.database.utils.api.UndefinedNameException;
import org.linuxforhealth.fhir.database.utils.model.DataModelVisitorBase;
import org.linuxforhealth.fhir.database.utils.model.ForeignKeyConstraint;
import org.linuxforhealth.fhir.database.utils.model.Table;

/**
 * Visitor adapter used to drop all the foreign key constraints
 * associated with tables in the schema.
 *
 * Expects any transaction handling to be performed outside this class.
 */
public class DropForeignKey extends DataModelVisitorBase {
    private static final Logger logger = Logger.getLogger(DropForeignKey.class.getName());

    // The database adapter used to issue changes to the database
    private final IDatabaseAdapter adapter;

    // The set of childTables we collect along the way
    private final Set<Table> childTables;

    /**
     * Public constructor
     * @param adapter
     * @param childTables set of child {@link Table} objects collected along the way
     */
    public DropForeignKey(IDatabaseAdapter adapter, Set<Table> childTables) {
        this.adapter = adapter;
        this.childTables = childTables;
    }

    @Override
    public void visited(Table fromChildTable, ForeignKeyConstraint fk) {
        // We want to drop every FK we come across
        logger.info(String.format("Dropping foreign key: %s.%s[%s]", fromChildTable.getSchemaName(), fromChildTable.getObjectName(), fk.getConstraintName()));

        try {
            adapter.dropForeignKey(fromChildTable.getSchemaName(), fromChildTable.getObjectName(), fk.getConstraintName());
            if (!childTables.contains(fromChildTable)) {
                this.childTables.add(fromChildTable);
            }
        } catch (UndefinedNameException x) {
            // This is not an error - we want this to be idempotent
            logger.info(String.format("Foreign key does not exist: %s.%s[%s]", fromChildTable.getSchemaName(), fromChildTable.getObjectName(), fk.getConstraintName()));
        }
    }
}