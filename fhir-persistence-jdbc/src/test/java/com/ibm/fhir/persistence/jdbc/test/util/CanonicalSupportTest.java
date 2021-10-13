/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceProfileRec;
import com.ibm.fhir.persistence.jdbc.util.CanonicalSupport;

/**
 * Unit test for {@link CanonicalSupport}
 */
public class CanonicalSupportTest {

    /**
     * Test a canonical uri without version or fragment
     * @throws FHIRPersistenceException
     */
    @Test
    public void simpleUriTest() throws FHIRPersistenceException {
        final String paramValue = "https://example.org/foo/bar";
        final String parameterName = "seven";
        final int parameterNameId = 7;
        final String resourceType = "Patient";
        final int resourceTypeId = 17;
        final long logicalResourceId = 42;
        final boolean systemLevel = true;
        ResourceProfileRec rec = CanonicalSupport.makeResourceProfileRec(parameterName, resourceType, resourceTypeId, logicalResourceId, paramValue, systemLevel);

        assertNotNull(rec);
        rec.setParameterNameId(parameterNameId);
        assertEquals(rec.getParameterName(), parameterName);
        assertEquals(rec.getLogicalResourceId(), logicalResourceId);
        assertEquals(rec.getCanonicalValue(), paramValue);
        assertEquals(rec.getParameterNameId(), parameterNameId);
        assertEquals(rec.getResourceType(), resourceType);
        assertEquals(rec.getResourceTypeId(), resourceTypeId);
        assertNull(rec.getVersion());
        assertNull(rec.getFragment());
    }

    /**
     * Test a canonical uri with version but no fragment
     * @throws FHIRPersistenceException
     */
    @Test
    public void uriVersionTest() throws FHIRPersistenceException {
        final String uri = "https://example.org/foo/bar";
        final String version = "1.0";
        final String paramValue = uri + "|" + version;
        final String parameterName = "7";
        final String resourceType = "Patient";
        final int resourceTypeId = 17;
        final long logicalResourceId = 42;
        final boolean systemLevel = true;
        ResourceProfileRec rec = CanonicalSupport.makeResourceProfileRec(parameterName, resourceType, resourceTypeId, logicalResourceId, paramValue, systemLevel);

        assertNotNull(rec);
        assertEquals(rec.getLogicalResourceId(), logicalResourceId);
        assertEquals(rec.getCanonicalValue(), uri);
        assertEquals(rec.getParameterName(), parameterName);
        assertEquals(rec.getResourceType(), resourceType);
        assertEquals(rec.getResourceTypeId(), resourceTypeId);
        assertEquals(rec.getVersion(), version);
        assertNull(rec.getFragment());
    }

    /**
     * Test a canonical uri with version and fragment
     * @throws FHIRPersistenceException
     */
    @Test
    public void uriVersionFragmentTest() throws FHIRPersistenceException {
        final String uri = "https://example.org/foo/bar";
        final String version = "1.0";
        final String fragment = "F1";
        final String paramValue = uri + "|" + version + "#" + fragment;
        final String parameterName = "7";
        final String resourceType = "Patient";
        final int resourceTypeId = 17;
        final long logicalResourceId = 42;
        final boolean systemLevel = true;
        ResourceProfileRec rec = CanonicalSupport.makeResourceProfileRec(parameterName, resourceType, resourceTypeId, logicalResourceId, paramValue, systemLevel);

        assertNotNull(rec);
        assertEquals(rec.getLogicalResourceId(), logicalResourceId);
        assertEquals(rec.getCanonicalValue(), uri);
        assertEquals(rec.getParameterName(), parameterName);
        assertEquals(rec.getResourceType(), resourceType);
        assertEquals(rec.getResourceTypeId(), resourceTypeId);
        assertEquals(rec.getVersion(), version);
        assertEquals(rec.getFragment(), fragment);
    }

    /**
     * Test a canonical uri with fragment but no version
     * @throws FHIRPersistenceException
     */
    @Test
    public void uriFragmentTest() throws FHIRPersistenceException {
        final String uri = "https://example.org/foo/bar";
        final String fragment = "F1";
        final String paramValue = uri + "#" + fragment;
        final String parameterName = "7";
        final String resourceType = "Patient";
        final int resourceTypeId = 17;
        final long logicalResourceId = 42;
        final boolean systemLevel = true;
        ResourceProfileRec rec = CanonicalSupport.makeResourceProfileRec(parameterName, resourceType, resourceTypeId, logicalResourceId, paramValue, systemLevel);

        assertNotNull(rec);
        assertEquals(rec.getLogicalResourceId(), logicalResourceId);
        assertEquals(rec.getCanonicalValue(), uri);
        assertEquals(rec.getParameterName(), parameterName);
        assertEquals(rec.getResourceType(), resourceType);
        assertEquals(rec.getResourceTypeId(), resourceTypeId);
        assertNull(rec.getVersion());
        assertEquals(rec.getFragment(), fragment);
    }

