/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.schema.control;

import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.model.DataModelVisitorBase;
import org.linuxforhealth.fhir.database.utils.model.ForeignKeyConstraint;
import org.linuxforhealth.fhir.database.utils.model.Table;

/**
 * Visitor adapter used to add all the foreign key constraints
 * associated with tables in the schema.
 *
 * Expects any transaction handling to be performed outside this class.
 */
public class AddForeignKey extends DataModelVisitorBase {
    private static final Logger logger = Logger.getLogger(DropForeignKey.class.getName());

    // The database adapter used to issue changes to the database
    private final ISchemaAdapter adapter;
    /**
     * Public constructor
     * @param adapter
     */
    public AddForeignKey(ISchemaAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void visited(Table fromChildTable, ForeignKeyConstraint fk) {
        // Enable (add) the FK constraint
        logger.info(String.format("Adding foreign key: %s.%s[%s]", fromChildTable.getSchemaName(), fromChildTable.getObjectName(), fk.getConstraintName()));
        fk.apply(fromChildTable.getSchemaName(), fromChildTable.getObjectName(), adapter, fromChildTable.getDistributionType());
    }
}