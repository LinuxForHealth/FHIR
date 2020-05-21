/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;

/**
 * Constraint on unique
 */
public class UniqueConstraint extends Constraint {
    private final List<String> localColumns = new ArrayList<>();
    
    /**
     * @param constraintName
     * @param columns
     */
    protected UniqueConstraint(String constraintName, String... columns) {
        super(constraintName);
        localColumns.addAll(Arrays.asList(columns));
    }

    /**
     * @param name
     * @param target
     */
    public void apply(String schemaName, String name, String tenantColumnName, IDatabaseAdapter target) {
        target.createUniqueConstraint(getConstraintName(), localColumns, schemaName, name);
    }
}