/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import static org.testng.Assert.assertEquals;

import java.util.Properties;

import org.testng.annotations.Test;

/**
 * JdbcProperty Adapter test. 
 */
public class JdbcPropertyAdapterTest {
    private static final String TEST_DATABASE = "test.database";
    private static final String TEST_HOST = "test.host";
    private static final String TEST_PASSWORD = "test.password";
    private static final int TEST_PORT = 1234;
    private static final String TEST_USER = "test.user";

    @Test
    public void testGetters() {
        Properties properties = new Properties();
        
        properties.put(JdbcPropertyAdapter.DATABASE_KEY, TEST_DATABASE);
        properties.put(JdbcPropertyAdapter.HOST_KEY, TEST_HOST);
        properties.put(JdbcPropertyAdapter.PASSWORD_KEY, TEST_PASSWORD);
        properties.put(JdbcPropertyAdapter.PORT_KEY, Integer.toString(TEST_PORT));
        properties.put(JdbcPropertyAdapter.USER_KEY, TEST_USER);
        
        JdbcPropertyAdapter pad = new JdbcPropertyAdapter(properties);
        
        assertEquals(pad.getDatabase(), TEST_DATABASE);
        assertEquals(pad.getHost(), TEST_HOST);
        assertEquals(pad.getPassword(), TEST_PASSWORD);
        assertEquals(pad.getPort(), TEST_PORT);
        assertEquals(pad.getUser(), TEST_USER);
    }

    @Test
    public void testSetters() {
        Properties properties = new Properties();
        
        
        JdbcPropertyAdapter pad = new JdbcPropertyAdapter(properties);
                
        pad.setDatabase(TEST_DATABASE);
        pad.setHost(TEST_HOST);
        pad.setPassword(TEST_PASSWORD);
        pad.setPort(TEST_PORT);
        pad.setUser(TEST_USER);
        
        assertEquals(pad.getDatabase(), TEST_DATABASE);
        assertEquals(pad.getHost(), TEST_HOST);
        assertEquals(pad.getPassword(), TEST_PASSWORD);
        assertEquals(pad.getPort(), TEST_PORT);
        assertEquals(pad.getUser(), TEST_USER);
    }

    @Test
    public void testProperties() {
        Properties properties = new Properties();
        
        JdbcPropertyAdapter pad = new JdbcPropertyAdapter(properties);
                
        pad.setDatabase(TEST_DATABASE);
        pad.setHost(TEST_HOST);
        pad.setPassword(TEST_PASSWORD);
        pad.setPort(TEST_PORT);
        pad.setUser(TEST_USER);
        
        assertEquals(pad.getDatabase(), properties.getProperty(JdbcPropertyAdapter.DATABASE_KEY));
        assertEquals(pad.getHost(), properties.getProperty(JdbcPropertyAdapter.HOST_KEY));
        assertEquals(pad.getPassword(), properties.getProperty(JdbcPropertyAdapter.PASSWORD_KEY));
        assertEquals(Integer.toString(pad.getPort()), properties.getProperty(JdbcPropertyAdapter.PORT_KEY));
        assertEquals(pad.getUser(), properties.getProperty(JdbcPropertyAdapter.USER_KEY));
    }

}