    /**
     * Test a canonical uri with empty fragment
     * @throws FHIRPersistenceException
     */
   @Test
    public void uriEmptyFragmentTest() throws FHIRPersistenceException {
        final String uri = "https://example.org/foo/bar";
        final String fragment = "";
        final String paramValue = uri + "#" + fragment;
        final String parameterName = "7";
        final String resourceType = "Patient";
        final int resourceTypeId = 17;
        final long logicalResourceId = 42;
        final boolean systemLevel = true;
        ResourceProfileRec rec = CanonicalSupport.makeResourceProfileRec(parameterName, resourceType, resourceTypeId, logicalResourceId, paramValue, systemLevel);

        assertNotNull(rec);
        assertEquals(rec.getLogicalResourceId(), logicalResourceId);
        assertEquals(rec.getCanonicalValue(), uri);
        assertEquals(rec.getParameterName(), parameterName);
        assertEquals(rec.getResourceType(), resourceType);
        assertEquals(rec.getResourceTypeId(), resourceTypeId);
        assertNull(rec.getVersion());
        assertNull(rec.getFragment());
    }

   /**
    * Test a canonical uri with empty version
    * @throws FHIRPersistenceException
    */
    @Test
    public void uriEmptyVersionTest() throws FHIRPersistenceException {
        final String uri = "https://example.org/foo/bar";
        final String version = "";
        final String paramValue = uri + "|" + version;
        final String parameterName = "7";
        final String resourceType = "Patient";
        final int resourceTypeId = 17;
        final long logicalResourceId = 42;
        final boolean systemLevel = true;
        ResourceProfileRec rec = CanonicalSupport.makeResourceProfileRec(parameterName, resourceType, resourceTypeId, logicalResourceId, paramValue, systemLevel);

        assertNotNull(rec);
        assertEquals(rec.getLogicalResourceId(), logicalResourceId);
        assertEquals(rec.getCanonicalValue(), uri);
        assertEquals(rec.getParameterName(), parameterName);
        assertEquals(rec.getResourceType(), resourceType);
        assertEquals(rec.getResourceTypeId(), resourceTypeId);
        assertNull(rec.getVersion());
        assertNull(rec.getFragment());
    }

    /**
     * Test a canonical uri with empty version and fragment
     * @throws FHIRPersistenceException
     */
    @Test
    public void uriEmptyVersionEmptyFragmentTest() throws FHIRPersistenceException {
        final String uri = "https://example.org/foo/bar";
        final String version = "";
        final String fragment = "";
        final String paramValue = uri + "|" + version + "#" + fragment;
        final String parameterName = "7";
        final String resourceType = "Patient";
        final int resourceTypeId = 17;
        final long logicalResourceId = 42;
        final boolean systemLevel = true;
        ResourceProfileRec rec = CanonicalSupport.makeResourceProfileRec(parameterName, resourceType, resourceTypeId, logicalResourceId, paramValue, systemLevel);

        assertNotNull(rec);
        assertEquals(rec.getLogicalResourceId(), logicalResourceId);
        assertEquals(rec.getCanonicalValue(), uri);
        assertEquals(rec.getParameterName(), parameterName);
        assertEquals(rec.getResourceType(), resourceType);
        assertEquals(rec.getResourceTypeId(), resourceTypeId);
        assertNull(rec.getVersion());
        assertNull(rec.getFragment());
    }

    /**
     * Test a canonical uri with invalid fragment/version order
     * @throws FHIRPersistenceException
     */
    @Test(expectedExceptions = FHIRPersistenceException.class)
    public void uriInvalidFragmentVersionTest() throws FHIRPersistenceException {
        final String uri = "https://example.org/foo/bar";
        final String version = "1.0";
        final String fragment = "F1";
        final String paramValue = uri + "#" + fragment + "|" + version;
        final String parameterName = "7";
        final String resourceType = "Patient";
        final int resourceTypeId = 17;
        final long logicalResourceId = 42;
        final boolean systemLevel = true;
        CanonicalSupport.makeResourceProfileRec(parameterName, resourceType, resourceTypeId, logicalResourceId, paramValue, systemLevel);
    }
}