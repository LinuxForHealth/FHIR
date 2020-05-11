/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.PartitionInfo;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Functional wrapper to get the catalog information
 *
 */
public class Db2GetPartitionInfo implements IDatabaseStatement {
    private final String catalogSchema;
    private final String tableSchema;

    // The consumer target for handling results
    private final Consumer<PartitionInfo> consumer;

    /**
     * Get partition information for all tables in the tableSchema, using
     * the catalogSchema as the schema containing the DATAPARTITIONS system table
     *
     * @param catalogSchema
     * @param tableSchema
     * @param consumer
     */
    public Db2GetPartitionInfo(String catalogSchema, String tableSchema,
            Consumer<PartitionInfo> consumer) {
        DataDefinitionUtil.assertValidName(catalogSchema);
        this.catalogSchema = catalogSchema;
        this.tableSchema = tableSchema;
        this.consumer = consumer;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        /**
         * Execute the encapsulated query against the database and stream the result data to the
         * configured target
         */

        final String SQL = ""
                + "SELECT tabname, datapartitionname, seqno, lowinclusive, lowvalue, highinclusive, highvalue "
                + " FROM " + catalogSchema + ".DATAPARTITIONS "
                + " WHERE tabschema = UCASE(?) "
                + " ORDER BY tabname, seqno";

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, tableSchema);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PartitionInfo pi = new PartitionInfo();
                pi.setTableName(rs.getString(1));
                pi.setDataPartitionName(rs.getString(2));
                pi.setSeqno(rs.getInt(3));
                pi.setLowInclusive("Y".equals(rs.getString(4)));
                pi.setLowValue(unwrap(rs.getString(5)));
                pi.setHighInclusive("Y".equals(rs.getString(6)));
                pi.setHighValue(unwrap(rs.getString(7)));

                // Hand off the result record to the consumer given to us
                // in the constructor
                consumer.accept(pi);
            }
        }
        catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
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

}
