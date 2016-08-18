/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.config.ConfigurationService;
import com.ibm.watsonhealth.fhir.config.PropertyGroup;
import com.ibm.watsonhealth.fhir.config.PropertyGroup.PropertyEntry;

public class ConfigurationServiceTest {

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
        assertEquals("password", ksPassword);
        String keyPassword = encryptionProps.getStringProperty("keyPassword");
        assertNotNull(keyPassword);
        assertEquals("password", keyPassword);
        
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
}
