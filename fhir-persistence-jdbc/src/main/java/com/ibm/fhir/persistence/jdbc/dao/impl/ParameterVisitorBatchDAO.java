/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SEARCH_ENABLE_LEGACY_WHOLE_SYSTEM_SEARCH_PARAMS;
import static com.ibm.fhir.search.SearchConstants.PROFILE;
import static com.ibm.fhir.search.SearchConstants.SECURITY;
import static com.ibm.fhir.search.SearchConstants.TAG;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dto.CompositeParmVal;
import com.ibm.fhir.persistence.jdbc.dto.DateParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValueVisitor;
import com.ibm.fhir.persistence.jdbc.dto.LocationParmVal;
import com.ibm.fhir.persistence.jdbc.dto.NumberParmVal;
import com.ibm.fhir.persistence.jdbc.dto.QuantityParmVal;
import com.ibm.fhir.persistence.jdbc.dto.ReferenceParmVal;
import com.ibm.fhir.persistence.jdbc.dto.StringParmVal;
import com.ibm.fhir.persistence.jdbc.dto.TokenParmVal;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import com.ibm.fhir.persistence.jdbc.util.CanonicalSupport;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;
import com.ibm.fhir.search.util.SearchHelper;

/**
 * Batch insert into the parameter values tables. Avoids having to create one stored procedure
 * per resource type, because the row type array approach apparently won't work with dynamic
 * SQL (EXECUTE ... USING ...). Unfortunately this means we have more database round-trips, we
 * don't have a choice.
 */
public class ParameterVisitorBatchDAO implements ExtractedParameterValueVisitor, AutoCloseable {
    private static final Logger logger = Logger.getLogger(ParameterVisitorBatchDAO.class.getName());

    // the connection to use for the inserts
    private final Connection connection;

    // the max number of rows we accumulate for a given statement before we submit the batch
    private final int batchSize;

    // Enable the feature to store whole system parameters
    private final boolean storeWholeSystemParams = true;

    // FK to the logical resource for the parameters being added
    private final long logicalResourceId;

    // Maintainers: remember to close all statements in AutoCloseable#close()
    private final String insertString;
    private final PreparedStatement strings;
    private int stringCount;

    private final String insertNumber;
    private final PreparedStatement numbers;
    private int numberCount;

    private final String insertDate;
    private final PreparedStatement dates;
    private int dateCount;

    private final String insertQuantity;
    private final PreparedStatement quantities;
    private int quantityCount;

    // rarely used so no need for {@code java.sql.PreparedStatement} or batching on this one
    // even on Location resources its only there once by default
    private final String insertLocation;

    // Searchable string attributes stored at the system level
    private final PreparedStatement systemStrings;
    private int systemStringCount;

    // Searchable date attributes stored at the system level
    private final PreparedStatement systemDates;
    private int systemDateCount;

    // DAO for handling parameters stored as token values (including system-level token search params)
    private final IResourceReferenceDAO resourceReferenceDAO;

    // Collect a list of token values to process in one go
    private final List<ResourceTokenValueRec> tokenValueRecs = new ArrayList<>();

    // Tags are now stored in their own tables
    private final List<ResourceTokenValueRec> tagTokenRecs = new ArrayList<>();

    // Security params are now stored in their own tables
    private final List<ResourceTokenValueRec> securityTokenRecs = new ArrayList<>();

    // Profiles are now stored in their own tables
    private final List<ResourceProfileRec> profileRecs = new ArrayList<>();

    // The table prefix (resourceType)
    private final String tablePrefix;

    // The common cache for all our identity lookup needs
    private final JDBCIdentityCache identityCache;

    // If not null, we stash certain parameter data here for insertion later
    private final ParameterTransactionDataImpl transactionData;

    // tracks the number of composites so we know what next composite_id to use
    int compositeIdCounter = 0;

    // when set, this value is added as the composite_id value for each parameter we store
    Integer currentCompositeId = null;

    // Supports slightly more useful error messages if we hit a nested composite
    String currentCompositeParameterName = null;

