/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

import java.io.IOException;
import java.io.Writer;

import org.linuxforhealth.fhir.database.utils.api.DataAccessException;

/**
 * Write output info for each table in the model
 */
public class FHIRDbIndexDetailWriter implements FHIRDbSizeModelVisitor {
    private static final String TAB = "\t";
    private static final String NL = System.lineSeparator();

    // Target for writing the output
    private final Writer output;

    /**
     * Public constructor
     * @param output
     */
    public FHIRDbIndexDetailWriter(Writer output) {
        this.output = output;
    }

    @Override
    public void start() {
        final StringBuilder line = new StringBuilder();
        line.append("index_detail");
        line.append(TAB);
        line.append("resourceType");
        line.append(TAB);
        line.append("tableName");
        line.append(TAB);
        line.append("tableSuffix");
        line.append(TAB);
        line.append("indexName");
        line.append(TAB);
        line.append("indexSuffix");
        line.append(TAB);
        line.append("indexBytes");
        try {
            output.write(line.toString());
            output.write(NL);
        } catch (IOException x) {
            throw new DataAccessException(x);
        }
    }

    @Override
    public void resource(String resourceType, long logicalResourceRowEstimate, long resourceRowEstimate, long totalTableSize, long totalIndexSize, long rowEstimate, long resourceTableSize, long resourceIndexSize) {
        // NOP
    }

    @Override
    public void table(String resourceType, String tableName, boolean isParameter, long rowEstimate, long tableSize, long paramIndexSize) {
        // NOP
    }

    @Override
    public void index(String resourceType, String tableName, String indexName, long indexSize) {
        String tableSuffix = "none";
        if (tableName.toLowerCase().startsWith(resourceType.toLowerCase())) {
            tableSuffix = tableName.substring(resourceType.length() + 1);
        }
        
        final String indexSuffix = indexName.substring(indexName.lastIndexOf('_')+1);

        final StringBuilder line = new StringBuilder();
        line.append("index_detail"); // make it easy to grep the index lines
        line.append(TAB);
        line.append(resourceType);
        line.append(TAB);
        line.append(tableName);
        line.append(TAB);
        line.append(tableSuffix);
        line.append(TAB);
        line.append(indexName);
        line.append(TAB);
        line.append(indexSuffix);
        line.append(TAB);
        line.append(indexSize);
        try {
            output.write(line.toString());
            output.write(NL);
        } catch (IOException x) {
            throw new DataAccessException(x);
        }
    }
}
