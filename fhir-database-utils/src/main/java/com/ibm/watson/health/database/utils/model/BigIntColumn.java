/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.database.utils.model;

import com.ibm.watson.health.database.utils.api.IDatabaseTypeAdapter;

/**
 * @author rarnold
 *
 */
public class BigIntColumn extends ColumnBase {

    /**
     * @param name
     */
    public BigIntColumn(String name, boolean nullable) {
        super(name, nullable);
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.schema.model.ColumnBase#getTypeInfo()
     */
    @Override
    public String getTypeInfo(IDatabaseTypeAdapter translator) {
        return "BIGINT";
    }

}
