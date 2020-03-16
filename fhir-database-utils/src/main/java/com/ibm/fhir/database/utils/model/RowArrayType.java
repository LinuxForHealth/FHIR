/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Represents the array type
 *         CREATE OR REPLACE TYPE <schema>.t_str_values_arr AS <schema>.t_str_values ARRAY[256]
 */
public class RowArrayType extends BaseObject {
    private final String rowTypeName;
    private final int arraySize;

    /**
     * Public constructor
     * @param schemaName
     * @param typeName
     * @param rowTypeName
     */
    public RowArrayType(String schemaName, String typeName, int version, String rowTypeName, int arraySize) {
        super(schemaName, typeName, DatabaseObjectType.TYPE, version);
        this.rowTypeName = rowTypeName;
        this.arraySize = arraySize;
    }

    @Override
    public String toString() {
        return "ARRAY TYPE " + DataDefinitionUtil.getQualifiedName(getSchemaName(), getObjectName());
    }

    @Override
    public void apply(IDatabaseAdapter target) {
        target.createArrType(getSchemaName(), getObjectName(), rowTypeName, arraySize);
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target) {
        target.createArrType(getSchemaName(), getObjectName(), rowTypeName, arraySize);
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropType(getSchemaName(), getObjectName());
    }
}
