/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.IVersionHistoryService;

/**
 * A NOP (no operation) object which can be used to simplify dependencies
 * by making this object depend on other, then everything else simply
 * depend on this, should that be the sort of behavior you want.
 */
public class NopObject extends BaseObject {

    /**
     * Public constructor
     * @param schemaName
     * @param objectName
     */
    public NopObject(String schemaName, String objectName) {
        // this object doesn't change any schema, but we always want to
        // use it, hence the special version of 0.
        super(schemaName, objectName, DatabaseObjectType.NOP, 0);
    }

    @Override
    public void apply(IDatabaseAdapter target) {
        // We're NOP so we do nothing on purpose
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target) {
        // We're NOP so we do nothing on purpose
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        // We're NOP so we do nothing on purpose
    }

    @Override
    public void applyTx(IDatabaseAdapter target, ITransactionProvider tp, IVersionHistoryService vhs) {
        // We're NOP so we do nothing on purpose
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#visit(com.ibm.fhir.database.utils.model.DataModelVisitor)
     */
    @Override
    public void visit(DataModelVisitor v) {
        // let the visitor decide what a NOP really means
        v.nop();
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#visitReverse(com.ibm.fhir.database.utils.model.DataModelVisitor)
     */
    @Override
    public void visitReverse(DataModelVisitor v) {
        // let the visitor decide what a NOP really means
        v.nop();
    }

}
