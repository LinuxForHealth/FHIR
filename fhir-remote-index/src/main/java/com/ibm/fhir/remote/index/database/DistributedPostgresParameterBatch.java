/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import com.ibm.fhir.database.utils.common.CalendarHelper;

/**
 * Parameter batch statements configured for a given resource type
 */
public class DistributedPostgresParameterBatch {
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

    /**
     * Public constructor
     * @param c
     * @param resourceType
     */
    public DistributedPostgresParameterBatch(Connection c, String resourceType) {
        this.connection = c;
        this.resourceType = resourceType;
    }

    /**
     * Push the current batch
     */
    public void pushBatch() throws SQLException {
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
        if (locationCount > 0) {
            locations.executeBatch();
            locationCount = 0;
        }
        if (resourceTokenRefCount > 0) {
            resourceTokenRefs.executeBatch();
            resourceTokenRefCount = 0;
        }
    }

    /**
     * Resets the state of the DAO by closing all statements and
     * setting any batch counts to 0
     */
    public void reset() {
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

    public void addString(long logicalResourceId, int parameterNameId, String strValue, String strValueLower, Integer compositeId, short shardKey) throws SQLException {
        if (strings == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertString = "INSERT INTO " + tablePrefix + "_str_values (parameter_name_id, str_value, str_value_lcase, logical_resource_id, composite_id, shard_key) VALUES (?,?,?,?,?,?)";
            strings = connection.prepareStatement(insertString);
        }

        strings.setInt(1, parameterNameId);
        strings.setString(2, strValue);
        strings.setString(3, strValueLower);
        strings.setLong(4, logicalResourceId);
        setComposite(strings, 5, compositeId);
        strings.setShort(6, shardKey);
        strings.addBatch();
        stringCount++;
    }

    public void addNumber(long logicalResourceId, int parameterNameId, BigDecimal value, BigDecimal valueLow, BigDecimal valueHigh, Integer compositeId, short shardKey) throws SQLException {
        if (numbers == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertNumber = "INSERT INTO " + tablePrefix + "_number_values (parameter_name_id, number_value, number_value_low, number_value_high, logical_resource_id, composite_id, shard_key) VALUES (?,?,?,?,?,?,?)";
            numbers = connection.prepareStatement(insertNumber);
        }
        numbers.setInt(1, parameterNameId);
        numbers.setBigDecimal(2, value);
        numbers.setBigDecimal(3, valueLow);
        numbers.setBigDecimal(4, valueHigh);
        numbers.setLong(5, logicalResourceId);
        setComposite(numbers, 6, compositeId);
        numbers.setShort(7, shardKey);
        numbers.addBatch();
        numberCount++;
    }

    public void addDate(long logicalResourceId, int parameterNameId, Timestamp dateStart, Timestamp dateEnd, Integer compositeId, short shardKey) throws SQLException {
        if (dates == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertDate = "INSERT INTO " + tablePrefix + "_date_values (parameter_name_id, date_start, date_end, logical_resource_id, composite_id, shard_key) VALUES (?,?,?,?,?,?)";
            dates = connection.prepareStatement(insertDate);
        }

        final Calendar UTC = CalendarHelper.getCalendarForUTC();
        dates.setInt(1, parameterNameId);
        dates.setTimestamp(2, dateStart, UTC);
        dates.setTimestamp(3, dateEnd, UTC);
        dates.setLong(4, logicalResourceId);
        setComposite(dates, 5, compositeId);
        dates.setShort(6, shardKey);
        dates.addBatch();
        dateCount++;
    }

    public void addQuantity(long logicalResourceId, int parameterNameId, Integer codeSystemId, String valueCode, BigDecimal valueNumber, BigDecimal valueNumberLow, BigDecimal valueNumberHigh, Integer compositeId, short shardKey) throws SQLException {
        if (quantities == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertQuantity = "INSERT INTO " + tablePrefix + "_quantity_values (parameter_name_id, code_system_id, code, quantity_value, quantity_value_low, quantity_value_high, logical_resource_id, composite_id, shard_key) VALUES (?,?,?,?,?,?,?,?,?)";
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
        quantities.setShort(9, shardKey);
        quantities.addBatch();
        quantityCount++;
    }

    public void addLocation(long logicalResourceId, int parameterNameId, Double lat, Double lng, Integer compositeId, short shardKey) throws SQLException {
        if (locations == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String insertLocation = "INSERT INTO " + tablePrefix + "_latlng_values (parameter_name_id, latitude_value, longitude_value, logical_resource_id, composite_id, shard_key) VALUES (?,?,?,?,?,?)";
            locations = connection.prepareStatement(insertLocation);
        }

        locations.setInt(1, parameterNameId);
        locations.setDouble(2, lat);
        locations.setDouble(3, lng);
        locations.setLong(4, logicalResourceId);
        setComposite(locations, 5, compositeId);
        locations.setShort(6, shardKey);
        locations.addBatch();
        locationCount++;
    }

    public void addResourceTokenRef(long logicalResourceId, int parameterNameId, long commonTokenValueId, Integer refVersionId, Integer compositeId, short shardKey) throws SQLException {
        if (resourceTokenRefs == null) {
            final String tablePrefix = resourceType.toLowerCase();
            final String tokenString = "INSERT INTO " + tablePrefix + "_resource_token_refs (parameter_name_id, common_token_value_id, ref_version_id, logical_resource_id, composite_id, shard_key) VALUES (?,?,?,?,?,?)";
            resourceTokenRefs = connection.prepareStatement(tokenString);
        }
        resourceTokenRefs.setInt(1, parameterNameId);
        resourceTokenRefs.setLong(2, commonTokenValueId);
        setComposite(resourceTokenRefs, 3, refVersionId);
        resourceTokenRefs.setLong(4, logicalResourceId);
        setComposite(resourceTokenRefs, 5, compositeId);
        resourceTokenRefs.setShort(6, shardKey);
        resourceTokenRefs.addBatch();
        resourceTokenRefCount++;
    }
}