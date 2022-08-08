/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.config.test;

import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.ConfigurationService;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.config.PropertyGroup.PropertyEntry;
import org.linuxforhealth.fhir.config.mock.MockPropertyGroup;

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

        Object[] includeResourceTypes = pg.getArrayProperty("fhirServer/notifications/common/includeResourceTypes");
        assertNotNull(includeResourceTypes);
        assertEquals(8, includeResourceTypes.length);
        assertEquals("QuestionnaireResponse", (String) includeResourceTypes[0]);
        assertEquals("CarePlan", (String) includeResourceTypes[1]);

        // Validate the notifications properties.
        PropertyGroup notificationProps = pg.getPropertyGroup("fhirServer/notifications");
        List<PropertyEntry> props = notificationProps.getProperties();
        assertNotNull(props);
        assertEquals(5, props.size());
        assertNotNull(notificationProps.getPropertyGroup("common"));
        assertNotNull(notificationProps.getPropertyGroup("websocket"));
        assertNotNull(notificationProps.getPropertyGroup("kafka"));

        Double d = notificationProps.getDoubleProperty("count-time-1", 1.0d);
        assertNotNull(d);
        assertEquals(11.0, d);

        d = notificationProps.getDoubleProperty("count-time-3", 1.0d);
        assertNotNull(d);
        assertEquals(1.0, d);

        d = notificationProps.getDoubleProperty("count-time-1");
        assertNotNull(d);
        assertEquals(11.0, d);

        assertNotNull(notificationProps.toString());
        assertFalse(notificationProps.toString().isEmpty());
    }

    @Test
    public void testLoadConfigurationWithEnvironmentVariables() throws Exception {
        Map<String,String> env = new HashMap<>(ConfigurationService.EnvironmentVariables.get());
        env.put("oauth.enabled", "true");
        env.put("oauth.base.url", "http://localhost:9443");
        env.put("oauth.url.path", "/oauth2/endpoint/provider/");
        Mockito.mockStatic(ConfigurationService.EnvironmentVariables.class);
        Mockito.when(ConfigurationService.EnvironmentVariables.get()).thenReturn(env);

        PropertyGroup pg = ConfigurationService.loadConfiguration("fhirConfig.json");
        assertNotNull(pg);

        // Validate retrieval of values based on environment variables.

        PropertyGroup oauthProps = pg.getPropertyGroup("fhirServer/oauth");

        assertEquals(true, oauthProps.getBooleanProperty("enabled").booleanValue());
        assertEquals("http://localhost:9443/oauth2/endpoint/provider/reg", oauthProps.getStringProperty("regUrl"));
    }


    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testLoadConfigurationIllegalArg() throws Exception {
        PropertyGroup pg = ConfigurationService.loadConfiguration("fhirConfig.json");
        assertNotNull(pg);

        Object[] includeResourceTypes = pg.getArrayProperty("fhirServer/notifications/common/includeResourceTypes");
        assertNotNull(includeResourceTypes);
        assertEquals(8, includeResourceTypes.length);
        assertEquals("QuestionnaireResponse", (String) includeResourceTypes[0]);
        assertEquals("CarePlan", (String) includeResourceTypes[1]);

        // Validate the notifications properties.
        PropertyGroup notificationProps = pg.getPropertyGroup("fhirServer/notifications");
        List<PropertyEntry> props = notificationProps.getProperties();
        assertNotNull(props);
        assertEquals(5, props.size());

        notificationProps.getDoubleProperty("count-time-2", 1.0);
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
