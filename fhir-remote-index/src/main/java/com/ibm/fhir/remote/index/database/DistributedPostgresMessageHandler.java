/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TokenParameter;

/**
 * Loads search parameter values into the target FHIR schema on
 * a PostgreSQL database.
 */
public class DistributedPostgresMessageHandler extends BaseMessageHandler {

    // the connection to use for the inserts
    private final Connection connection;

    // rarely used so no need for {@code java.sql.PreparedStatement} or batching on this one
    // even on Location resources its only there once by default
    private final String insertLocation;

    // Searchable string attributes stored at the system level
    private final PreparedStatement systemStrings;
    private int systemStringCount;

    // Searchable date attributes stored at the system level
    private final PreparedStatement systemDates;
    private int systemDateCount;

    /**
     * Public constructor
     * 
     * @param connection
     */
    public DistributedPostgresMessageHandler(Connection connection) {
        this.connection = connection;
        insertString = "INSERT INTO " + tablePrefix + "_str_values (parameter_name_id, str_value, str_value_lcase, logical_resource_id, composite_id) VALUES (?,?,?,?,?)";
        strings = connection.prepareStatement(insertString);

        insertNumber = multitenant ?
                "INSERT INTO " + tablePrefix + "_number_values (mt_id, parameter_name_id, number_value, number_value_low, number_value_high, logical_resource_id, composite_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?,?,?)"
                :
                "INSERT INTO " + tablePrefix + "_number_values (parameter_name_id, number_value, number_value_low, number_value_high, logical_resource_id, composite_id) VALUES (?,?,?,?,?,?)";
        numbers = c.prepareStatement(insertNumber);

        insertDate = multitenant ?
                "INSERT INTO " + tablePrefix + "_date_values (mt_id, parameter_name_id, date_start, date_end, logical_resource_id, composite_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?,?)"
                :
                "INSERT INTO " + tablePrefix + "_date_values (parameter_name_id, date_start, date_end, logical_resource_id, composite_id) VALUES (?,?,?,?,?)";
        dates = c.prepareStatement(insertDate);

        insertQuantity = multitenant ?
                "INSERT INTO " + tablePrefix + "_quantity_values (mt_id, parameter_name_id, code_system_id, code, quantity_value, quantity_value_low, quantity_value_high, logical_resource_id, composite_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?,?,?,?,?)"
                :
                "INSERT INTO " + tablePrefix + "_quantity_values (parameter_name_id, code_system_id, code, quantity_value, quantity_value_low, quantity_value_high, logical_resource_id, composite_id) VALUES (?,?,?,?,?,?,?,?)";
        quantities = c.prepareStatement(insertQuantity);

        insertLocation = multitenant ? "INSERT INTO " + tablePrefix + "_latlng_values (mt_id, parameter_name_id, latitude_value, longitude_value, logical_resource_id, composite_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?,?)"
                : "INSERT INTO " + tablePrefix + "_latlng_values (parameter_name_id, latitude_value, longitude_value, logical_resource_id, composite_id) VALUES (?,?,?,?,?)";

        if (storeWholeSystemParams) {
            // System level string attributes
            String insertSystemString = multitenant ?
                    "INSERT INTO str_values (mt_id, parameter_name_id, str_value, str_value_lcase, logical_resource_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?)"
                    :
                    "INSERT INTO str_values (parameter_name_id, str_value, str_value_lcase, logical_resource_id) VALUES (?,?,?,?)";
            systemStrings = c.prepareStatement(insertSystemString);

            // System level date attributes
            String insertSystemDate = multitenant ?
                    "INSERT INTO date_values (mt_id, parameter_name_id, date_start, date_end, logical_resource_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?)"
                    :
                    "INSERT INTO date_values (parameter_name_id, date_start, date_end, logical_resource_id) VALUES (?,?,?,?)";
            systemDates = c.prepareStatement(insertSystemDate);
        } else {
            systemStrings = null;
            systemDates = null;
        }
    }
    /**
     * Compute the shard key value use to distribute resources among nodes
     * of the database
     * @param resourceType
     * @param logicalId
     * @return
     */
    private short encodeShardKey(String resourceType, String logicalId) {
        final String requestShardKey = resourceType + "/" + logicalId;
        return Short.valueOf((short)requestShardKey.hashCode());
    }

    @Override
    protected void pushBatch() {
        // Push any data we've accumulated so far. This may occur
        // if we cross a volume threshold, and will always occur as
        // the last step before the current transaction is committed,
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, LocationParameter p) {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, TokenParameter p) {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, QuantityParameter p) {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, NumberParameter p) {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, DateParameter p) {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void process(String tenantId, String resourceType, String logicalId, long logicalResourceId, StringParameter p) {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        // TODO Auto-generated method stub
        
    }
}
