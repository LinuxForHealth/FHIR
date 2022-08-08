/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

import java.util.HashMap;
import java.util.Map;

import org.linuxforhealth.fhir.schema.app.util.SchemaSupport;

/**
 * Collect a summary of size information for each parameter table
 */
public class FHIRDbParameterSummary implements FHIRDbSizeModelVisitor {
    private final Map<String, FHIRDbTableSize> parameterMap = new HashMap<>();

    // Utilities for processing table names
    private static final SchemaSupport SCHEMA_SUPPORT = new SchemaSupport();

    // Where to render the results
    private final ISizeReport report;

    /**
     * Public constructor
     * @param output
     */
    public FHIRDbParameterSummary(ISizeReport report) {
        this.report = report;
    }

    @Override
    public void start() {
        // NOP
    }

    @Override
    public void resource(String resourceType, long logicalResourceRowEstimate, long resourceRowEstimate, long totalTableSize, long totalIndexSize, long rowEstimate, long resourceTableSize, long resourceIndexSize) {
        // NOP
    }

    @Override
    public void table(String resourceType, String parameterTable, boolean isParameterTable, long rowEstimate, long paramTableSize, long paramIndexSize) {
        if (isParameterTable) {
            final String paramType = SCHEMA_SUPPORT.getParamTableType(parameterTable);
            FHIRDbTableSize ts = parameterMap.computeIfAbsent(paramType, k -> new FHIRDbTableSize());
            ts.accumulateRowEstimate(rowEstimate);
            ts.accumulateTableSize(paramTableSize);
            ts.accumulateIndexSize("all", paramIndexSize);
        }
    }

    /**
     * Render the parameter type summary data that has been accumulated.
     * @param totalTableSize
     * @param totalIndexSize
     */
    public void render(long totalTableSize, long totalIndexSize) {
        for (Map.Entry<String, FHIRDbTableSize> entry: parameterMap.entrySet()) {
            final String paramType = entry.getKey();
            FHIRDbTableSize ts = entry.getValue();
            report.renderParameterSummary(paramType, ts.getRowEstimate(), totalTableSize, ts.getTableSize(), totalIndexSize, ts.getTotalIndexSize());
        }
    }

    @Override
    public void index(String resourceType, String tableName, String indexName, long indexSize) {
        // NOP
    }
}
