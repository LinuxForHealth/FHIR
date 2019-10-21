/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.PartitionInfo;
import com.ibm.fhir.database.utils.common.DateMath;

/**
 * Access to the DB2 catalog tables
 */
public class Db2CatalogDAO {
    
    // The catalog schema
    private final String catalogSchema;
    
    private final IDatabaseTranslator writer;

    public Db2CatalogDAO(IDatabaseTranslator w) {
        this.writer = w;
        this.catalogSchema = "SYSCAT";
    }
    
    /**
     * So that we can test in Derby
     * @param catalogSchema
     */
    public Db2CatalogDAO(IDatabaseTranslator w, String catalogSchema) {
        this.writer = w;
        this.catalogSchema = catalogSchema;
    }
    
    /**
     * Get the list of partitions. Take special note of the low and high values, because
     * they include string markers e.g.
     * <CODE>
        PNAME            SEQNO       LOWINCLUSIVE LOWVALUE                         HIGHINCLUSIVE HIGHVALUE                       
        ---------------- ----------- ------------ -------------------------------- ------------- --------------------------------
        PART0                      0 Y            '2017-01-01-00.00.00.000000'     N             '2017-02-01-00.00.00.000000'    
        PART1                      1 Y            '2017-02-01-00.00.00.000000'     N             '2017-03-01-00.00.00.000000'    
        PART2                      2 Y            '2017-03-01-00.00.00.000000'     N             '2017-04-01-00.00.00.000000'    
        </CODE>
     * @param c
     * @param tableName
     * @return
     */
    public List<PartitionInfo> getPartitionList(Connection c, String schema, String tableName) throws SQLException {
        final String SQL = ""
            + "SELECT datapartitionname, seqno, lowinclusive, lowvalue, highinclusive, highvalue "
            + "  FROM " + catalogSchema + ".DATAPARTITIONS"
            + " WHERE tabschema = UCASE(?)"
            + "   AND tabname = UCASE(?)"
            + " ORDER BY seqno";
        
        List<PartitionInfo> result = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, schema);
            ps.setString(2, tableName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PartitionInfo pi = new PartitionInfo();
                pi.setDataPartitionName(rs.getString(1));
                pi.setSeqno(rs.getInt(2));
                pi.setLowInclusive("Y".equals(rs.getString(3)));
                pi.setLowValue(unwrap(rs.getString(4)));
                pi.setHighInclusive("Y".equals(rs.getString(5)));
                pi.setHighValue(unwrap(rs.getString(6)));
                if (!rs.wasNull()) {
                    result.add(pi);
                }
            }
        }
        
        return result;
    }

    /**
     * The high/low values look like this: '2017-01-01-00.00.00.000000'.
     * We want this: 2017-01-01
     * @param value
     * @return
     */
    public static String unwrap(final String value) {
        if (value == null || value.length() < 2) {
            return value;
        }
        else if (value.charAt(0) == '\'' && value.length() >= 11) {
            return value.substring(1, 11); // 2017-01-01
        }
        else {
            return value;
        }
    }

    /**
     * Bolt on a new partition to the given table
     * @param c
     * @param prefix
     * @param schema
     * @param table
     * @param lowValue
     * @param highValue
     * @throws SQLException
     */
    public void addPartition(Connection c, final String prefix, final String schema, final String table, final Date lowValue, final Date highValue) throws SQLException {
        String partitionName = prefix + DateMath.format2(lowValue);
        String highValueStr = DateMath.format(highValue);
        String lowValueStr = DateMath.format(lowValue);
        
        // RTC_33190 Begin: add partition failed due to missing '' around highValueStr, and partitionName can not be used as parameter.
        // Because both partitionName and highValueStr are not gotten from user input, so there will be no sql injection risk without using ps.setString.
        final String ddl = "ALTER TABLE " + schema + "." + table 
            + " ADD PARTITION " + partitionName + " STARTING FROM '" + lowValueStr + "' INCLUSIVE  ENDING AT '" + highValueStr + "' EXCLUSIVE";
        
        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        }
        // RTC_33190 End
    }
    
    /**
     * Drop the named partition from the table. Obtains a table (exclusive) lock before
     * proceeding
     * @param c
     * @param schema
     * @param table
     * @param partitionName
     * @throws SQLException
     */
    public void detachPartition(Connection c, final String schema, final String table, final String partitionName) throws SQLException {
        // Get exclusive access to the table so that we can do all this work together
        final String tableName = schema + "." + table;
        final String LOCK_TABLE = "LOCK TABLE " + tableName + " IN EXCLUSIVE MODE";
        try (Statement s = c.createStatement()) {
            s.executeUpdate(LOCK_TABLE);
        }

        // Make up a brand new table name we detach into. This table will later be dropped
        String tmp = UUID.randomUUID().toString().replace("-", "");
        final String detachedName = schema + ".DRP_" + table + "_" + tmp;

        // Now detach the partition
        final String DETACH = "ALTER TABLE " + tableName + " DETACH PARTITION " + partitionName + " INTO " + detachedName;
        try (Statement s = c.createStatement()) {
            s.executeUpdate(DETACH);
        }
        
        // The table detachedName will be dropped later
    }

    /**
     * Get a list of all the table names which come from the detach partition operation
     * @param connection
     * @param schema
     * @param table
     * @return
     */
    public List<String> getDetachedTableList(Connection connection, String schema, String table) throws SQLException {
        List<String> result = new ArrayList<>();
        final String SQL = ""
                + "SELECT tabname FROM " + catalogSchema + ".tables "
                + " WHERE tabschema = ? "
                + "   AND tabname LIKE ?";

        final String like = "DRP_" + table + "_%";
        try (PreparedStatement ps = connection.prepareStatement(SQL)) {
            ps.setString(1, schema);
            ps.setString(2, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        }
                
        return result;
    }

    /**
     * Drop the named table
     * @param connection
     * @param schemaTable the schema qualified table name
     */
    public void dropTable(Connection connection, final String schemaTable) throws SQLException {
        final String DROP_TABLE = "DROP TABLE " + schemaTable;
        try (Statement s = connection.createStatement()) {
            try {
                s.executeUpdate(DROP_TABLE);
            }
            catch (SQLException x) {
                // The table may not exist, which is fine. Then this is a NOP (idempotent)
                if (!writer.isUndefinedName(x)) {
                    throw x;
                }
            }
        }
        
    }
}
