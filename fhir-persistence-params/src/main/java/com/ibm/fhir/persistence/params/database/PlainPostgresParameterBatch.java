/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.MetricHandle;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.database.utils.common.PreparedStatementHelper;
import com.ibm.fhir.persistence.params.api.ParamMetrics;

/**
 * Parameter batch statements configured for a given resource type
 * using the plain schema variant
 */
public class PlainPostgresParameterBatch {
    private static final Logger logger = Logger.getLogger(PlainPostgresParameterBatch.class.getName());

    private final Connection connection;
    private final String resourceType;

    private PreparedStatement strings;
    private int stringCount;

    private PreparedStatement numbers;
    private int numberCount;

    private PreparedStatement dates;
    private int dateCount;

    private PreparedStatement quantities;
    private int quantityCount;

    private PreparedStatement locations;
    private int locationCount;

    private PreparedStatement resourceTokenRefs;
    private int resourceTokenRefCount;

    private PreparedStatement tags;
    private int tagCount;

    private PreparedStatement profiles;
    private int profileCount;

    private PreparedStatement security;
    private int securityCount;

    private PreparedStatement refs;
    private int refCount;

    /**
     * Public constructor
     * @param c
     * @param resourceType
     */
    public PlainPostgresParameterBatch(Connection c, String resourceType) {
        this.connection = c;
        this.resourceType = resourceType;
    }

