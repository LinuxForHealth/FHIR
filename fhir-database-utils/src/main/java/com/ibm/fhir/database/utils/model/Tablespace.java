/**
 * (C) Copyright IBM Corp. 2019
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
 * @author rarnold
 *
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

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#apply(com.ibm.fhir.database.utils.api.IDatabaseAdapter)
     */
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

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#drop(com.ibm.fhir.database.utils.api.IDatabaseAdapter)
     */
    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropTablespace(getName());
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#collect(com.ibm.fhir.task.api.ITaskCollector, com.ibm.fhir.database.utils.api.IDatabaseAdapter, com.ibm.fhir.database.utils.api.ITransactionProvider)
     */
    @Override
    public ITaskGroup collect(ITaskCollector tc, IDatabaseAdapter target, ITransactionProvider tp, IVersionHistoryService vhs) {
        // no dependencies, so no need to recurse down
        List<ITaskGroup> children = null;
        return tc.makeTaskGroup(this.getTypeAndName(), () -> applyTx(target, tp, vhs), children);
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#addDependencies(java.util.Collection)
     */
    @Override
    public void addDependencies(Collection<IDatabaseObject> deps) {
        throw new IllegalStateException("Tablespace should have no dependencies");
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#fetchDependenciesTo(java.util.Collection)
     */
    @Override
    public void fetchDependenciesTo(Collection<IDatabaseObject> out) {
        // NOP
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#grant(com.ibm.fhir.database.utils.api.IDatabaseAdapter, java.lang.String, java.lang.String)
     */
    @Override
    public void grant(IDatabaseAdapter target, String groupName, String toUser) {
        // NOP
    }
}
