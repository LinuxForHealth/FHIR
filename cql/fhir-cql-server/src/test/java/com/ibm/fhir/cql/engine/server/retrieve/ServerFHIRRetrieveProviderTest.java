/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.cql.engine.server.retrieve;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.cql.engine.searchparam.SearchParameterResolver;
import com.ibm.fhir.cql.engine.server.terminology.ServerFHIRTerminologyProvider;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Link;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.search.util.SearchHelper;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

public class ServerFHIRRetrieveProviderTest {

    private ServerFHIRTerminologyProvider termProvider;
    private ServerFHIRRetrieveProvider provider;
    private FHIRResourceHelpers helpers;
    private SearchHelper searchHelper;

    @BeforeClass
    public void initializeSearchUtil() {
        searchHelper = new SearchHelper();
    }

    @BeforeMethod
    public void setup() {
        helpers = mock(FHIRResourceHelpers.class);
        termProvider = mock(ServerFHIRTerminologyProvider.class);

        SearchParameterResolver resolver = new SearchParameterResolver(searchHelper);

        provider = new ServerFHIRRetrieveProvider(helpers, resolver);
        provider.setTerminologyProvider(termProvider);
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testQueryById() throws Exception {
        Patient p1 = Patient.builder().id("p1").build();

        SingleResourceResult result = mock(SingleResourceResult.class);
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(p1);

        when(helpers.doRead(eq("Patient"), eq("123"), eq(true), isNull())).thenReturn(result);

        Iterable<Object> resources = provider.retrieve("Patient", "id", "123", "Patient", null, null, null, null, null, null, null, null);
        assertNotNull(resources);

        Map<String, Resource> results = new HashMap<>();
        resources.forEach(o -> results.put(((Resource) o).getId(), (Resource) o));
        assertEquals(results.size(),1);

        Set<String> expected = new HashSet<>();
        expected.add(p1.getId());
        assertEquals(results.keySet(), expected);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testQueryByIdNotFound() throws Exception {
        when(helpers.doRead(eq("Patient"), eq("123"), eq(true), isNull())).thenThrow(FHIRPersistenceResourceNotFoundException.class);

        provider.retrieve("Patient", "id", "123", "Patient", null, null, null, null, null, null, null, null);
    }

    @Test
    public void testMultiplePagesAllReadSuccessfully() throws Exception {
        Condition c1 = Condition.builder().id("c1").subject(Reference.builder().reference(com.ibm.fhir.model.type.String.of("Patient/123")).build()).build();

        Bundle page1 =
                Bundle.builder().type(BundleType.SEARCHSET).total(UnsignedInt.of(1)).entry(Bundle.Entry.builder().resource(c1).build()).link(Link.builder().relation(com.ibm.fhir.model.type.String.of("next")).url(Uri.of(getBaseUrl()
                        + "/Condition?subject:Patient=123&_page=2")).build()).build();

        Condition c2 = Condition.builder().id("c2").subject(Reference.builder().reference(com.ibm.fhir.model.type.String.of("Patient/123")).build()).build();

        Bundle page2 = Bundle.builder().type(BundleType.SEARCHSET).total(UnsignedInt.of(1)).entry(Bundle.Entry.builder().resource(c2).build()).build();

        when(helpers.doSearch(eq("Condition"), isNull(), isNull(), not(hasEntry("_page")), anyString())).thenReturn(page1);
        when(helpers.doSearch(eq("Condition"), isNull(), isNull(), hasEntry("_page"), anyString())).thenReturn(page2);

        Iterable<Object> resources = provider.retrieve("Patient", "subject", "123", "Condition", null, null, null, null, null, null, null, null);
        assertNotNull(resources);

        Map<String, Resource> results = new HashMap<>();
        resources.forEach(o -> results.put(((Resource) o).getId(), (Resource) o));
        assertEquals(results.size(),2);

        Set<String> expected = new HashSet<>();
        expected.add(c1.getId());
        expected.add(c2.getId());
        assertEquals(results.keySet(), expected);

        verify(helpers, times(2)).doSearch(eq("Condition"), isNull(), isNull(), ArgumentMatchers.<MultivaluedMap<String, String>> any(), anyString());
    }

    private String getBaseUrl() {
        return "https://localhost:9443/fhir-server/api/v4";
    }

    private MultivaluedMap<String, String> hasEntry(String key) {
        return argThat(new HasEntry(key));
    }

    private static class HasEntry implements ArgumentMatcher<MultivaluedMap<String, String>> {

        private String key;

        public HasEntry(String key) {
            this.key = key;
        }

        @Override
        public boolean matches(MultivaluedMap<String, String> map) {
            return map.containsKey(key);
        }

        @Override
        public String toString() {
            return "[hasEntry(" + key + ")]";
        }
    }
}
