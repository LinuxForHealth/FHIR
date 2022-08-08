/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.database.utils.api.IVersionHistoryService;
import org.linuxforhealth.fhir.database.utils.api.SchemaApplyContext;
import org.linuxforhealth.fhir.task.api.ITaskCollector;
import org.linuxforhealth.fhir.task.api.ITaskGroup;

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
    public void apply(ISchemaAdapter target, SchemaApplyContext context) {
        if (this.extentSizeKB > 0) {
            target.createTablespace(getName(), this.extentSizeKB);
        }
        else {
            // Use database default
            target.createTablespace(getName());
        }
    }

    @Override
    public void apply(Integer priorVersion, ISchemaAdapter target, SchemaApplyContext context) {
        if (priorVersion != null && priorVersion > 0) {
            throw new UnsupportedOperationException("Modifying tablespaces is not supported");
        }
        apply(target, context);
    }

    @Override
    public void drop(ISchemaAdapter target) {
        target.dropTablespace(getName());
    }

    @Override
    public ITaskGroup collect(ITaskCollector tc, ISchemaAdapter target, SchemaApplyContext context, ITransactionProvider tp, IVersionHistoryService vhs) {
        // no dependencies, so no need to recurse down
        List<ITaskGroup> children = null;
        return tc.makeTaskGroup(this.getTypeNameVersion(), () -> applyTx(target, context, tp, vhs), children);
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
    public void grant(ISchemaAdapter target, String groupName, String toUser) {
        // NOP
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.database.utils.model.IDatabaseObject#visit(org.linuxforhealth.fhir.database.utils.model.DataModelVisitor)
     */
    @Override
    public void visit(DataModelVisitor v) {
        v.visited(this);
    }

    /* (non-Javadoc)
     * @see org.linuxforhealth.fhir.database.utils.model.IDatabaseObject#visitReverse(org.linuxforhealth.fhir.database.utils.model.DataModelVisitor)
     */
    @Override
    public void visitReverse(DataModelVisitor v) {
        v.visited(this);
    }
}
