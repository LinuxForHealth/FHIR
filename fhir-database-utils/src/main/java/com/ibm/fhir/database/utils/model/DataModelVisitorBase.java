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

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#visited(com.ibm.fhir.database.utils.model.ForeignKeyConstraint)
     */
    @Override
    public void visited(Table fromChildTable, ForeignKeyConstraint fk) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#visited(com.ibm.fhir.database.utils.model.Table)
     */
    @Override
    public void visited(Table tbl) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#nop()
     */
    @Override
    public void nop() {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#visited(com.ibm.fhir.database.utils.model.ProcedureDef)
     */
    @Override
    public void visited(ProcedureDef procedureDef) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#visited(com.ibm.fhir.database.utils.model.RowArrayType)
     */
    @Override
    public void visited(RowArrayType rowArrayType) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#visited(com.ibm.fhir.database.utils.model.RowType)
     */
    @Override
    public void visited(RowType rowType) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#visited(com.ibm.fhir.database.utils.model.Sequence)
     */
    @Override
    public void visited(Sequence sequence) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#visited(com.ibm.fhir.database.utils.model.SessionVariableDef)
     */
    @Override
    public void visited(SessionVariableDef sessionVariableDef) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#visited(com.ibm.fhir.database.utils.model.Tablespace)
     */
    @Override
    public void visited(Tablespace tablespace) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.DataModelVisitor#visited(com.ibm.fhir.database.utils.model.FunctionDef)
     */
    @Override
    public void visited(FunctionDef functionDef) {
        // NOP
    }

    @Override
    public void visited(AlterSequenceStartWith alterSequence) {
        // TODO Auto-generated method stub
        
    }

}
