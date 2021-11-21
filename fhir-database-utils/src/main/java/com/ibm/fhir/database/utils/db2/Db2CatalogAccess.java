/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.ICatalogAccess;
import com.ibm.fhir.database.utils.api.PartitionInfo;
import com.ibm.fhir.database.utils.common.DateMath;

/**
 * Abstracts the partition maintenance operations to facilitate better unit testing
 * of the higher business function layers
 */
public class Db2CatalogAccess implements ICatalogAccess {
    private static final Logger logger = Logger.getLogger(Db2CatalogAccess.class.getName());

    private Connection connection;
    private Db2CatalogDAO dao = new Db2CatalogDAO(new Db2Translator());

    public Db2CatalogAccess(Connection c) {
        this.connection = c;
    }

    @Override
    public List<PartitionInfo> getPartitionList(String schema, String table) {
        try {
            return dao.getPartitionList(connection, schema, table);
        }
        catch (SQLException x) {
            throw new DataAccessException(x);
        }
    }

    @Override
    public void addMonthPartition(String schema, String table, Date lowValue) {
        try {
            logger.info("Adding partition 'M"  + DateMath.format2(lowValue) + "' to " + schema + "." + table);
            dao.addPartition(connection, "M", schema, table, lowValue, DateMath.addMonths(lowValue, 1));
        }
        catch (SQLException x) {
            throw new DataAccessException(x);
        }
    }

    @Override
    public void addDayPartition(String schema, String table, Date lowValue) {
        try {
            logger.info("Adding partition 'D"  + DateMath.format2(lowValue) + "' to " + schema + "." + table);
            dao.addPartition(connection, "D", schema, table, lowValue, DateMath.addDays(lowValue, 1));
        }
        catch (SQLException x) {
            throw new DataAccessException(x);
        }
    }

    @Override
    public void dropPartition(String schema, String table, PartitionInfo pi) {
        try {
            logger.info("Detaching partition '" + pi.getDataPartitionName() + "' from " + schema + "." + table);
            dao.detachPartition(connection, schema, table, pi.getDataPartitionName());
        }
        catch (SQLException x) {
            throw new DataAccessException(x);
        }
    }

    @Override
    public void dropDetachedPartitions(String schema, String table, int partMaintBatchSize) {
        // Get a list of all the tables which have been created from
        // partitions which have been detached and drop them
        int count = 0;
        try {
            List<String> tables = dao.getDetachedTableList(connection, schema, table);
            for (String tbl: tables) {
                final String schemaTable = schema + "." + tbl;
                logger.info("Dropping detached partition table: " + schemaTable);
                dao.dropTable(connection, schemaTable);
                if (++count == partMaintBatchSize) {
                    break;
                }
            }
        }
        catch (SQLException x) {
            throw new DataAccessException(x);
        }
    }

    @Override
    public void commitBatch() {
        try {
            logger.info("Committing batch partition maintanence operations!");
            this.connection.commit();
        } catch (SQLException x) {
            throw new DataAccessException(x);
        }
    }

}
