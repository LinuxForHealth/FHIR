/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.notification.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.notification.FHIRNotificationEvent;

/**
 * FHIRNotificationUtil Tests
 */
public class FHIRNotificationUtilTest {

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("src/test/resources");
    }

    @BeforeMethod
    public void startMethod(Method method) throws FHIRException {
        // Configure the request context for our search tests
        FHIRRequestContext context = FHIRRequestContext.get();
        if (context == null) {
            context = new FHIRRequestContext();
        }
        FHIRRequestContext.set(context);

        //Facilitate the switching of tenant configurations based on method name
        String tenant = "default";
        String methodName = method.getName();
        if (methodName.contains("_tenant_")) {
            int idx = methodName.indexOf("_tenant_") + "_tenant_".length();
            tenant = methodName.substring(idx);
        }
        context.setTenantId(tenant);

        context.setOriginalRequestUri("https://localhost:9443/fhir-server/api/v4");
    }

    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }

    @Test
    public void testUtil_NotDeleteDontInclude_tenant_default() throws FHIRException {
        Parameters.Builder builder = Parameters.builder();
        builder.id("144f2a21-856d-40d8-8411-2cb482ba0c6f");
        Parameters parms = builder.build();

        FHIRNotificationEvent evt = new FHIRNotificationEvent();
        evt.setLastUpdated("2021-10-10");
        evt.setLocation("Patient/1-2-3-4");
        evt.setOperationType("$operation");
        evt.setResourceId("1-2-3-4");
        evt.setDatasourceId("default");
        evt.setTenantId("default");
        evt.setResource(parms);

        assertEquals(FHIRNotificationUtil.toJsonString(evt, false),
            "{\"lastUpdated\":\"2021-10-10\",\"location\":\"Patient/1-2-3-4\",\"operationType\":\"$operation\",\"resourceId\":\"1-2-3-4\",\"datasourceId\":\"default\",\"tenantId\":\"default\"}");
    }

    @Test
    public void testUtil_NotDeleteAndInclude_tenant_default() throws FHIRException {
        Parameters.Builder builder = Parameters.builder();
        builder.id("144f2a21-856d-40d8-8411-2cb482ba0c6f");
        Parameters parms = builder.build();

        FHIRNotificationEvent evt = new FHIRNotificationEvent();
        evt.setLastUpdated("2021-10-10");
        evt.setLocation("Patient/1-2-3-4");
        evt.setOperationType("$operation");
        evt.setResourceId("1-2-3-4");
        evt.setDatasourceId("default");
        evt.setTenantId("default");
        evt.setResource(parms);

        assertEquals(FHIRNotificationUtil.toJsonString(evt, true),
            "{\"lastUpdated\":\"2021-10-10\",\"location\":\"Patient/1-2-3-4\",\"operationType\":\"$operation\",\"resourceId\":\"1-2-3-4\",\"datasourceId\":\"default\",\"tenantId\":\"default\",\"resource\":{\"resourceType\":\"Parameters\",\"id\":\"144f2a21-856d-40d8-8411-2cb482ba0c6f\"}}");
    }

    @Test
    public void testUtil_NotDeleteAndInclude_tenant_omit() throws FHIRException {
        Parameters.Builder builder = Parameters.builder();
        builder.id("144f2a21-856d-40d8-8411-2cb482ba0c6f");
        Parameters parms = builder.build();

        FHIRNotificationEvent evt = new FHIRNotificationEvent();
        evt.setLastUpdated("2021-10-10");
        evt.setLocation("Patient/1-2-3-4");
        evt.setOperationType("$operation");
        evt.setResourceId("1-2-3-4");
        evt.setDatasourceId("default");
        evt.setTenantId("default");
        evt.setResource(parms);

        assertEquals(FHIRNotificationUtil.toJsonString(evt, true),
            "{\"lastUpdated\":\"2021-10-10\",\"location\":\"Patient/1-2-3-4\",\"operationType\":\"$operation\",\"resourceId\":\"1-2-3-4\",\"datasourceId\":\"default\",\"tenantId\":\"default\"}");
    }

    @Test
    public void testUtil_NotDeleteAndInclude_tenant_subset() throws FHIRException {
        Parameters.Builder builder = Parameters.builder();
        builder.id("144f2a21-856d-40d8-8411-2cb482ba0c6f");
        Parameters parms = builder.build();

        FHIRNotificationEvent evt = new FHIRNotificationEvent();
        evt.setLastUpdated("2021-10-10");
        evt.setLocation("Patient/1-2-3-4");
        evt.setOperationType("$operation");
        evt.setResourceId("1-2-3-4");
        evt.setDatasourceId("default");
        evt.setTenantId("default");
        evt.setResource(parms);

        assertEquals(FHIRNotificationUtil.toJsonString(evt, true),
            "{\"lastUpdated\":\"2021-10-10\",\"location\":\"Patient/1-2-3-4\",\"operationType\":\"$operation\",\"resourceId\":\"1-2-3-4\",\"datasourceId\":\"default\",\"tenantId\":\"default\",\"resource\":{\"resourceType\":\"Parameters\",\"id\":\"144f2a21-856d-40d8-8411-2cb482ba0c6f\",\"meta\":{\"tag\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-ObservationValue\",\"code\":\"SUBSETTED\",\"display\":\"subsetted\"}]}}}");
    }

    @Test
    public void testUtil_NotDeleteAndInclude_tenant_empty() throws FHIRException {
        Parameters.Builder builder = Parameters.builder();
        builder.id("144f2a21-856d-40d8-8411-2cb482ba0c6f");
        Parameters parms = builder.build();

        FHIRNotificationEvent evt = new FHIRNotificationEvent();
        evt.setLastUpdated("2021-10-10");
        evt.setLocation("Patient/1-2-3-4");
        evt.setOperationType("$operation");
        evt.setResourceId("1-2-3-4");
        evt.setDatasourceId("default");
        evt.setTenantId("default");
        evt.setResource(parms);

        assertEquals(FHIRNotificationUtil.toJsonString(evt, true),
            "{\"lastUpdated\":\"2021-10-10\",\"location\":\"Patient/1-2-3-4\",\"operationType\":\"$operation\",\"resourceId\":\"1-2-3-4\",\"datasourceId\":\"default\",\"tenantId\":\"default\",\"resource\":{\"resourceType\":\"Parameters\",\"id\":\"144f2a21-856d-40d8-8411-2cb482ba0c6f\",\"meta\":{\"tag\":[{\"system\":\"http://terminology.hl7.org/CodeSystem/v3-ObservationValue\",\"code\":\"SUBSETTED\",\"display\":\"subsetted\"}]}}}");
    }

    @Test
    public void testUtil_NotDeleteNotInclude_tenant_default() throws FHIRException {
        Parameters.Builder builder = Parameters.builder();
        builder.id("144f2a21-856d-40d8-8411-2cb482ba0c6f");
        Parameters parms = builder.build();

        FHIRNotificationEvent evt = new FHIRNotificationEvent();
        evt.setLastUpdated("2021-10-10");
        evt.setLocation("Patient/1-2-3-4");
        evt.setOperationType("$operation");
        evt.setResourceId("1-2-3-4");
        evt.setDatasourceId("default");
        evt.setTenantId("default");
        evt.setResource(parms);

        assertEquals(FHIRNotificationUtil.toJsonString(evt, false),
            "{\"lastUpdated\":\"2021-10-10\",\"location\":\"Patient/1-2-3-4\",\"operationType\":\"$operation\",\"resourceId\":\"1-2-3-4\",\"datasourceId\":\"default\",\"tenantId\":\"default\"}");
    }

    @Test
    public void testUtil_NotDeleteIncludeWithoutResource_tenant_default() throws FHIRException {
        FHIRNotificationEvent evt = new FHIRNotificationEvent();
        evt.setLastUpdated("2021-10-10");
        evt.setLocation("Patient/1-2-3-4");
        evt.setOperationType("$operation");
        evt.setResourceId("1-2-3-4");
        evt.setDatasourceId("default");
        evt.setTenantId("default");

        assertEquals(FHIRNotificationUtil.toJsonString(evt, true),
            "{\"lastUpdated\":\"2021-10-10\",\"location\":\"Patient/1-2-3-4\",\"operationType\":\"$operation\",\"resourceId\":\"1-2-3-4\",\"datasourceId\":\"default\",\"tenantId\":\"default\"}");
    }

    @Test
    public void testUtil_Delete_tenant_default() throws FHIRException {
        Parameters.Builder builder = Parameters.builder();
        builder.id("144f2a21-856d-40d8-8411-2cb482ba0c6f");
        Parameters parms = builder.build();

        FHIRNotificationEvent evt = new FHIRNotificationEvent();
        evt.setLastUpdated("2021-10-10");
        evt.setLocation("Patient/1-2-3-4");
        evt.setOperationType("delete");
        evt.setResourceId("1-2-3-4");
        evt.setDatasourceId("default");
        evt.setTenantId("default");
        evt.setResource(parms);

        assertEquals(FHIRNotificationUtil.toJsonString(evt, true),
            "{\"lastUpdated\":\"2021-10-10\",\"location\":\"Patient/1-2-3-4\",\"operationType\":\"delete\",\"resourceId\":\"1-2-3-4\",\"datasourceId\":\"default\",\"tenantId\":\"default\"}");
    }

    @Test
    public void testUtil_RoundTrip_tenant_default() throws FHIRException {
        String jsonString = "{\"lastUpdated\":\"2021-10-10\",\"location\":\"Patient/1-2-3-4\",\"operationType\":\"$operation\",\"resourceId\":\"1-2-3-4\",\"datasourceId\":\"default\",\"tenantId\":\"default\"}";
        FHIRNotificationEvent evt = FHIRNotificationUtil.toNotificationEvent(jsonString);
        assertNotNull(evt);
        assertEquals(evt.getLastUpdated(),"2021-10-10");
        assertEquals(evt.getLocation(),"Patient/1-2-3-4");
        assertEquals(evt.getOperationType(),"$operation");
        assertEquals(evt.getResourceId(),"1-2-3-4");
        assertEquals(evt.getDatasourceId(),"default");
        assertEquals(evt.getTenantId(),"default");
        assertNull(evt.getResource());
    }

    @Test
    public void testUtil_BadJson_tenant_default() throws FHIRException {
        String jsonString = "{\"";
        FHIRNotificationEvent evt = FHIRNotificationUtil.toNotificationEvent(jsonString);
        assertNull(evt);
    }
}