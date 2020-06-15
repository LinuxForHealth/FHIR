/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.derby.DerbyResourceDAO;
import com.ibm.fhir.persistence.jdbc.postgresql.PostgreSqlResourceDAO;

public class FHIRResourceDAOFactory {

    // The various DAO Types that are used in the JDBC Persistence layer.
    public enum FHIRResourceDAOType {
        DB2("jdbc:db2:"),
        DERBY("derby"),
        POSTGRESQL("jdbc:postgresql:");

        private final String value;

        FHIRResourceDAOType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static FHIRResourceDAOType of(Connection conn) throws SQLException, IllegalArgumentException {
            String dbUrl = conn.getMetaData().getURL();
            dbUrl = dbUrl.toLowerCase();
            if (dbUrl.contains(DB2.value)) {
                return DB2;
            } else if (dbUrl.contains(DERBY.value)) {
                return DERBY;
            } else if (dbUrl.contains(POSTGRESQL.value)) {
                return POSTGRESQL;
            } else {
                throw new IllegalArgumentException(dbUrl);
            }

        }
    }

   public static ResourceDAO getResourceDAO (Connection conn, TransactionSynchronizationRegistry trxSynchRegistry) throws IllegalArgumentException, SQLException {
        ResourceDAO resourceDAO = null;
        switch (FHIRResourceDAOType.of(conn)) {
            case DB2:
                resourceDAO = new ResourceDAOImpl(trxSynchRegistry);
                break;
            case DERBY:
                resourceDAO = new DerbyResourceDAO(trxSynchRegistry);
                break;
            case POSTGRESQL:
                resourceDAO = new PostgreSqlResourceDAO(trxSynchRegistry);
                break;
        }
        return resourceDAO;
    }

    public static ResourceDAO getResourceDAO (Connection conn) throws IllegalArgumentException, SQLException {
        ResourceDAO resourceDAO = null;
        switch (FHIRResourceDAOType.of(conn)) {
            case DB2:
                resourceDAO = new ResourceDAOImpl(conn);
                break;
            case DERBY:
                resourceDAO = new DerbyResourceDAO(conn);
                break;
            case POSTGRESQL:
                resourceDAO = new PostgreSqlResourceDAO(conn);
                break;
        }
        return resourceDAO;
    }

}
