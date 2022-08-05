/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cassandra.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.ConfigurationService;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.persistence.cassandra.CassandraPropertyGroupAdapter;
import org.linuxforhealth.fhir.persistence.cassandra.ContactPoint;

/**
 * Unit test for Cassandra property configuration
 */
public class ConfigTest {
    private final static String TEST_CONFIG_FILE = "fhir-server-config.json";

    @Test
    public void test1() throws Exception {
        PropertyGroup fhirServerConfig = ConfigurationService.loadConfiguration(TEST_CONFIG_FILE);
        assertNotNull(fhirServerConfig);
        
        PropertyGroup pg = fhirServerConfig.getPropertyGroup(FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/default/connectionProperties");
        assertNotNull(pg);
        CassandraPropertyGroupAdapter adapter = new CassandraPropertyGroupAdapter(pg);
        assertEquals(adapter.getLocalDatacenter(), "localDatacenter1");
        List<ContactPoint> cpList = adapter.getContactPoints();
        assertNotNull(cpList);
        assertEquals(cpList.size(), 2);
        assertEquals(cpList.get(0).getHost(), "host1");
        assertEquals(cpList.get(0).getPort(), 1);
        assertEquals(cpList.get(1).getHost(), "host2");
        assertEquals(cpList.get(1).getPort(), 2);        
    }
}