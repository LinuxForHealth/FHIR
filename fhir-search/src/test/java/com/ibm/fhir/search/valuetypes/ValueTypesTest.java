/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.valuetypes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.ActivityDefinition;
import com.ibm.fhir.model.resource.AdverseEvent;
import com.ibm.fhir.model.resource.Appointment;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.ClaimResponse;
import com.ibm.fhir.model.resource.MolecularSequence;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.Parameter;
import com.ibm.fhir.search.test.BaseSearchTest;

/**
 * Tests ValueType Gets
 * 
 * @author pbastide
 *
 */
public class ValueTypesTest extends BaseSearchTest {

    // --- Test Get Value Types with Simple Cases

    @Test
    public void testGetValueTypesPatientHumanName() throws Exception {
        /*
         * Checks the Value Types PatientName should
         */
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(Patient.class, "name");
        assertFalse(valueTypes.isEmpty());
        assertEquals(1, valueTypes.size());
        assertTrue(valueTypes.contains(HumanName.class));
    }

    @Test
    public void testGetValueTypesPatientInvalid() throws Exception {
        /*
         * Checks an invalid search that should be empty.
         */
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(Patient.class, "namex");
        assertTrue(valueTypes.isEmpty());
    }

    @Test
    public void testGetValueTypesNullClass() throws Exception {
        /*
         * checks a null class should be empty.
         */
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(null, "namex");
        assertTrue(valueTypes.isEmpty());
    }

    @Test
    public void testGetValueTypesInvalidClass() throws Exception {
        /*
         * checks an invalid class should be empty.
         */
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(Observation.class, "namex");
        assertTrue(valueTypes.isEmpty());
    }

    // --- Test more Complicated.

    @Test
    public void testGetValueTypesAdverseEventResultingCondition() throws Exception {
        /*
         * "expression" : "AdverseEvent.resultingCondition",
         */
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(AdverseEvent.class, "resultingcondition");
        assertFalse(valueTypes.isEmpty());
        assertEquals(valueTypes.size(), 1);
        assertTrue(valueTypes.contains(Reference.class));
    }

    @Test
    public void testGetValueTypesValueSet() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(ValueSet.class, "code");
        assertFalse(valueTypes.isEmpty());
        printValueTypes(valueTypes);
        assertEquals(valueTypes.size(), 1);
        assertTrue(valueTypes.contains(Code.class));
    }

    @Test
    public void testGetValueTypesPatientInvalidParameterName() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(Patient.class, "invalid-parameter-name");
        assertTrue(valueTypes.isEmpty());
        assertEquals(valueTypes.size(), 0);
    }

    @Test
    public void testGetValueTypes1() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(Observation.class, "_lastUpdated");
        assertEquals(valueTypes.size(), 1);
        assertTrue(valueTypes.contains(Instant.class));
    }

    @Test
    public void testGetValueTypes2() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(Patient.class, "_id");
        assertEquals(valueTypes.size(), 1);
        assertTrue(valueTypes.contains(com.ibm.fhir.model.type.Id.class));
    }

    @Test
    public void testGetValueTypeObservationDate() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(Observation.class, "date");
        printValueTypes(valueTypes);
        assertEquals(valueTypes.size(), 3);
        assertTrue(valueTypes.contains(Element.class));
    }

    @Test
    public void testGetValueTypeObservationCustom() throws Exception {
        /*
         * In prior, the original query is: <code> "code": "date", "base": "Observation", "type": "date", "xpath":
         * "f:Observation/f:effectiveDateTime | f:Observation/f:effectivePeriod" </code>
         */

        /*
         * R4 expression" : "AllergyIntolerance.recordedDate | CarePlan.period | CareTeam.period |
         * ClinicalImpression.date | Composition.date | Consent.dateTime | DiagnosticReport.effective | Encounter.period
         * | EpisodeOfCare.period | FamilyMemberHistory.date | Flag.period | Immunization.occurrence | List.date |
         * Observation.effective | Procedure.performed | (RiskAssessment.occurrence as dateTime) |
         * SupplyRequest.authoredOn",
         */
        FHIRRequestContext.set(new FHIRRequestContext("tenant4"));

        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(Observation.class, "date");
        printValueTypes(valueTypes);
        assertEquals(valueTypes.size(), 3);
        assertTrue(valueTypes.contains(Element.class));
        assertTrue(valueTypes.contains(DateTime.class));
        assertTrue(valueTypes.contains(Period.class));
    }

    @Test
    public void testGetValueTypeActivityDefinition() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(ActivityDefinition.class, "context-quantity");
        printValueTypes(valueTypes);
        assertEquals(valueTypes.size(), 2);
        assertTrue(valueTypes.contains(Quantity.class));
        assertTrue(valueTypes.contains(Range.class));
    }

    @Test
    public void testGetValueTypes4() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));
        Set<Class<?>> valueTypes = ValueTypesFactory.getValueTypesProcessor().getValueTypes(Observation.class, "date");
        assertEquals(valueTypes.size(), 3);
    }

    @Test
    public void testDateSearch() throws FHIRSearchException {
        Class<?> resourceType = ActivityDefinition.class;

        String name = "date";
        Parameter queryParm = new Parameter(Type.DATE, name, null, name);
        assertFalse(ValueTypesFactory.getValueTypesProcessor().isInstantSearch(resourceType, queryParm));

        name = "depends-on";
        queryParm = new Parameter(Type.REFERENCE, name, null, name);
        assertFalse(ValueTypesFactory.getValueTypesProcessor().isInstantSearch(resourceType, queryParm));

        // Appointment-date selects Appointment.start which is of type Instant
        resourceType = Appointment.class;
        name = "date";
        queryParm = new Parameter(Type.DATE, name, null, name);
        assertTrue(ValueTypesFactory.getValueTypesProcessor().isInstantSearch(resourceType, queryParm));

        resourceType = CapabilityStatement.class;
        name = "date";
        queryParm = new Parameter(Type.DATE, name, null, name);
        assertFalse(ValueTypesFactory.getValueTypesProcessor().isInstantSearch(resourceType, queryParm));

        resourceType = ClaimResponse.class;
        name = "payment-date";
        queryParm = new Parameter(Type.DATE, name, null, name);
        assertFalse(ValueTypesFactory.getValueTypesProcessor().isInstantSearch(resourceType, queryParm));

    }

    @Test
    public void testRangeSearch() throws FHIRSearchException {

        Class<?> resourceType = ActivityDefinition.class;

        String name = "context-quantity";
        Parameter queryParm = new Parameter(Type.QUANTITY, name, null, name);
        assertTrue(ValueTypesFactory.getValueTypesProcessor().isRangeSearch(resourceType, queryParm));
        
        name = "derived-from";
        queryParm = new Parameter(Type.REFERENCE, name, null, name);
        assertFalse(ValueTypesFactory.getValueTypesProcessor().isRangeSearch(resourceType, queryParm));

    }
    
    @Test
    public void testIntegerSearch() throws FHIRSearchException { 
        Class<?> resourceType = MolecularSequence.class;

        String name = "variant-end";
        Parameter queryParm = new Parameter(Type.NUMBER, name, null, name);
        assertTrue(ValueTypesFactory.getValueTypesProcessor().isIntegerSearch(resourceType, queryParm));
        
    }

}
