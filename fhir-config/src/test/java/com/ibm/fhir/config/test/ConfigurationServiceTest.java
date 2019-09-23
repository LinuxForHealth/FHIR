/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.ConfigurationService;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.config.mock.MockPropertyGroup;

public class ConfigurationServiceTest {
    
    @AfterMethod
    public void cleanUp() {
        System.setProperty(ConfigurationService.PROPERTY_GROUP_CLASSNAME, "");
    }

    @Test
    public void testLoadConfiguration() throws Exception {
        PropertyGroup pg = ConfigurationService.loadConfiguration("fhirConfig.json");
        assertNotNull(pg);
        
        // Validate the "encryption" property group.
        PropertyGroup encryptionProps = pg.getPropertyGroup("fhirServer/encryption");
        assertNotNull(encryptionProps);
        Boolean enabled = encryptionProps.getBooleanProperty("enabled");
        assertNotNull(enabled);
        assertEquals(Boolean.FALSE, enabled);
        String ksLoc = encryptionProps.getStringProperty("keystoreLocation");
        assertNotNull(ksLoc);
        assertEquals("resources/security/fhirkeys.jceks", ksLoc);
        String ksPassword = encryptionProps.getStringProperty("keystorePassword");
        assertNotNull(ksPassword);
        assertEquals("change-password", ksPassword);
        String keyPassword = encryptionProps.getStringProperty("keyPassword");
        assertNotNull(keyPassword);
        assertEquals("change-password", keyPassword);
        
        // Validate retrieval of an array of strings.
        Object[] allowableCustomTypes = pg.getArrayProperty("fhirServer/virtualResources/allowableResourceTypes");
        assertNotNull(allowableCustomTypes);
        assertEquals(2, allowableCustomTypes.length);
        assertEquals("WeatherDetail", (String) allowableCustomTypes[0]);
        assertEquals("XYZStudy", (String) allowableCustomTypes[1]);
        
        // Validate the notifications properties.
        PropertyGroup notificationProps = pg.getPropertyGroup("fhirServer/notifications");
        List<PropertyEntry> props = notificationProps.getProperties();
        assertNotNull(props);
        assertEquals(3, props.size());
        assertNotNull(notificationProps.getPropertyGroup("common"));
        assertNotNull(notificationProps.getPropertyGroup("websocket"));
        assertNotNull(notificationProps.getPropertyGroup("kafka"));
    }
    
    @Test
    public void testMockPropertyGroup1() throws Exception {
        System.setProperty(ConfigurationService.PROPERTY_GROUP_CLASSNAME, MockPropertyGroup.class.getName());
        PropertyGroup pg = ConfigurationService.loadConfiguration("fhirConfig.json");
        assertNotNull(pg);
        assertTrue(pg instanceof MockPropertyGroup);
    }
    
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMockPropertyGroup2() throws Exception {
        System.setProperty(ConfigurationService.PROPERTY_GROUP_CLASSNAME, "BAD_CLASS_NAME");
        ConfigurationService.loadConfiguration("fhirConfig.json");
    }
    
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMockPropertyGroup3() throws Exception {
        System.setProperty(ConfigurationService.PROPERTY_GROUP_CLASSNAME, TestMockPropertyGroup.class.getName());
        ConfigurationService.loadConfiguration("fhirConfig.json");
    }
}
