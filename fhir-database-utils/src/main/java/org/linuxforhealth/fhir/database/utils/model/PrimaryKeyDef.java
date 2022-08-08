/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents the definition of a primary key constraint on a table
 */
public class PrimaryKeyDef {

    // The name of the PK constraint
    public final String constraintName;

    // The list of columns comprising the primary key
    public final List<String> columns = new ArrayList<>();
    
    public PrimaryKeyDef(String constraintName, Collection<String> columns) {
        this.constraintName = constraintName;
        this.columns.addAll(columns);
    }
    
    /**
     * Getter for the name of the constraint
     * @return
     */
    public String getConstraintName() {
        return this.constraintName;
    }
    
    public Collection<String> getColumns() {
        return Collections.unmodifiableCollection(this.columns);
    }
}
