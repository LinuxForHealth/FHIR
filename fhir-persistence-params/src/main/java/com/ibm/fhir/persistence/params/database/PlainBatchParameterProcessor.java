/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.MetricHandle;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.index.NumberParameter;
import com.ibm.fhir.persistence.index.ProfileParameter;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.persistence.index.ReferenceParameter;
import com.ibm.fhir.persistence.index.SecurityParameter;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.index.TagParameter;
import com.ibm.fhir.persistence.index.TokenParameter;
import com.ibm.fhir.persistence.params.api.BatchParameterProcessor;
import com.ibm.fhir.persistence.params.model.CodeSystemValue;
import com.ibm.fhir.persistence.params.model.CommonCanonicalValue;
import com.ibm.fhir.persistence.params.model.CommonTokenValue;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentValue;
import com.ibm.fhir.persistence.params.model.ParameterNameValue;


/**
 * Processes batched parameters by pushing the values to various
 * JDBC statements based on the plain variant of the schema
 */
public class PlainBatchParameterProcessor implements BatchParameterProcessor {
    private static final Logger logger = Logger.getLogger(PlainBatchParameterProcessor.class.getName());

    // A cache of the resource-type specific DAOs we've created
    private final Map<String, PlainPostgresParameterBatch> daoMap = new HashMap<>();

    // Encapculates the statements for inserting whole-system level search params
    private final PlainPostgresSystemParameterBatch systemDao;

    // Resource types we've touched in the current batch
    private final Set<String> resourceTypesInBatch = new HashSet<>();

    // The database connection this consumer thread is using
    private final Connection connection;

    /**
     * Public constructor
     * @param connection
     */
    public PlainBatchParameterProcessor(Connection connection) {
        this.connection = connection;
        this.systemDao = new PlainPostgresSystemParameterBatch(connection);        
    }

    /**
     * Close any resources we're holding to support a cleaner exit
     */
    public void close() {
        for (Map.Entry<String, PlainPostgresParameterBatch> entry: daoMap.entrySet()) {
            entry.getValue().close();
        }
        systemDao.close();
    }

    /**
     * Start processing a new batch
     */
    public void startBatch() {
        resourceTypesInBatch.clear();
    }

    /**
     * Make sure that each statement that may contain data is cleared before we
     * retry a batch
     */
    public void reset() {
        for (String resourceType: resourceTypesInBatch) {
            PlainPostgresParameterBatch dao = daoMap.get(resourceType);
            dao.close();
        }
        systemDao.close();
    }

    @Override
    public Short encodeShardKey(String requestShard) {
        // This implementation doesn't get involved in application-based sharding
        return null;
    }

    /**
     * Push any statements that have been batched but not yet executed
     * @throws FHIRPersistenceException
     */
    public void pushBatch() throws FHIRPersistenceException {
        try {
            for (String resourceType: resourceTypesInBatch) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Pushing batch for [" + resourceType + "]");
                }
                PlainPostgresParameterBatch dao = daoMap.get(resourceType);
                try {
                    dao.pushBatch();
                } catch (SQLException x) {
                    throw new FHIRPersistenceException("pushBatch failed for '" + resourceType + "'");
                }
            }

