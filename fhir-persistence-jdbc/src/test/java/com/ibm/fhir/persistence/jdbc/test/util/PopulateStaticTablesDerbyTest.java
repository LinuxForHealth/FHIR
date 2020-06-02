/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.FHIRDbDAOImpl;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * 
 */
public class PopulateStaticTablesDerbyTest {
    private Properties testProps;

    public PopulateStaticTablesDerbyTest() throws Exception {
        this.testProps = TestUtil.readTestProperties("test.jdbc.properties");
    }

    @BeforeClass
    public void startup() throws FHIRPersistenceDBConnectException, SQLException {
        DerbyInitializer derbyInit;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(this.testProps);
            derbyInit.bootstrapDb();
        }
    }

    @Test(groups = { "derby" })
    public void testGetDerbyConnectionAndCheckStaticTables() throws Exception {
        FHIRDbDAO dao = new FHIRDbDAOImpl(testProps);
        boolean result1 = false;
        boolean result2 = false;
        try (Connection connection = dao.getConnection();) {
            assertNotNull(connection);
            try (PreparedStatement stmt =
                    connection.prepareStatement("SELECT COUNT(*) FROM FHIRDATA.PARAMETER_NAMES")) {
                assertTrue(stmt.execute());
                ResultSet resultSet = stmt.getResultSet();
                if (resultSet.next()) {
                    int r = resultSet.getInt(1);
                    if (r > 0) {
                        result1 = true;
                    }
                }
            }
            try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM FHIRDATA.RESOURCE_TYPES")) {
                assertTrue(stmt.execute());
                ResultSet resultSet = stmt.getResultSet();
                if (resultSet.next()) {
                    int r = resultSet.getInt(1);
                    if (r > 0) {
                        result2 = true;
                    }
                }
            }
        }
        assertTrue(result1);
        assertTrue(result2);
    }
}