    /**
     * Push the current batch
     */
    public void pushBatch() throws SQLException {
        if (stringCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_STRINGS.name())) {
                strings.executeBatch();
                pushMetric.accumulateItems(stringCount);
                stringCount = 0;
            }
        }

        if (numberCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_NUMBERS.name())) {
                numbers.executeBatch();
                pushMetric.accumulateItems(numberCount);
                numberCount = 0;
            }
        }
        if (dateCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_DATES.name())) {
                dates.executeBatch();
                pushMetric.accumulateItems(dateCount);
                dateCount = 0;
            }
        }
        if (quantityCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_QUANTITIES.name())) {
                quantities.executeBatch();
                pushMetric.accumulateItems(quantityCount);
                quantityCount = 0;
            }
        }
        if (locationCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_LOCATIONS.name())) {
                locations.executeBatch();
                pushMetric.accumulateItems(locationCount);
                locationCount = 0;
            }
        }
        if (resourceTokenRefCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_TOKENS.name())) {
                resourceTokenRefs.executeBatch();
                pushMetric.accumulateItems(resourceTokenRefCount);
                resourceTokenRefCount = 0;
            }
        }
        if (tagCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_TAGS.name())) {
                tags.executeBatch();
                pushMetric.accumulateItems(tagCount);
                tagCount = 0;
            }
        }
        if (profileCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_PROFILES.name())) {
                profiles.executeBatch();
                pushMetric.accumulateItems(profileCount);
                profileCount = 0;
            }
        }
        if (securityCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_SECURITY.name())) {
                security.executeBatch();
                pushMetric.accumulateItems(securityCount);
                securityCount = 0;
            }
        }
        if (refCount > 0) {
            try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_REFERENCES.name())) {
                refs.executeBatch();
                pushMetric.accumulateItems(refCount);
                refCount = 0;
            }
        }
    }

    /**
     * Resets the state of the DAO by closing all statements and
     * setting any batch counts to 0
     */
    public void close() {
        if (strings != null) {
            try {
                strings.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                strings = null;
                stringCount = 0;
            }
        }

        if (numbers != null) {
            try {
                numbers.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                numbers = null;
                numberCount = 0;
            }
        }

        if (dates != null) {
            try {
                dates.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                dates = null;
                dateCount = 0;
            }
        }

        if (quantities != null) {
            try {
                quantities.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                quantities = null;
                quantityCount = 0;
            }
        }

        if (locations != null) {
            try {
                locations.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                locations = null;
                locationCount = 0;
            }
        }

        if (resourceTokenRefs != null) {
            try {
                resourceTokenRefs.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                resourceTokenRefs = null;
                resourceTokenRefCount = 0;
            }
        }
        if (tags != null) {
            try {
                tags.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                tags = null;
                tagCount = 0;
            }            
        }
        if (profiles != null) {
            try {
                profiles.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                profiles = null;
                profileCount = 0;
            }            
        }
        if (security != null) {
            try {
                security.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                security = null;
                securityCount = 0;
            }
        }
        if (refs != null) {
            try {
                refs.close();
            } catch (SQLException x) {
                // NOP
            } finally {
                refs = null;
                refCount = 0;
            }
        }
    }

    /**
     * Set the compositeId on the given PreparedStatement, handling a value if necessary
     * @param ps
     * @param index
     * @param compositeId
     * @throws SQLException
     */
    private void setComposite(PreparedStatement ps, int index, Integer compositeId) throws SQLException {
        if (compositeId != null) {
            ps.setInt(index, compositeId);
        } else {
            ps.setNull(index, Types.INTEGER);
        }
    }
    /**
     * Utility method to set a string value and handle null
     * @param ps
     * @param index
     * @param value
     * @throws SQLException
     */
    private void setString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value);
        }
    }

    /**
     * Add a string parameter value to the batch statement
     * @param logicalResourceId
     * @param parameterNameId
     * @param strValue
     * @param strValueLower
     * @param compositeId
     * @throws SQLException
     */
    public void addString(long logicalResourceId, int parameterNameId, String strValue, String strValueLower, Integer compositeId) throws SQLException {
        if (strings == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertString = "INSERT INTO " + tablePrefix + "_str_values (parameter_name_id, str_value, str_value_lcase, logical_resource_id, composite_id) VALUES (?,?,?,?,?)";
            strings = connection.prepareStatement(insertString);
        }

        PreparedStatementHelper psh = new PreparedStatementHelper(strings);
        psh.setInt(parameterNameId)
            .setString(strValue)
            .setString(strValueLower)
            .setLong(logicalResourceId)
            .setInt(compositeId)
            .addBatch();
        stringCount++;
    }

    /**
     * Add a number parameter value to the batch statement
     * @param logicalResourceId
     * @param parameterNameId
     * @param value
     * @param valueLow
     * @param valueHigh
     * @param compositeId
     * @throws SQLException
     */
    public void addNumber(long logicalResourceId, int parameterNameId, BigDecimal value, BigDecimal valueLow, BigDecimal valueHigh, Integer compositeId) throws SQLException {
        if (numbers == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertNumber = "INSERT INTO " + tablePrefix + "_number_values (parameter_name_id, number_value, number_value_low, number_value_high, logical_resource_id, composite_id) VALUES (?,?,?,?,?,?)";
            numbers = connection.prepareStatement(insertNumber);
        }
        numbers.setInt(1, parameterNameId);
        numbers.setBigDecimal(2, value);
        numbers.setBigDecimal(3, valueLow);
        numbers.setBigDecimal(4, valueHigh);
        numbers.setLong(5, logicalResourceId);
        setComposite(numbers, 6, compositeId);
        numbers.addBatch();
        numberCount++;
    }

    /**
     * Add a date parameter value to the batch statement
     * @param logicalResourceId
     * @param parameterNameId
     * @param dateStart
     * @param dateEnd
     * @param compositeId
     * @throws SQLException
     */
    public void addDate(long logicalResourceId, int parameterNameId, Timestamp dateStart, Timestamp dateEnd, Integer compositeId) throws SQLException {
        if (dates == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertDate = "INSERT INTO " + tablePrefix + "_date_values (parameter_name_id, date_start, date_end, logical_resource_id, composite_id) VALUES (?,?,?,?,?)";
            dates = connection.prepareStatement(insertDate);
        }

        final Calendar UTC = CalendarHelper.getCalendarForUTC();
        dates.setInt(1, parameterNameId);
        dates.setTimestamp(2, dateStart, UTC);
        dates.setTimestamp(3, dateEnd, UTC);
        dates.setLong(4, logicalResourceId);
        setComposite(dates, 5, compositeId);
        dates.addBatch();
        dateCount++;
    }

    /**
     * Add a quantity parameter value to the batch statement
     * @param logicalResourceId
     * @param parameterNameId
     * @param codeSystemId
     * @param valueCode
     * @param valueNumber
     * @param valueNumberLow
     * @param valueNumberHigh
     * @param compositeId
     * @throws SQLException
     */
    public void addQuantity(long logicalResourceId, int parameterNameId, Integer codeSystemId, String valueCode, BigDecimal valueNumber, BigDecimal valueNumberLow, BigDecimal valueNumberHigh, Integer compositeId) throws SQLException {
        if (quantities == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertQuantity = "INSERT INTO " + tablePrefix + "_quantity_values (parameter_name_id, code_system_id, code, quantity_value, quantity_value_low, quantity_value_high, logical_resource_id, composite_id) VALUES (?,?,?,?,?,?,?,?)";
            quantities = connection.prepareStatement(insertQuantity);
        }

        quantities.setInt(1, parameterNameId);
        quantities.setInt(2, codeSystemId);
        quantities.setString(3, valueCode);
        quantities.setBigDecimal(4, valueNumber);
        quantities.setBigDecimal(5, valueNumberLow);
        quantities.setBigDecimal(6, valueNumberHigh);
        quantities.setLong(7, logicalResourceId);
        setComposite(quantities, 8, compositeId);
        quantities.addBatch();
        quantityCount++;
    }

    /**
     * Add a location parameter value to the batch statement
     * 
     * @param logicalResourceId
     * @param parameterNameId
     * @param lat
     * @param lng
     * @param compositeId
     * @throws SQLException
     */
    public void addLocation(long logicalResourceId, int parameterNameId, Double lat, Double lng, Integer compositeId) throws SQLException {
        if (locations == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertLocation = "INSERT INTO " + tablePrefix + "_latlng_values (parameter_name_id, latitude_value, longitude_value, logical_resource_id, composite_id) VALUES (?,?,?,?,?)";
            locations = connection.prepareStatement(insertLocation);
        }

        locations.setInt(1, parameterNameId);
        locations.setDouble(2, lat);
        locations.setDouble(3, lng);
        locations.setLong(4, logicalResourceId);
        setComposite(locations, 5, compositeId);
        locations.addBatch();
        locationCount++;
    }

    /**
     * Add a token parameter value to the batch statement
     * 
     * @param logicalResourceId
     * @param parameterNameId
     * @param commonTokenValueId can be null
     * @param compositeId can be null
     * @throws SQLException
     */
    public void addResourceTokenRef(long logicalResourceId, int parameterNameId, Long commonTokenValueId, Integer compositeId) throws SQLException {
        if (resourceTokenRefs == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String tokenString = "INSERT INTO " + tablePrefix + "_resource_token_refs (parameter_name_id, common_token_value_id, logical_resource_id, composite_id) VALUES (?,?,?,?)";
            resourceTokenRefs = connection.prepareStatement(tokenString);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(resourceTokenRefs);
        psh.setInt(parameterNameId)
            .setLong(commonTokenValueId)
            .setLong(logicalResourceId)
            .setInt(compositeId);
        psh.addBatch();
        resourceTokenRefCount++;
    }

    /**
     * Add a tag parameter value to the batch statement
     * 
     * @param logicalResourceId
     * @param commonTokenValueId
     * @throws SQLException
     */
    public void addTag(long logicalResourceId, long commonTokenValueId) throws SQLException {
        if (tags == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String tokenString = "INSERT INTO " + tablePrefix + "_tags (common_token_value_id, logical_resource_id) VALUES (?,?)";
            tags = connection.prepareStatement(tokenString);
        }
        tags.setLong(1, commonTokenValueId);
        tags.setLong(2, logicalResourceId);
        tags.addBatch();
        tagCount++;
    }

    /**
     * Add a profile parameter value to the batch statement
     * 
     * @param logicalResourceId
     * @param canonicalId
     * @param version
     * @param fragment
     * @throws SQLException
     */
    public void addProfile(long logicalResourceId, long canonicalId, String version, String fragment) throws SQLException {
        if (profiles == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String tokenString = "INSERT INTO " + tablePrefix + "_profiles (canonical_id, logical_resource_id, version, fragment) VALUES (?,?,?,?)";
            profiles = connection.prepareStatement(tokenString);
        }
        profiles.setLong(1, canonicalId);
        profiles.setLong(2, logicalResourceId);
        setString(profiles, 3, version);
        setString(profiles, 4, fragment);
        profiles.addBatch();
        profileCount++;
    }

    /**
     * Add a security parameter value to the batch statement
     * @param logicalResourceId
     * @param commonTokenValueId
     * @throws SQLException
     */
    public void addSecurity(long logicalResourceId, long commonTokenValueId) throws SQLException {
        if (security == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String INS = "INSERT INTO " + tablePrefix + "_security (common_token_value_id, logical_resource_id) VALUES (?,?)";
            security = connection.prepareStatement(INS);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(security);
        psh.setLong(commonTokenValueId)
            .setLong(logicalResourceId)
            .addBatch();
        securityCount++;
    }

    /**
     * Add a reference parameter value to the batch statement
     * 
     * @param logicalResourceId
     * @param refLogicalResourceId
     * @param refVersionId
     */
    public void addReference(long logicalResourceId, int parameterNameId, long refLogicalResourceId, Integer refVersionId) throws SQLException {
        logger.fine(() -> "Adding reference: parameterNameId:" + parameterNameId + " refLogicalResourceId:" + refLogicalResourceId + " refVersionId:" + refVersionId);
        if (refs == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertString = "INSERT INTO " + tablePrefix + "_ref_values (parameter_name_id, logical_resource_id, ref_logical_resource_id, ref_version_id) VALUES (?,?,?,?)";
            refs = connection.prepareStatement(insertString);
        }
        PreparedStatementHelper psh = new PreparedStatementHelper(refs);
        psh.setInt(parameterNameId)
            .setLong(logicalResourceId)
            .setLong(refLogicalResourceId)
            .setInt(refVersionId)
            .addBatch();
        refCount++;
    }
}