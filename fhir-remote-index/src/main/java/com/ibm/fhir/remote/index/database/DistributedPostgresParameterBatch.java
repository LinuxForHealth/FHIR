/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Parameter batch statements configured for a given resource type
 */
public class DistributedPostgresParameterBatch {
    private final Connection connection;
    private final String resourceType;
    private final PreparedStatement strings;
    private int stringCount;

    private final PreparedStatement numbers;
    private int numberCount;

    private final PreparedStatement dates;
    private int dateCount;

    private final PreparedStatement quantities;
    private int quantityCount;

    /**
     * Public constructor
     * @param connection
     * @param resourceType
     */
    public DistributedPostgresParameterBatch(Connection connection, String resourceType) {
        this.connection = connection;
        this.resourceType = resourceType;
    }

    /**
     * Push the current batch
     */
    public void pushBatch() {
        
    }
}