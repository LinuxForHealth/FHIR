package com.ibm.fhir.cql.engine.server.retrieve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;

import com.ibm.fhir.cql.engine.searchparam.SearchParameterResolver;
import com.ibm.fhir.cql.engine.server.terminology.ServerFhirTerminologyProvider;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Bundle.Link;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

public class ServerFhirRetrieveProviderTest {

    private ServerFhirTerminologyProvider termProvider;
    private ServerFhirRetrieveProvider provider;
    private FHIRResourceHelpers helpers;

    @Before
    public void setup() {
        helpers = mock(FHIRResourceHelpers.class);
        termProvider = mock(ServerFhirTerminologyProvider.class);

        SearchParameterResolver resolver = new SearchParameterResolver();

        provider = new ServerFhirRetrieveProvider(helpers, resolver);
        provider.setTerminologyProvider(termProvider);
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testQueryById() throws Exception {
        Patient p1 = Patient.builder().id("p1").build();

        SingleResourceResult result = mock(SingleResourceResult.class);
        when(result.isSuccess()).thenReturn(true);
        when(result.getResource()).thenReturn(p1);

        when(helpers.doRead(eq("Patient"), eq("123"), eq(true), eq(false), isNull())).thenReturn(result);

        Iterable<Object> resources = provider.retrieve("Patient", "id", "123", "Patient", null, null, null, null, null, null, null, null);
        assertNotNull(resources);

        Map<String, Resource> results = new HashMap<>();
        resources.forEach(o -> results.put(((Resource) o).getId(), (Resource) o));
        assertEquals(1, results.size());

        Set<String> expected = new HashSet<>();
        expected.add(p1.getId());
        assertEquals(expected, results.keySet());
    }

    @Test
    public void testMultiplePagesAllReadSuccessfully() throws Exception {
        Condition c1 = Condition.builder().id("c1").subject(Reference.builder().reference(com.ibm.fhir.model.type.String.of("Patient/123")).build()).build();

        Bundle page1 =
                Bundle.builder().type(BundleType.SEARCHSET).total(UnsignedInt.of(1)).entry(Bundle.Entry.builder().resource(c1).build()).link(Link.builder().relation(com.ibm.fhir.model.type.String.of("next")).url(Uri.of(getBaseUrl()
                        + "/Condition?subject:Patient=123&_page=2")).build()).build();

        Condition c2 = Condition.builder().id("c2").subject(Reference.builder().reference(com.ibm.fhir.model.type.String.of("Patient/123")).build()).build();

        Bundle page2 = Bundle.builder().type(BundleType.SEARCHSET).total(UnsignedInt.of(1)).entry(Bundle.Entry.builder().resource(c2).build()).build();

        when(helpers.doSearch(eq("Condition"), isNull(), isNull(), not(hasEntry("_page")), isNull(), isNull())).thenReturn(page1);
        when(helpers.doSearch(eq("Condition"), isNull(), isNull(), hasEntry("_page"), isNull(), isNull())).thenReturn(page2);

        Iterable<Object> resources = provider.retrieve("Patient", "subject", "123", "Condition", null, null, null, null, null, null, null, null);
        assertNotNull(resources);

        Map<String, Resource> results = new HashMap<>();
        resources.forEach(o -> results.put(((Resource) o).getId(), (Resource) o));
        assertEquals(2, results.size());

        Set<String> expected = new HashSet<>();
        expected.add(c1.getId());
        expected.add(c2.getId());
        assertEquals(expected, results.keySet());

        verify(helpers, times(2)).doSearch(eq("Condition"), isNull(), isNull(), ArgumentMatchers.<MultivaluedMap<String, String>> any(), isNull(), isNull());
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

        public boolean matches(MultivaluedMap<String, String> map) {
            return map.containsKey(key);
        }

        public String toString() {
            return "[hasEntry(" + key + ")]";
        }
    }
}
