/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Collection;
import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.IVersionHistoryService;
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskGroup;

/**
 * Used to create and drop tablespaces within a database
 */
public class Tablespace extends DatabaseObject {

    // The extent size to use for this tablespace
    private final int extentSizeKB;

    /**
     * Public constructor
     * @param tablespaceName
     * @param extentSizeKB
     */
    public Tablespace(String tablespaceName, int version, int extentSizeKB) {
        super(tablespaceName, DatabaseObjectType.TABLESPACE, version);
        this.extentSizeKB = extentSizeKB;
    }

    @Override
    public void apply(IDatabaseAdapter target) {
        if (this.extentSizeKB > 0) {
            target.createTablespace(getName(), this.extentSizeKB);
        }
        else {
            // Use database default
            target.createTablespace(getName());
        }
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target) {
        if (priorVersion != null && priorVersion > 0) {
            throw new UnsupportedOperationException("Modifying tablespaces is not supported");
        }
        apply(target);
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropTablespace(getName());
    }

    @Override
    public ITaskGroup collect(ITaskCollector tc, IDatabaseAdapter target, ITransactionProvider tp, IVersionHistoryService vhs) {
        // no dependencies, so no need to recurse down
        List<ITaskGroup> children = null;
        return tc.makeTaskGroup(this.getTypeNameVersion(), () -> applyTx(target, tp, vhs), children);
    }

    @Override
    public void addDependencies(Collection<IDatabaseObject> deps) {
        throw new IllegalStateException("Tablespace should have no dependencies");
    }

    @Override
    public void fetchDependenciesTo(Collection<IDatabaseObject> out) {
        // NOP
    }

    @Override
    public void grant(IDatabaseAdapter target, String groupName, String toUser) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#visit(com.ibm.fhir.database.utils.model.DataModelVisitor)
     */
    @Override
    public void visit(DataModelVisitor v) {
        v.visited(this);
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#visitReverse(com.ibm.fhir.database.utils.model.DataModelVisitor)
     */
    @Override
    public void visitReverse(DataModelVisitor v) {
        v.visited(this);
    }

    @Override
    public void applyDistributionRules(IDatabaseAdapter target) {
        // NOP
    }
}
