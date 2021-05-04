/*
 * (C) Copyright IBM Corp. 2019,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * The OrderByClause SQL definition
 */
public class PaginationClause {

    private final int offset;

    private final int rowsPerPage;

    public PaginationClause(int offset, int rowsPerPage) {
        this.offset = offset;
        this.rowsPerPage = rowsPerPage;
    }

    public String getSqlString(IDatabaseTranslator translator) {
        return translator.pagination(offset, rowsPerPage);
    }

    @Override
    public String toString() {
        // Just a string for info purposes
        return "OFFSET" + this.offset + " LIMIT " + this.rowsPerPage;
    }
}