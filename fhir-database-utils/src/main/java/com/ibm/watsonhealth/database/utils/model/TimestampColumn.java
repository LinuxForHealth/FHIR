/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.model;

import com.ibm.watsonhealth.database.utils.api.IDatabaseTypeAdapter;

/**
 * @author rarnold
 *
 */
public class TimestampColumn extends ColumnBase {

    /**
     * @param name
     */
    public TimestampColumn(String name, boolean nullable) {
        super(name, nullable);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.schema.model.ColumnBase#getTypeInfo()
     */
    @Override
    public String getTypeInfo(IDatabaseTypeAdapter translator) {
        return "TIMESTAMP";
    }

}