    // Enable use of legacy whole-system search parameters for the search request
    private final boolean legacyWholeSystemSearchParamsEnabled;

    /**
     * Public constructor
     * @param c
     * @param resourceId
     */
    public ParameterVisitorBatchDAO(Connection c, String adminSchemaName, String tablePrefix, boolean multitenant, long logicalResourceId, int batchSize,
            JDBCIdentityCache identityCache, IResourceReferenceDAO resourceReferenceDAO, ParameterTransactionDataImpl ptdi) throws SQLException {
        if (batchSize < 1) {
            throw new IllegalArgumentException("batchSize must be >= 1");
        }

        this.connection = c;
        this.logicalResourceId = logicalResourceId;
        this.batchSize = batchSize;
        this.identityCache = identityCache;
        this.resourceReferenceDAO = resourceReferenceDAO;
        this.tablePrefix = tablePrefix;
        this.transactionData = ptdi;
        this.legacyWholeSystemSearchParamsEnabled =
                FHIRConfigHelper.getBooleanProperty(PROPERTY_SEARCH_ENABLE_LEGACY_WHOLE_SYSTEM_SEARCH_PARAMS, false);

        insertString = multitenant ?
                "INSERT INTO " + tablePrefix + "_str_values (mt_id, parameter_name_id, str_value, str_value_lcase, logical_resource_id, composite_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?,?)"
                :
                "INSERT INTO " + tablePrefix + "_str_values (parameter_name_id, str_value, str_value_lcase, logical_resource_id, composite_id) VALUES (?,?,?,?,?)";
        strings = c.prepareStatement(insertString);

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
     * Look up the normalized id for the parameter, adding it to the parameter_names table if it doesn't yet exist
     * @param parameterName
     * @return
     */
    protected int getParameterNameId(String parameterName) throws FHIRPersistenceException {
        return identityCache.getParameterNameId(parameterName);
    }

    /**
     * Looks up the code system. If it doesn't exist, adds it to the database
     * @param codeSystem
     * @return
     */
    protected int getCodeSystemId(String codeSystem) throws FHIRPersistenceException {
        return identityCache.getCodeSystemId(codeSystem);
    }

    @Override
    public void visit(StringParmVal param) throws FHIRPersistenceException {
        String parameterName = param.getName();
        String value = param.getValueString();

        if (PROFILE.equals(parameterName)) {
            // profile canonicals are now stored in their own tables.
            processProfile(param);
            if (!legacyWholeSystemSearchParamsEnabled) {
                // Don't store in legacy search param tables
                return;
            }
        }

        while (value != null && value.getBytes().length > FhirSchemaConstants.MAX_SEARCH_STRING_BYTES) {
            // keep chopping the string in half until its byte representation fits inside
            // the VARCHAR
            value = value.substring(0, value.length() / 2);
        }

        try {
            int parameterNameId = getParameterNameId(parameterName);
            if (storeWholeSystem(param)) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("systemStringValue: " + parameterName + "[" + parameterNameId + "], " + value);
                }

                systemStrings.setInt(1, parameterNameId);
                if (value != null) {
                    systemStrings.setString(2, value);
                    systemStrings.setString(3, SearchHelper.normalizeForSearch(value));
                }
                else {
                    systemStrings.setNull(2, Types.VARCHAR);
                    systemStrings.setNull(3, Types.VARCHAR);
                }
                systemStrings.setLong(4, logicalResourceId);
                systemStrings.addBatch();

                if (++systemStringCount == this.batchSize) {
                    systemStrings.executeBatch();
                    systemStringCount = 0;
                }
            }

            // always store at the resource-specific level
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("stringValue: " + parameterName + "[" + parameterNameId + "], " + value);
            }

            setStringParms(strings, parameterNameId, value);
            strings.addBatch();

