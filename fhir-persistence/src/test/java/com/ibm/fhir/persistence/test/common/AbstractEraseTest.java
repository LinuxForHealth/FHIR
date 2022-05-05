/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.persistence.util.FHIRPersistenceTestSupport;

/**
 * Tests related to the erase method in FHIRPersistence.
 */
public abstract class AbstractEraseTest extends AbstractPersistenceTest {

    private List<Resource> resources = new ArrayList<>();
    Basic.Builder allTypesBuilder = null;

    /**
     * Convenience function to create a Meta tag
     *
     * @param tag
     * @return
     */
    private Meta tag(String tag) {
        return Meta.builder().tag(Coding.builder().code(Code.of(tag)).build()).build();
    }

    /**
     * Convenience function to create an extension
     *
     * @param url
     * @param value
     * @return
     */
    private Extension extension(String url, Element value) {
        return Extension.builder().url(url).value(value).build();
    }

    @BeforeClass
    public void setupResourceBuilder() throws Exception {
        // We need the all so we can set the Erase on all tables
        FHIRRequestContext.get().setTenantId("all");

        Basic resource = TestUtil.getMinimalResource(Basic.class);
        allTypesBuilder = resource.toBuilder();

        // Number
        allTypesBuilder.extension(extension("http://example.org/integer", Integer.of(1)));
        // Date/DateTime
        allTypesBuilder.extension(extension("http://example.org/dateTime", DateTime.now()));
        // String
        allTypesBuilder.extension(extension("http://example.org/string", string("abcde")));
        // Token
        allTypesBuilder.extension(extension("http://example.org/code", Code.of("sample-code")));
        // Reference
        allTypesBuilder.extension(extension("http://example.org/Reference", Reference.builder().reference(string("Patient/Ref1")).build()));
        // Quantity
        allTypesBuilder.extension(extension("http://example.org/Quantity", Quantity.builder().value(Decimal.of(10.0)).build()));
        // URI
        allTypesBuilder.extension(extension("http://example.org/uri", Uri.of("https://test.com")));

        // Skipped Location/Composite intentionally.
    }

    @AfterClass
    public void removeSavedResourcesAndResetTenant() throws Exception {
        // *** NOTE NOTE NOTE ***
        // Although we are soft-deleting the resources, this generates more records
        // in the change record log, so it's important that the tests in this class
        // are written to take that into account
        if (persistence.isDeleteSupported()) {
            // as this is AfterClass, we need to manually start/end the transaction
            startTrx();
            for (Resource resource : resources) {
                try {
                    FHIRPersistenceTestSupport.delete(persistence, getDefaultPersistenceContext(), resource);
                } catch (Exception e) {
                    // Swallow any exception.
                }
            }
            commitTrx();
        }
        FHIRRequestContext.get().setTenantId("default");
    }

