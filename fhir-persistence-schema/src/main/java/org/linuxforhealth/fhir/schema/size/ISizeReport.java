/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

/**
 * Render a size report
 */
public interface ISizeReport {

    /**
     * Render a report using the data from the given model
     * @param model
     */
    public void render(FHIRDbSizeModel model);
    
    /**
     * Render the parameter summary for the given parameter table name
     * @param parameterTable
     * @param rowEstimate
     * @param totalTableSize
     * @param tableSize
     * @param totalIndexSize
     * @param indexSize
     */
    public void renderParameterSummary(String parameterTable, long rowEstimate, long totalTableSize, long tableSize, long totalIndexSize, long indexSize);
}
