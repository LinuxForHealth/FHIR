/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.persistence.ResourcePayload;
import com.ibm.fhir.persistence.util.FHIRPersistenceTestSupport;

/**
 * Tests related to the high-speed export method in FHIRPersistence.
 */
public abstract class AbstractExportTest extends AbstractPersistenceTest {
    Basic resource1;
    Basic resource2;
    Basic resource3;
    Basic resource4;

    @BeforeClass
    public void createResources() throws Exception {
        FHIRRequestContext.get().setTenantId("all");

        Basic resource = TestUtil.getMinimalResource(Basic.class);

        Basic.Builder resource1Builder = resource.toBuilder();
        Basic.Builder resource2Builder = resource.toBuilder();
        Basic.Builder resource3Builder = resource.toBuilder();
        Basic.Builder resource4Builder = resource.toBuilder();

        // number
        resource1Builder.extension(extension("http://example.org/integer", Integer.of(1)));
        resource2Builder.extension(extension("http://example.org/integer", Integer.of(2)));
        resource3Builder.extension(extension("http://example.org/integer", Integer.of(3)));
        resource4Builder.extension(extension("http://example.org/integer", Integer.of(4)));

        // save them in-order so that lastUpdated goes from 1 -> 3 as well
        resource1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource1Builder.meta(tag("pagingTest")).build()).getResource();
        resource2 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource2Builder.meta(tag("pagingTest")).build()).getResource();
        resource3 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource3Builder.meta(tag("pagingTest")).build()).getResource();
        resource4 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource4Builder.meta(tag("pagingTest")).build()).getResource();

        // update resource3 two times so we have 3 different versions
        resource3 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource3.getId(), resource3).getResource();
        resource3 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource3.getId(), resource3).getResource();

        // delete resource4
        delete(getDefaultPersistenceContext(), resource4);
    }

    @AfterClass
    public void removeSavedResourcesAndResetTenant() throws Exception {
        Resource[] resources = {resource1, resource2, resource3};
        if (persistence.isDeleteSupported()) {
            // as this is AfterClass, we need to manually start/end the transaction
            startTrx();
            for (Resource resource : resources) {
                delete(getDefaultPersistenceContext(), resource);
            }
            commitTrx();
        }
        FHIRRequestContext.get().setTenantId("default");
    }

    @Test
    public void testHighSpeedExport() throws Exception {

        // Make sure we start the clock at the right place otherwise our
        // selection span won't cover any data
        Instant fromLastModified = resource1.getMeta().getLastUpdated().getValue().toInstant();
        Instant toLastModified = null;

        final Set<String> logicalIds = new HashSet<>();
        Function<ResourcePayload,Boolean> processor = new Function<ResourcePayload,Boolean>() {

            @Override
            public Boolean apply(ResourcePayload t) {
                logicalIds.add(t.getLogicalId());

                // don't stop now
                return true;
            }

        };

        ResourcePayload result = persistence.fetchResourcePayloads(Basic.class, fromLastModified, toLastModified, processor);
        assertNotNull(result);

        // Check that the export processed all 3 resources we had added to the database
        assertEquals(logicalIds.size(), 3);
        assertTrue(logicalIds.contains(resource1.getId()));
        assertTrue(logicalIds.contains(resource2.getId()));
        assertTrue(logicalIds.contains(resource3.getId()));
        assertFalse(logicalIds.contains(resource4.getId())); // deleted resource should not be present
    }


    /**
     * Convenience function to create a Meta tag
     * @param tag
     * @return
     */
    private Meta tag(String tag) {
        return Meta.builder()
                   .tag(Coding.builder()
                              .code(Code.of(tag))
                              .build())
                   .build();
    }

    /**
     * Convenience function to create an extension
     * @param url
     * @param value
     * @return
     */
    private Extension extension(String url, Element value) {
        return Extension.builder()
                        .url(url)
                        .value(value)
                        .build();
    }
}
