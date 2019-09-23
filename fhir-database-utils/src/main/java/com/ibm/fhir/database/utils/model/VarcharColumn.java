/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseTypeAdapter;

/**
 * @author rarnold
 *
 */
public class VarcharColumn extends ColumnBase {
    private final int size;

    /**
     * @param name
     */
    public VarcharColumn(String name, int size, boolean nullable) {
        super(name, nullable);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.schema.model.ColumnBase#getTypeInfo()
     */
    @Override
    public String getTypeInfo(IDatabaseTypeAdapter adapter) {
        return adapter.varcharClause(size);
    }

}
