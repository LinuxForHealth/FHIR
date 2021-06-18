package com.ibm.fhir.cql.engine.searchparam;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.SearchParamType;

public class SearchParameterResolverTest {
    
    SearchParameterResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new SearchParameterResolver();
    }
    
    @Test
    public void testResolveConditionCode() throws Exception {
        SearchParameter param = resolver.getSearchParameterDefinition("Condition", "code", SearchParamType.TOKEN);
        assertNotNull("Could not resolve search parameter for Condition.code", param);
    }
    
    @Test
    public void testResolveConditionSubject() throws Exception {
        SearchParameter param = resolver.getSearchParameterDefinition("Condition", "subject");
        assertNotNull("Could not resolve search parameter for Condition.subject", param);
    }
    
    @Test
    public void testResolvePatientID() throws Exception {
        SearchParameter param = resolver.getSearchParameterDefinition("Patient", "id");
        assertNotNull("Could not resolve search parameter for Patient.id", param);
    }
    
    @Test
    public void testResolveMedicationRequestMedication() throws Exception {
        SearchParameter param = resolver.getSearchParameterDefinition("MedicationRequest", "medication");
        assertNotNull("Could not resolve search parameter for MedicationRequest.medication", param);
    }
    
    @Test
    public void testResolveAllergyIntoleranceCode() throws Exception {
        SearchParameter param = resolver.getSearchParameterDefinition("AllergyIntolerance", "code");
        assertNotNull("Could not resolve search parameter for MedicationRequest.medication", param);
    }
}