            try {
                try (MetricHandle createMetric = FHIRRequestContext.get().getMetricHandle("WHOLE_SYSTEM")) {
                    logger.fine("Pushing batch for whole-system parameters");
                    systemDao.pushBatch();
                }
            } catch (SQLException x) {
                throw new FHIRPersistenceException("batch insert for whole-system parameters", x);
            }
        } finally {
            // Reset the set of active resource-types ready for the next batch
            resourceTypesInBatch.clear();
        }
    }

    /**
     * Get the DAO used to batch parameter inserts for the given resourceType.
     * This method also tracks the unique set of resource types seen for a
     * collection of messages.
     * @param resourceType
     * @return
     */
    private PlainPostgresParameterBatch getParameterBatchDao(String resourceType) {
        resourceTypesInBatch.add(resourceType);
        PlainPostgresParameterBatch dao = daoMap.get(resourceType);
        if (dao == null) {
            dao = new PlainPostgresParameterBatch(connection, resourceType);
            daoMap.put(resourceType, dao);
        }
        return dao;
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, StringParameter parameter) throws FHIRPersistenceException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process string parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                + parameter.toString() + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            final String strValue = parameter.getValue();
            final String strValueLower = strValue != null ? strValue.toLowerCase() : null;
            dao.addString(logicalResourceId, parameterNameValue.getParameterNameId(), strValue, strValueLower, parameter.getCompositeId());

            if (parameter.isSystemParam()) {
                systemDao.addString(logicalResourceId, parameterNameValue.getParameterNameId(), strValue, strValueLower);
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "StringParameter", x);
            throw new FHIRPersistenceException("Failed inserting string params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, NumberParameter p) throws FHIRPersistenceException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process number parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                    + p.toString() + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addNumber(logicalResourceId, parameterNameValue.getParameterNameId(), p.getValue(), p.getLowValue(), p.getHighValue(), p.getCompositeId());
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting string params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, QuantityParameter p, CodeSystemValue codeSystemValue) throws FHIRPersistenceException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process quantity parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                    + p.toString() + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addQuantity(logicalResourceId, parameterNameValue.getParameterNameId(), codeSystemValue.getCodeSystemId(), p.getValueCode(), p.getValueNumber(), p.getValueNumberLow(), p.getValueNumberHigh(), p.getCompositeId());
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting quantity params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, LocationParameter p) throws FHIRPersistenceException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process location parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                    + p.toString() + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addLocation(logicalResourceId, parameterNameValue.getParameterNameId(), p.getValueLatitude(), p.getValueLongitude(), p.getCompositeId());
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting location params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, DateParameter p) throws FHIRPersistenceException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process date parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                    + p.toString() + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            final Timestamp valueDateStart = Timestamp.from(p.getValueDateStart());
            final Timestamp valueDateEnd = Timestamp.from(p.getValueDateEnd());
            dao.addDate(logicalResourceId, parameterNameValue.getParameterNameId(), valueDateStart, valueDateEnd, p.getCompositeId());
            if (p.isSystemParam()) {
                systemDao.addDate(logicalResourceId, parameterNameValue.getParameterNameId(), valueDateStart, valueDateEnd, p.getCompositeId());
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "DateParameter", x);
            throw new FHIRPersistenceException("Failed inserting date params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, TokenParameter p,
        CommonTokenValue commonTokenValue) throws FHIRPersistenceException {

        // commonTokenValue can be null
        Long commonTokenValueId = commonTokenValue != null ? commonTokenValue.getCommonTokenValueId() : null;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process token parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                    + p.toString() + "] [" + commonTokenValueId + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addResourceTokenRef(logicalResourceId, parameterNameValue.getParameterNameId(), commonTokenValueId, p.getCompositeId());
            if (p.isSystemParam()) {
                // Currently we store _tag:text as a token value, and because it's also whole-system, we need to add it here
                systemDao.addResourceTokenRef(logicalResourceId, parameterNameValue.getParameterNameId(), commonTokenValue.getCommonTokenValueId());
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting token params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, TagParameter p,
        CommonTokenValue commonTokenValue) throws FHIRPersistenceException {

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process tag parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                    + p.toString() + "] [" + commonTokenValue.getCommonTokenValueId() + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addTag(logicalResourceId, commonTokenValue.getCommonTokenValueId());
            
            if (p.isSystemParam()) {
                systemDao.addTag(logicalResourceId, commonTokenValue.getCommonTokenValueId());
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting tag params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, ProfileParameter p,
        CommonCanonicalValue commonCanonicalValue) throws FHIRPersistenceException {

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process profile parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                    + p.toString() + "] [" + commonCanonicalValue.getCanonicalId() + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addProfile(logicalResourceId, commonCanonicalValue.getCanonicalId(), p.getVersion(), p.getFragment());
            if (p.isSystemParam()) {
                systemDao.addProfile(logicalResourceId, commonCanonicalValue.getCanonicalId(), p.getVersion(), p.getFragment());
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting profile params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, SecurityParameter p,
        CommonTokenValue commonTokenValue) throws FHIRPersistenceException {

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process security parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                    + p.toString() + "] [" + commonTokenValue.getCommonTokenValueId() + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addSecurity(logicalResourceId, commonTokenValue.getCommonTokenValueId());
            
            if (p.isSystemParam()) {
                systemDao.addSecurity(logicalResourceId, commonTokenValue.getCommonTokenValueId());
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting security params for '" + resourceType + "'");
        }
    }

    @Override
    public void process(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue,
        ReferenceParameter parameter, LogicalResourceIdentValue refLogicalResourceId) throws FHIRPersistenceException {

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("process reference parameter [" + resourceType + "] [" + logicalId + "] [" + logicalResourceId + "] [" + parameterNameValue.getParameterName() + "] ["
                    + parameter.toString() + "] [" + refLogicalResourceId.getLogicalResourceId() + "]");
        }

        try {
            PlainPostgresParameterBatch dao = getParameterBatchDao(resourceType);
            dao.addReference(logicalResourceId, parameterNameValue.getParameterNameId(), refLogicalResourceId.getLogicalResourceId(), parameter.getRefVersionId());
        } catch (SQLException x) {
            throw new FHIRPersistenceException("Failed inserting security params for '" + resourceType + "'");
        }
    }
}
