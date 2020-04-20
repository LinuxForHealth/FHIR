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

    public enum FHIRResourceDAOType {
        DB2("db2"),
        DERBY("derby"),
        POSTGRESQL("postgresql");

        private final String value;

        FHIRResourceDAOType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static FHIRResourceDAOType of(Connection con) throws SQLException, IllegalArgumentException {
            String dbUrl = con.getMetaData().getURL();
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
        switch (FHIRResourceDAOType.of(con)) {
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


    public static ResourceDAO getResourceDAO (Connection con) throws IllegalArgumentException, SQLException {
        ResourceDAO resourceDAO = null;
        switch (FHIRResourceDAOType.of(con)) {
            case DB2:
                resourceDAO = new ResourceDAOImpl(con);
                break;
            case DERBY:
                resourceDAO = new DerbyResourceDAO(con);
                break;
            case POSTGRESQL:
                resourceDAO = new PostgreSqlResourceDAO(con);
                break;
        }
        return resourceDAO;
    }

}
