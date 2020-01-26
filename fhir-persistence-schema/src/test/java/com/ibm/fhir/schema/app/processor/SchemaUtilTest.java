/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.ConnectionDetails;
import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.task.api.ITaskGroup;

/**
 * Tests Schema Util
 */
public class SchemaUtilTest {
    @Test
    public void testSchemaUtil() {
        ITaskGroup group = new ITaskGroup() {

            @Override
            public String getTaskId() {
                return "TEST_ID";
            }

            @Override
            public void addParent(ITaskGroup parent) {
                // No Operation
            }

            @Override
            public void taskCompletionCallback(ITaskGroup taskGroup) {
                // No Operation
            }

        };

        String taskId = SchemaUtil.mapToId(group);
        assertEquals(taskId, "TEST_ID");
    }

    @Test
    public void testLoadDriver() {
        IDatabaseTranslator translator = new IDatabaseTranslator() {
            @Override
            public boolean isDerby() {
                return false;
            }

            @Override
            public String addForUpdate(String sql) {
                return null;
            }

            @Override
            public String globalTempTableName(String tableName) {
                return null;
            }

            @Override
            public String createGlobalTempTable(String ddl) {
                return null;
            }

            @Override
            public boolean isDuplicate(SQLException x) {
                return false;
            }

            @Override
            public boolean isAlreadyExists(SQLException x) {
                return false;
            }

            @Override
            public boolean isLockTimeout(SQLException x) {
                return false;
            }

            @Override
            public boolean isDeadlock(SQLException x) {
                return false;
            }

            @Override
            public boolean isConnectionError(SQLException x) {
                return false;
            }

            @Override
            public DataAccessException translate(SQLException x) {
                return null;
            }

            @Override
            public boolean isUndefinedName(SQLException x) {
                return false;
            }

            @Override
            public void fillProperties(Properties p, ConnectionDetails cd) {
            }

            @Override
            public String timestampDiff(String left, String right, String alias) {
                return null;
            }

            @Override
            public String createSequence(String name, int cache) {
                return null;
            }

            @Override
            public String reorgTableCommand(String tableName) {
                return null;
            }

            @Override
            public String getDriverClassName() {
                return SchemaUtilTest.class.getCanonicalName();
            }

            @Override
            public String getUrl(Properties connectionProperties) {
                return null;
            }

            @Override
            public boolean clobSupportsInline() {
                return false;
            }

        };
        SchemaUtil.loadDriver(translator);
        assert true;
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testLoadDriverInvalid() {
        IDatabaseTranslator translator = new IDatabaseTranslator() {
            @Override
            public boolean isDerby() {
                return false;
            }

            @Override
            public String addForUpdate(String sql) {
                return null;
            }

            @Override
            public String globalTempTableName(String tableName) {
                return null;
            }

            @Override
            public String createGlobalTempTable(String ddl) {
                return null;
            }

            @Override
            public boolean isDuplicate(SQLException x) {
                return false;
            }

            @Override
            public boolean isAlreadyExists(SQLException x) {
                return false;
            }

            @Override
            public boolean isLockTimeout(SQLException x) {
                return false;
            }

            @Override
            public boolean isDeadlock(SQLException x) {
                return false;
            }

            @Override
            public boolean isConnectionError(SQLException x) {
                return false;
            }

            @Override
            public DataAccessException translate(SQLException x) {
                return null;
            }

            @Override
            public boolean isUndefinedName(SQLException x) {
                return false;
            }

            @Override
            public void fillProperties(Properties p, ConnectionDetails cd) {
            }

            @Override
            public String timestampDiff(String left, String right, String alias) {
                return null;
            }

            @Override
            public String createSequence(String name, int cache) {
                return null;
            }

            @Override
            public String reorgTableCommand(String tableName) {
                return null;
            }

            @Override
            public String getDriverClassName() {
                return "INVALID_" + SchemaUtilTest.class.getCanonicalName();
            }

            @Override
            public String getUrl(Properties connectionProperties) {
                return null;
            }

            @Override
            public boolean clobSupportsInline() {
                return false;
            }

        };
        SchemaUtil.loadDriver(translator);
        fail();
    }
    
    @Test
    public void testConfigureLogger() {
        SchemaUtil.configureLogger();
        SchemaUtil.configureLogger(".");
        SchemaUtil.configureLogger("");
        SchemaUtil.configureLogger();
        assert true;
    }
    
    @Test
    public void testRandomKey() {
        String output = SchemaUtil.getRandomKey();
        assertNotNull(output);
        assertFalse(output.isEmpty());
    }
    
    @Test
    public void testLogClasspath() {
        Logger rootLogger = Logger.getLogger(SchemaUtil.class.getCanonicalName());
        rootLogger.setLevel(Level.FINE);
        SchemaUtil.logClasspath();
        assert true;
        rootLogger.setLevel(Level.INFO);
        SchemaUtil.logClasspath();
        assert true;
    }
}