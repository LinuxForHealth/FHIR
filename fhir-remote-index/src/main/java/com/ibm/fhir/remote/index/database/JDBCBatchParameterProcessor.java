/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TokenParameter;
import com.ibm.fhir.remote.index.api.BatchParameterProcessor;


/**
 * Processes batched parameters by pushing the values to various
 * JDBC statements
 */
public class JDBCBatchParameterProcessor implements BatchParameterProcessor {
    // A cache of the resource-type specific DAOs we've created
    private final Map<String, DistributedPostgresParameterBatch> daoMap = new HashMap<>();

    // Encapculates the statements for inserting whole-system level search params
    private final DistributedPostgresSystemParameterBatch systemDao;

    // Resource types we've touched in the current batch
    private final Set<String> resourceTypesInBatch = new HashSet<>();

    // The database connection this consumer thread is using
    private final Connection connection;

    /**
     * Public constructor
     * @param connection
     */
    public JDBCBatchParameterProcessor(Connection connection) {
        this.connection = connection;
        this.systemDao = new DistributedPostgresSystemParameterBatch(connection);        
    }

    /**
     * Close any resources we're holding to support a cleaner exit
     */
    public void close() {
        for (Map.Entry<String, DistributedPostgresParameterBatch> entry: daoMap.entrySet()) {
            entry.getValue().reset();
        }
        systemDao.close();
    }

    /**
     * Push any statements that have been batched but not yet executed
     * @throws FHIRPersistenceException
     */
    public void pushBatch() throws FHIRPersistenceException {
        try {
            for (String resourceType: resourceTypesInBatch) {
                DistributedPostgresParameterBatch dao = daoMap.get(resourceType);
                try {
                    dao.pushBatch();
                } catch (SQLException x) {
                    throw new FHIRPersistenceException("pushBatch failed for '" + resourceType + "'");
                }
            }

            try {
                systemDao.pushBatch();
            } catch (SQLException x) {
                throw new FHIRPersistenceException("batch insert for whole-system parameters", x);
            }
        } finally {
            // Reset the set of active resource-types ready for the next batch
            resourceTypesInBatch.clear();
        }
    }

    private DistributedPostgresParameterBatch getParameterBatchDao(String resourceType) {
        DistributedPostgresParameterBatch dao = daoMap.get(resourceType);
        if (dao == null) {
            dao = new DistributedPostgresParameterBatch(connection, resourceType);
            daoMap.put(resourceType, dao);
        }
        return dao;
    }

    @Override
    public short encodeShardKey(String resourceType, String logicalId) {
        final String requestShardKey = resourceType + "/" + logicalId;
        return Short.valueOf((short)requestShardKey.hashCode());
    }

    @Override
    public void process(String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, StringParameter parameter) throws FHIRPersistenceException {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        try {
            DistributedPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addString(logicalResourceId, parameterNameValue.getParameterNameId(), parameter.getValue(), parameter.getValue().toLowerCase(), parameter.getCompositeId(), shardKey);

            if (parameter.isSystemParam()) {
                systemDao.addString(logicalResourceId, parameterNameValue.getParameterNameId(), parameter.getValue(), parameter.getValue().toLowerCase(), parameter.getCompositeId(), shardKey);
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting string params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, NumberParameter p) throws FHIRPersistenceException {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        try {
            DistributedPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addNumber(logicalResourceId, parameterNameValue.getParameterNameId(), p.getValue(), p.getLowValue(), p.getHighValue(), p.getCompositeId(), shardKey);
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting string params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, QuantityParameter p, CodeSystemValue codeSystemValue) throws FHIRPersistenceException {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        try {
            DistributedPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addQuantity(logicalResourceId, parameterNameValue.getParameterNameId(), codeSystemValue.getCodeSystemId(), p.getValueCode(), p.getValueNumber(), p.getValueNumberLow(), p.getValueNumberHigh(), p.getCompositeId(), shardKey);
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting quantity params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, LocationParameter p) throws FHIRPersistenceException {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        try {
            DistributedPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addLocation(logicalResourceId, parameterNameValue.getParameterNameId(), p.getValueLatitude(), p.getValueLongitude(), p.getCompositeId(), shardKey);
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting location params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, DateParameter p) throws FHIRPersistenceException {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        try {
            DistributedPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addDate(logicalResourceId, parameterNameValue.getParameterNameId(), p.getValueDateStart(), p.getValueDateEnd(), p.getCompositeId(), shardKey);
            if (p.isSystemParam()) {
                systemDao.addDate(logicalResourceId, parameterNameValue.getParameterNameId(), p.getValueDateStart(), p.getValueDateEnd(), p.getCompositeId(), shardKey);
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting date params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, TokenParameter p,
        CommonTokenValue commonTokenValue) throws FHIRPersistenceException {
        final short shardKey = encodeShardKey(resourceType, logicalId);
        try {
            DistributedPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addResourceTokenRef(logicalResourceId, parameterNameValue.getParameterNameId(), commonTokenValue.getCommonTokenValueId(), p.getRefVersionId(), p.getCompositeId(), shardKey);
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting token params for '" + resourceType + "'");
        }
    }

}
