/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;


/**
 * A visitor used to traverse the FHIRDbSizeModel elements
 */
public interface FHIRDbSizeModelVisitor {

    /**
     * Called once before any of the other methods
     */
    void start();

    /**
     * Resource-level summary
     * @param resourceType the FHIR resource type name
     * @param logicalResourceRowEstimate estimate of rows in xx_logical_resources
     * @param resourceRowEstimate estimate of rows in xx_resources
     * @param totalTableSize the database level total table size
     * @param totalIndexSize the database level total index size
     * @param rowEstimate the estimated number of row across all tables for the given resource
     * @param resourceTableSize the resource level total table size
     * @param resourceIndexSize the resource level total index size
     */
    void resource(String resourceType, long logicalResourceRowEstimate, long resourceRowEstimate, long totalTableSize, long totalIndexSize, long rowEstimate, long resourceTableSize, long resourceIndexSize);

    /**
     * The size of a table
     * @param resourceType
     * @param tableName
     * @param rowEstimate
     * @param tableSize
     * @param allIndexSize
     */
    void table(String resourceType, String tableName, boolean isParameter, long rowEstimate, long tableSize, long allIndexSize);

    /**
     * The size of an index
     * @param resourceType
     * @param tableName
     * @param indexName
     * @param indexSize
     */
    void index(String resourceType, String tableName, String indexName, long indexSize);
}