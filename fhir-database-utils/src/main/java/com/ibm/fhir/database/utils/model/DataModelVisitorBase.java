/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.database.utils.model;


/**
 * A base implementation of the {@link DataModelVisitor} providing NOP overrides
 * for each of the visited methods. This simplifies visitor implementations
 * which may only want to provide real implementations for one or two of
 * the methods.
 */
public class DataModelVisitorBase implements DataModelVisitor {

    @Override
    public void visited(Table fromChildTable, ForeignKeyConstraint fk) {
        // NOP
    }

    @Override
    public void visited(Table tbl) {
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
    public void visited(RowArrayType rowArrayType) {
        // NOP
    }

    @Override
    public void visited(RowType rowType) {
        // NOP
    }

    @Override
    public void visited(Sequence sequence) {
        // NOP
    }

    @Override
    public void visited(SessionVariableDef sessionVariableDef) {
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
        // NOP
    }
}