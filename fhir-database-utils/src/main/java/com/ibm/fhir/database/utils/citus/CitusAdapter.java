/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.citus;

import java.util.List;

import com.ibm.fhir.database.utils.api.DistributionRules;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.model.CheckConstraint;
import com.ibm.fhir.database.utils.model.ColumnBase;
import com.ibm.fhir.database.utils.model.IdentityDef;
import com.ibm.fhir.database.utils.model.PrimaryKeyDef;
import com.ibm.fhir.database.utils.model.With;
import com.ibm.fhir.database.utils.postgres.PostgresAdapter;


/**
 * A database adapter implementation for Citus (distributed PostgreSQL)
 */
public class CitusAdapter extends PostgresAdapter {

    /**
     * Public constructor
     * @param target
     */
    public CitusAdapter(IDatabaseTarget target) {
        super(target);
    }

    /**
     * Public constructor
     * @param cp
     */
    public CitusAdapter(IConnectionProvider cp) {
        super(cp);
    }

    public void createTable(String schemaName, String name, String tenantColumnName, List<ColumnBase> columns, PrimaryKeyDef primaryKey,
        IdentityDef identity, String tablespaceName, List<With> withs, List<CheckConstraint> checkConstraints,
        DistributionRules distributionRules) {
        super.createTable(schemaName, name, tenantColumnName, columns, primaryKey, 
            identity, tablespaceName, withs, checkConstraints, distributionRules);

        // Apply the distribution rules, if any. Tables without distribution rules are created
        // only on Citus controller nodes and never distributed to the worker nodes
        if (distributionRules != null) {
            if (distributionRules.isReferenceTable()) {
                // A table that is fully replicated for each worker node
                CreateReferenceTableDAO dao = new CreateReferenceTableDAO(schemaName, name);
                runStatement(dao);
            } else if (distributionRules.getDistributionColumn() != null && distributionRules.getDistributionColumn().length() > 0) {
                // A table that is sharded using a hash on the distributionColumn value
                CreateDistributedTableDAO dao = new CreateDistributedTableDAO(schemaName, name, distributionRules.getDistributionColumn());
                runStatement(dao);
            }
        }
    }
}