    @Test
    public void testEraseResourceWithHistory() throws Exception {
        // Resource 1 - C - U - U (with multiple values)
        // Erases all resources
        Basic resource1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), allTypesBuilder.meta(tag("eraseTest")).build()).getResource();
        resources.add(resource1);
        resource1 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource1.getId(), resource1).getResource();
        resource1 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource1.getId(), resource1).getResource();

        EraseDTO dto = new EraseDTO();
        dto.setLogicalId(resource1.getId());
        dto.setResourceType("Basic");
        ResourceEraseRecord eraseRecord = persistence.erase(null, dto);
        assertNotNull(eraseRecord);
        assertEquals((int) eraseRecord.getTotal(), 3);
        assertEquals(eraseRecord.getStatus(), ResourceEraseRecord.Status.DONE);
        SingleResourceResult<? extends Basic> result = persistence.read(getDefaultPersistenceContext(), resource1.getClass(), resource1.getId());
        assertNull(result.getResource());
    }

    @Test
    public void testEraseSingleResource() throws Exception {
        Basic resource1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), allTypesBuilder.meta(tag("eraseTest")).build()).getResource();
        resources.add(resource1);
        EraseDTO dto = new EraseDTO();
        dto.setLogicalId(resource1.getId());
        dto.setResourceType("Basic");
        ResourceEraseRecord eraseRecord = persistence.erase(null, dto);
        assertNotNull(eraseRecord);
        assertEquals((int) eraseRecord.getTotal(), 1);
        assertEquals(eraseRecord.getStatus(), ResourceEraseRecord.Status.DONE);
        SingleResourceResult<? extends Basic> result = persistence.read(getDefaultPersistenceContext(), resource1.getClass(), resource1.getId());
        assertNull(result.getResource());
    }

    @Test
    public void testEraseLastIsDeleted() throws Exception {
        // Resource where the last is deleted (C - D)
        Basic resource1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), allTypesBuilder.meta(tag("eraseTest")).build()).getResource();
        resources.add(resource1);
        delete(getDefaultPersistenceContext(), resource1);
        EraseDTO dto = new EraseDTO();
        dto.setLogicalId(resource1.getId());
        dto.setResourceType("Basic");
        ResourceEraseRecord eraseRecord = persistence.erase(null, dto);
        assertNotNull(eraseRecord);
        assertEquals((int) eraseRecord.getTotal(), 2);
        assertEquals(eraseRecord.getStatus(), ResourceEraseRecord.Status.DONE);
        SingleResourceResult<? extends Basic> result = persistence.read(getDefaultPersistenceContext(), resource1.getClass(), resource1.getId());
        assertNull(result.getResource());
    }

    @Test
    public void testEraseNotExists() throws Exception {
        EraseDTO dto = new EraseDTO();
        dto.setLogicalId("---NOTEXISTS");
        dto.setResourceType("Basic");
        ResourceEraseRecord eraseRecord = persistence.erase(null, dto);
        assertNotNull(eraseRecord);
        assertEquals(eraseRecord.getStatus(), ResourceEraseRecord.Status.NOT_FOUND);
    }

    @Test
    public void testEraseSpecificVersionGreater() throws Exception {
        // Resource_1 C-U-U - Latest is 3
        // This test sets 4, since it exceeds the value.
        Basic resource1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), allTypesBuilder.meta(tag("eraseTest")).build()).getResource();
        resources.add(resource1);
        resource1 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource1.getId(), resource1).getResource();
        resource1 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource1.getId(), resource1).getResource();

        EraseDTO dto = new EraseDTO();
        dto.setLogicalId(resource1.getId());
        dto.setResourceType("Basic");
        dto.setVersion(4);
        ResourceEraseRecord eraseRecord = persistence.erase(null, dto);
        assertNotNull(eraseRecord);
        assertEquals((int) eraseRecord.getTotal(), -1);
        assertEquals(eraseRecord.getStatus(), ResourceEraseRecord.Status.NOT_SUPPORTED_GREATER);
        SingleResourceResult<? extends Basic> result = persistence.read(getDefaultPersistenceContext(), resource1.getClass(), resource1.getId());
        assertNotNull(result.getResource());
    }

    @Test
    public void testEraseSpecificVersion() throws Exception {
        // The payload is erased (so the last check is for null on the resource)
        Basic resource1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), allTypesBuilder.meta(tag("eraseTest")).build()).getResource();
        resources.add(resource1);
        resource1 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource1.getId(), resource1).getResource();
        resource1 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource1.getId(), resource1).getResource();

        EraseDTO dto = new EraseDTO();
        dto.setLogicalId(resource1.getId());
        dto.setResourceType("Basic");
        dto.setVersion(2);
        ResourceEraseRecord eraseRecord = persistence.erase(null, dto);
        assertNotNull(eraseRecord);
        assertEquals((int) eraseRecord.getTotal(), 1);
        assertEquals(eraseRecord.getStatus(), ResourceEraseRecord.Status.VERSION);
        SingleResourceResult<? extends Basic> result = persistence.read(getDefaultPersistenceContext(), resource1.getClass(), resource1.getId());
        assertNotNull(result.getResource());
        result = persistence.vread(getDefaultPersistenceContext(), resource1.getClass(), resource1.getId(), "2");
        assertNull(result.getResource());
    }

    @Test
    public void testEraseLatestSpecificVersion() throws Exception {
        // Resource_1 C-U-U - Latest is 3
        // This test sets 3, since it is the value.
        Basic resource1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), allTypesBuilder.meta(tag("eraseTest")).build()).getResource();
        resources.add(resource1);
        resource1 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource1.getId(), resource1).getResource();
        resource1 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource1.getId(), resource1).getResource();

        EraseDTO dto = new EraseDTO();
        dto.setLogicalId(resource1.getId());
        dto.setResourceType("Basic");
        dto.setVersion(3);
        ResourceEraseRecord eraseRecord = persistence.erase(null, dto);
        assertNotNull(eraseRecord);
        assertEquals((int) eraseRecord.getTotal(), -1);
        assertEquals(eraseRecord.getStatus(), ResourceEraseRecord.Status.NOT_SUPPORTED_LATEST);
        SingleResourceResult<? extends Basic> result = persistence.read(getDefaultPersistenceContext(), resource1.getClass(), resource1.getId());
        assertNotNull(result.getResource());
    }

    @Test
    public void testEraseSingleResourceWithVersion1() throws Exception {
        Basic resource1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), allTypesBuilder.meta(tag("eraseTest")).build()).getResource();
        resources.add(resource1);
        EraseDTO dto = new EraseDTO();
        dto.setLogicalId(resource1.getId());
        dto.setResourceType("Basic");
        dto.setVersion(1);
        ResourceEraseRecord eraseRecord = persistence.erase(null, dto);
        assertNotNull(eraseRecord);
        assertEquals((int) eraseRecord.getTotal(), 1);
        assertEquals(eraseRecord.getStatus(), ResourceEraseRecord.Status.DONE);
        SingleResourceResult<? extends Basic> result = persistence.read(getDefaultPersistenceContext(), resource1.getClass(), resource1.getId());
        assertNull(result.getResource());
    }
}