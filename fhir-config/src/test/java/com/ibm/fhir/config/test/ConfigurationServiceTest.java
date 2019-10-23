/*
 * (C) Copyright IBM Corp. 2016,2019
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
                
        // Validate retrieval of an array of strings.
        //Object[] allowableCustomTypes = pg.getArrayProperty("fhirServer/virtualResources/allowableResourceTypes");
       
        Object[] includeResourceTypes = pg.getArrayProperty("fhirServer/notifications/common/includeResourceTypes");
        assertNotNull(includeResourceTypes);
        assertEquals(8, includeResourceTypes.length);
        assertEquals("QuestionnaireResponse", (String) includeResourceTypes[0]);
        assertEquals("CarePlan", (String) includeResourceTypes[1]);
        
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