            if (++stringCount == this.batchSize) {
                strings.executeBatch();
                stringCount = 0;
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "=" + value, x);
        }
    }

    private void setStringParms(PreparedStatement insert, int parameterNameId, String value) throws SQLException {
        insert.setInt(1, parameterNameId);
        if (value != null) {
            insert.setString(2, value);
            insert.setString(3, SearchHelper.normalizeForSearch(value));
        } else {
            insert.setNull(2, Types.VARCHAR);
            insert.setNull(3, Types.VARCHAR);
        }
        insert.setLong(4, logicalResourceId);
        setCompositeId(insert, 5);
    }

    /**
     * Special case to store profile strings as canonical uri values in their own table
     * @param param
     * @throws FHIRPersistenceException
     */
    private void processProfile(StringParmVal param) throws FHIRPersistenceException {
        final String parameterName = param.getName();
        final int resourceTypeId = identityCache.getResourceTypeId(param.getResourceType());

        // Parse the parameter value to extract the URI|VERSION#FRAGMENT pieces
        ResourceProfileRec rec = CanonicalSupport.makeResourceProfileRec(parameterName, param.getResourceType(), resourceTypeId, this.logicalResourceId, param.getValueString(), param.isWholeSystem());
        if (transactionData != null) {
            transactionData.addValue(rec);
        } else {
            profileRecs.add(rec);
        }
    }

    /**
     * Set the composite_id column value or null if required
     * @param ps the statement
     * @param idx the column index
     * @throws SQLException
     */
    private void setCompositeId(PreparedStatement ps, int idx) throws SQLException {
        if (this.currentCompositeId != null) {
            ps.setInt(idx, this.currentCompositeId);
        } else {
            ps.setNull(idx, Types.INTEGER);
        }
    }

    @Override
    public void visit(NumberParmVal param) throws FHIRPersistenceException {
        String parameterName = param.getName();
        BigDecimal value = param.getValueNumber();
        BigDecimal valueLow = param.getValueNumberLow();
        BigDecimal valueHigh = param.getValueNumberHigh();

        // System-level number search parameters are not supported
        if (storeWholeSystem(param)) {
            String msg = "System-level number search parameters are not supported: " + parameterName;
            logger.warning(msg);
            throw new IllegalArgumentException(msg);
        }

        try {
            int parameterNameId = getParameterNameId(parameterName);

            if (logger.isLoggable(Level.FINE)) {
                logger.fine("numberValue: " + parameterName + "[" + parameterNameId + "], "
                        + value + " [" + valueLow + ", " + valueHigh + "]");
            }

            setNumberParms(numbers, parameterNameId, value, valueLow, valueHigh);
            numbers.addBatch();

            if (++numberCount == this.batchSize) {
                numbers.executeBatch();
                numberCount = 0;
            }
        }
        catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "={" + value + " ["+ valueLow + "," + valueHigh + "}", x);
        }
    }

    private void setNumberParms(PreparedStatement insert, int parameterNameId, BigDecimal value, BigDecimal valueLow, BigDecimal valueHigh) throws SQLException {
        insert.setInt(1, parameterNameId);
        insert.setBigDecimal(2, value);
        insert.setBigDecimal(3, valueLow);
        insert.setBigDecimal(4, valueHigh);
        insert.setLong(5, logicalResourceId);
        setCompositeId(insert, 6);
    }

    @Override
    public void visit(DateParmVal param) throws FHIRPersistenceException {
        String parameterName = param.getName();
        Timestamp dateStart = param.getValueDateStart();
        Timestamp dateEnd = param.getValueDateEnd();
        try {
            int parameterNameId = getParameterNameId(parameterName);

            if (storeWholeSystem(param)) {
                // store as a system level search param
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("systemDateValue: " + parameterName + "[" + parameterNameId + "], "
                             + "[" + dateStart + ", " + dateEnd + "]");
                }

                // Insert record into the base level date attribute table
                setDateParms(systemDates, parameterNameId, dateStart, dateEnd);
                systemDates.addBatch();

                if (++systemDateCount == this.batchSize) {
                    systemDates.executeBatch();
                    systemDateCount = 0;
                }
            }

            // always store the param at the resource-specific level
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("dateValue: " + parameterName + "[" + parameterNameId + "], "
                        + "period: [" + dateStart + ", " + dateEnd + "]");
            }

            setDateParms(dates, parameterNameId, dateStart, dateEnd);
            dates.addBatch();

            if (++dateCount == this.batchSize) {
                dates.executeBatch();
                dateCount = 0;
            }
        } catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "={" + dateStart + ", " + dateEnd + "}", x);
        }

    }

    private void setDateParms(PreparedStatement insert, int parameterNameId, Timestamp dateStart, Timestamp dateEnd) throws SQLException {
        final Calendar UTC = CalendarHelper.getCalendarForUTC();
        insert.setInt(1, parameterNameId);
        insert.setTimestamp(2, dateStart, UTC);
        insert.setTimestamp(3, dateEnd, UTC);
        insert.setLong(4, logicalResourceId);
        setCompositeId(insert, 5);
    }

    @Override
    public void visit(TokenParmVal param) throws FHIRPersistenceException {
        String parameterName = param.getName();
        String codeSystem = param.getValueSystem();
        String tokenValue = param.getValueCode();
        try {
            boolean isSystemParam = storeWholeSystem(param);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("tokenValue: " + parameterName + ", " + codeSystem + ", " + tokenValue);
            }

            // Add the new token value to the collection we're building...what's the resourceTypeId?
            final int resourceTypeId = identityCache.getResourceTypeId(param.getResourceType());
            if (tokenValue == null) {
                logger.fine(() -> "tokenValue is NULL for: " + parameterName + ", " + codeSystem);
            }

            // Issue 1683, for composites we now also record the current composite id (can be null)
            ResourceTokenValueRec rec = new ResourceTokenValueRec(parameterName, param.getResourceType(), resourceTypeId, logicalResourceId, codeSystem, tokenValue, this.currentCompositeId, isSystemParam);
            if (TAG.equals(parameterName)) {
                // tag search params are often low-selectivity (many resources sharing the same value) so
                // we put them into their own tables to allow better cardinality estimation by the query
                // optimizer
                if (this.transactionData != null) {
                    this.transactionData.addTagValue(rec);
                } else {
                    this.tagTokenRecs.add(rec);
                }
                if (legacyWholeSystemSearchParamsEnabled) {
                    // Store as legacy search params as well
                    if (this.transactionData != null) {
                        this.transactionData.addValue(rec);
                    } else {
                        this.tokenValueRecs.add(rec);
                    }
                }
            } else if (SECURITY.equals(parameterName)) {
                // search search params are often low-selectivity (many resources sharing the same value) so
                // we put them into their own tables to allow better cardinality estimation by the query
                // optimizer
                if (this.transactionData != null) {
                    this.transactionData.addSecurityValue(rec);
                } else {
                    this.securityTokenRecs.add(rec);
                }
                if (legacyWholeSystemSearchParamsEnabled) {
                    // Store as legacy search params as well
                    if (this.transactionData != null) {
                        this.transactionData.addValue(rec);
                    } else {
                        this.tokenValueRecs.add(rec);
                    }
                }
            } else {
                if (this.transactionData != null) {
                    this.transactionData.addValue(rec);
                } else {
                    this.tokenValueRecs.add(rec);
                }
            }
        } catch (FHIRPersistenceDataAccessException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "=" + codeSystem + ":" + tokenValue, x);
        }
    }

    @Override
    public void visit(QuantityParmVal param) throws FHIRPersistenceException {
        String parameterName = param.getName();
        String code = param.getValueCode();
        String codeSystem = param.getValueSystem();
        BigDecimal quantityValue = param.getValueNumber();
        BigDecimal quantityLow = param.getValueNumberLow();
        BigDecimal quantityHigh = param.getValueNumberHigh();

        // System-level quantity search parameters are not supported
        if (storeWholeSystem(param)) {
            String msg = "System-level quantity search parameters are not supported: " + parameterName;
            logger.warning(msg);
            throw new IllegalArgumentException(msg);
        }

        // Skip anything with a null code, since CODE column is non-nullable,
        // but allow empty code for when no code or unit is specified
        if (code == null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("CODELESS QUANTITY (skipped): " + parameterName + "=" + code + ":" + codeSystem + "{" + quantityValue + ", " + quantityLow + ", " + quantityHigh + "}");
            }
        } else {
            try {
                int parameterNameId = getParameterNameId(parameterName);

                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("quantityValue: " + parameterName + "[" + parameterNameId + "], "
                            + quantityValue + " [" + quantityLow + ", " + quantityHigh + "]");
                }

                setQuantityParms(quantities, parameterNameId, codeSystem, code, quantityValue, quantityLow, quantityHigh);
                quantities.addBatch();

                if (++quantityCount == batchSize) {
                    quantities.executeBatch();
                    quantityCount = 0;
                }
            } catch (FHIRPersistenceDataAccessException x) {
                // wrap the exception so we have more context about the parameter causing the problem
                throw new FHIRPersistenceDataAccessException(parameterName + "=" + code + ":" + codeSystem + "{" + quantityValue + ", " + quantityLow + ", " + quantityHigh + "}", x);
            } catch (SQLException x) {
                throw new FHIRPersistenceDataAccessException(parameterName + "=" + code + ":" + codeSystem + "{" + quantityValue + ", " + quantityLow + ", " + quantityHigh + "}", x);
            }
        }

    }

    private void setQuantityParms(PreparedStatement insert, int parameterNameId, String codeSystem, String code, BigDecimal quantityValue, BigDecimal quantityLow, BigDecimal quantityHigh)
            throws SQLException, FHIRPersistenceException {
        insert.setInt(1, parameterNameId);
        insert.setInt(2, getCodeSystemId(codeSystem));
        insert.setString(3, code);
        insert.setBigDecimal(4, quantityValue);
        insert.setBigDecimal(5, quantityLow);
        insert.setBigDecimal(6, quantityHigh);
        insert.setLong(7, logicalResourceId);
        setCompositeId(insert, 8);
    }

    @Override
    public void visit(LocationParmVal param) throws FHIRPersistenceException {
        String parameterName = param.getName();
        double lat = param.getValueLatitude();
        double lng = param.getValueLongitude();

        // System-level location search parameters are not supported
        if (storeWholeSystem(param)) {
            String msg = "System-level location search parameters are not supported: " + parameterName;
            logger.warning(msg);
            throw new IllegalArgumentException(msg);
        }

        try {
            PreparedStatement insert = connection.prepareStatement(insertLocation);
            setLocationParms(insert, getParameterNameId(parameterName), lat, lng);
            insert.executeUpdate();
        } catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "={" + lat + ", " + lng + "}", x);
        }
    }

    private void setLocationParms(PreparedStatement insert, int parameterNameId, double lat, double lng) throws SQLException, FHIRPersistenceException {
        insert.setInt(1, parameterNameId);
        insert.setDouble(2, lat);
        insert.setDouble(3, lng);
        insert.setLong(4, logicalResourceId);
        setCompositeId(insert, 5);

    }

    @Override
    public void visit(CompositeParmVal compositeParameter) throws FHIRPersistenceException {
        if (this.currentCompositeId != null) {
            // no soup for you
            logger.warning("A compositeParameter '" + currentCompositeParameterName + "' cannot itself contain a composite '" + compositeParameter.getName());
            throw new FHIRPersistenceException("composite parameters cannot themselves contain composites");
        }

        // This composite is a collection of multiple parameters.
        List<ExtractedParameterValue> component = compositeParameter.getComponent();
        this.currentCompositeId = this.compositeIdCounter++;
        this.currentCompositeParameterName = compositeParameter.getName();
        for (ExtractedParameterValue val : component) {
            val.accept(this);
        }

        // Clear the currentCompositeId value so we no longer associate it with other parameters
        this.currentCompositeId = null;
        this.currentCompositeParameterName = null;
    }

    @Override
    public void close() throws Exception {
        // flush any stragglers, remembering to reset each count because
        // close() should be idempotent.
        try {
            if (stringCount > 0) {
                strings.executeBatch();
                stringCount = 0;
            }

            if (numberCount > 0) {
                numbers.executeBatch();
                numberCount = 0;
            }

            if (dateCount > 0) {
                dates.executeBatch();
                dateCount = 0;
            }

            if (quantityCount > 0) {
                quantities.executeBatch();
                quantityCount = 0;
            }

            if (systemStringCount > 0) {
                systemStrings.executeBatch();
                systemStringCount = 0;
            }

            if (systemDateCount > 0) {
                systemDates.executeBatch();
                systemDateCount = 0;
            }
        } catch (SQLException x) {
            SQLException batchException = x.getNextException();
            if (batchException != null) {
                // We're really interested in the underlying cause here
                throw batchException;
            }
            else {
                throw x;
            }
        }

        if (this.transactionData == null) {
            // Not using transaction data, so we need to process collected values right here
            this.resourceReferenceDAO.addNormalizedValues(this.tablePrefix, tokenValueRecs, profileRecs, tagTokenRecs, securityTokenRecs);
        }

        closeStatement(strings);
        closeStatement(numbers);
        closeStatement(dates);
        closeStatement(quantities);
        closeStatement(systemStrings);
        closeStatement(systemDates);
    }

    /**
     * Quietly close the given statement
     * @param ps
     */
    private void closeStatement(PreparedStatement ps) {
        try {
            ps.close();
        } catch (SQLException x) {
            logger.warning("failed to close statement");
        }
    }

    /**
     * Should we store this parameter also at the whole-system search level?
     * @param param
     * @return
     */
    private boolean storeWholeSystem(ExtractedParameterValue param) {
        return storeWholeSystemParams && param.isWholeSystem();
    }

    @Override
    public void visit(ReferenceParmVal rpv) throws FHIRPersistenceException {
        if (rpv.getRefValue() == null) {
            return;
        }

        final String resourceType = this.tablePrefix;
        final String parameterName = rpv.getName();
        Integer resourceTypeId = identityCache.getResourceTypeId(resourceType);
        if (resourceTypeId == null) {
            // resourceType is not sensitive, so it's OK to include in the exception message
            throw new FHIRPersistenceException("Resource type not found in cache: '" + resourceType + "'");
        }


        // The ReferenceValue has already been processed to convert the reference to
        // the required standard form, ready for insertion as a token value.
        ReferenceValue refValue = rpv.getRefValue();

        // Ignore references containing only a "display" element (apparently supported by the spec,
        // but contains nothing useful to store because there's no searchable value).
        String refResourceType = refValue.getTargetResourceType();
        String refLogicalId = refValue.getValue();
        Integer refVersion = refValue.getVersion();
        ResourceTokenValueRec rec;

        if (refValue.getType() == ReferenceType.DISPLAY_ONLY || refValue.getType() == ReferenceType.INVALID) {
            // protect against code regression. Invalid/improper references should be
            // filtered out already.
            logger.warning("Invalid reference parameter type: '" + resourceType + "." + rpv.getName() + "' type=" + refValue.getType().name());
            throw new IllegalArgumentException("Invalid reference parameter value. See server log for details.");
        }

        // reference params are never system-level
        final boolean isSystemParam = false;
        if (refResourceType != null) {
            // Store a token value configured as a reference to another resource
            rec = new ResourceTokenValueRec(parameterName, resourceType, resourceTypeId, logicalResourceId, refResourceType, refLogicalId, refVersion, this.currentCompositeId, isSystemParam);
        } else {
            // stored as a token with the default system
            rec = new ResourceTokenValueRec(parameterName, resourceType, resourceTypeId, logicalResourceId, JDBCConstants.DEFAULT_TOKEN_SYSTEM, refLogicalId, this.currentCompositeId, isSystemParam);
        }

        if (this.transactionData != null) {
            this.transactionData.addValue(rec);
        } else {
            this.tokenValueRecs.add(rec);
        }
    }
}