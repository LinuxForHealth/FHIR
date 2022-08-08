/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.postgres;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.linuxforhealth.fhir.database.utils.model.AlterSequenceStartWith;
import org.linuxforhealth.fhir.database.utils.model.AlterTableIdentityCache;
import org.linuxforhealth.fhir.database.utils.model.CreateIndex;
import org.linuxforhealth.fhir.database.utils.model.DataModelVisitor;
import org.linuxforhealth.fhir.database.utils.model.ForeignKeyConstraint;
import org.linuxforhealth.fhir.database.utils.model.FunctionDef;
import org.linuxforhealth.fhir.database.utils.model.ProcedureDef;
import org.linuxforhealth.fhir.database.utils.model.Sequence;
import org.linuxforhealth.fhir.database.utils.model.Table;
import org.linuxforhealth.fhir.database.utils.model.Tablespace;

/**
 * Manages setting the Vacuum Settings on the Physical Data Model
 */
public class GatherTablesDataModelVisitor implements DataModelVisitor {
    // These tables are skipped as they are not often UPDATED.
    private static final Set<String> SKIP = new HashSet<>(Arrays.asList(
        "COMMON_TOKEN_VALUES", "COMMON_CANONICAL_VALUES",
        "TENANTS", "TENANT_KEYS", "PARAMETER_NAMES", "CODE_SYSTEMS"));

    private List<Table> tables = new ArrayList<>();

    public GatherTablesDataModelVisitor() {
        // NOP
    }

    /**
     * gets the list of tables
     * @return 0..* tables
     */
    public List<Table> getTables() {
        return tables;
    }

    @Override
    public void visited(Table tbl) {
        String tableName = tbl.getObjectName().toUpperCase();
        // The Table pattern is to skip <RESOURCETYPE>_RESOURCES and not LOGICAL_RESOURCES
        if (!(tableName.endsWith("_RESOURCES") && !tableName.contains("LOGICAL_RESOURCES"))
                && !SKIP.contains(tableName)) {
            tables.add(tbl);
        }
    }

    @Override
    public void visited(Table fromChildTable, ForeignKeyConstraint fk) {
        // NOP
    }

    @Override
    public void nop() {
        // NOP
    }

    @Override
    public void visited(ProcedureDef procedureDef) {
        // NOP
    }

    @Override
    public void visited(Sequence sequence) {
        // NOP
    }

    @Override
    public void visited(Tablespace tablespace) {
        // NOP
    }

    @Override
    public void visited(FunctionDef functionDef) {
        // NOP
    }

    @Override
    public void visited(AlterSequenceStartWith alterSequence) {
        // NOP
    }

    @Override
    public void visited(AlterTableIdentityCache alterTable) {
        // NOP
    }

    @Override
    public void visited(CreateIndex createIndex) {
        //NOP
    }
}
