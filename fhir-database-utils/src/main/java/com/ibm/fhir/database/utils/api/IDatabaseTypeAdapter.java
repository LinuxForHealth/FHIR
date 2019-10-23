/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Used to handle the syntax differences between databases for certain
 * types like varbinary/varchar as bit data etc.
 */
public interface IDatabaseTypeAdapter {
    
    /**
     * Generate a clause for binary data type
     * @param size
     * @return
     */
    public String varbinaryClause(int size);

    /**
     * Generate a clause for BLOB (with an inline size if supported)
     * @param size
     * @param inlineSize
     * @return
     */
    public String blobClause(long size, int inlineSize);

    /**
     * Generate a clause for VARCHAR
     * @param size
     * @return
     */
    public String varcharClause(int size);

}
