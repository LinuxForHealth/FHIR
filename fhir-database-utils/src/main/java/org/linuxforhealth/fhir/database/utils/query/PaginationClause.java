/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * The pagination clause SQL definition
 *   OFFSET {x} LIMIT {y}
 * or other database-specific equivalent
 */
public class PaginationClause {

    // The row offset value
    private final int offset;

    // The row limit value
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
        // Just a string for info purposes (debugging)
        return "OFFSET " + this.offset + " LIMIT " + this.rowsPerPage;
    }
}