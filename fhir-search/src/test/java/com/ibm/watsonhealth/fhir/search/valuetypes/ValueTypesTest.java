/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.valuetypes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.Set;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.model.resource.MedicationAdministration;
import com.ibm.watsonhealth.fhir.model.resource.Observation;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.ValueSet;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.search.test.BaseSearchTest;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;
import com.ibm.watsonhealth.fhir.search.valuetypes.ValueTypesUtil;

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
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(Patient.class, "name");
        assertFalse(valueTypes.isEmpty());
        assertEquals(1, valueTypes.size());
        assertTrue(valueTypes.contains(HumanName.class));
    }

    @Test
    public void testGetValueTypesPatientInvalid() throws Exception {
        /*
         * Checks an invalid search that should be empty.
         */
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(Patient.class, "namex");
        assertTrue(valueTypes.isEmpty());
    }

    @Test
    public void testGetValueTypesNullClass() throws Exception {
        /*
         * checks a null class should be empty.
         */
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(null, "namex");
        assertTrue(valueTypes.isEmpty());
    }

    @Test
    public void testGetValueTypesInvalidClass() throws Exception {
        /*
         * checks an invalid class should be empty.
         */
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(SearchUtil.class, "namex");
        assertTrue(valueTypes.isEmpty());
    }

    // --- Test more Complicated.

    @Test
    public void testGetValueTypesObservationDate() throws Exception {
        /*
         * "expression" : Observation.effective
         */
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(Observation.class, "date");
        assertFalse(valueTypes.isEmpty());
        assertEquals(valueTypes.size(), 1);
        assertTrue(valueTypes.contains(Element.class));
    }

    @Test
    public void testGetValueTypeObservationInvalidPath() throws Exception {
        // Expression: Library.relatedArtifact.where(type='predecessor').resource
        // This path is invalid.
        Class<?> clz = ValueTypesUtil.getValueType(Observation.class, "Library.relatedArtifact.where(type='predecessor').resource");
        assertNull(clz);
    }

    @Test
    public void testGetValueTypeObservationValidPath() throws Exception {
        // Expression: Observation.effective
        // This path is valid.
        Class<?> clz = ValueTypesUtil.getValueType(Observation.class, "Observation.effective");
        assertNotNull(clz);
        System.out.println(clz.getSimpleName());
    }

    @Test
    public void testGetValueTypeMedicationAdministrationValid() throws Exception {
        String path = "MedicationAdministration.effective";
        Class<?> clz = ValueTypesUtil.getValueType(MedicationAdministration.class, path);
        assertNotNull(clz);
        System.out.println(clz.getSimpleName());
    }

    @Test
    public void testGetValueTypesValueSet() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(ValueSet.class, "code");
        assertFalse(valueTypes.isEmpty());
        printValueTypes(valueTypes);
        assertEquals(valueTypes.size(), 1);
        assertTrue(valueTypes.contains(Code.class));
    }

    @Test
    public void testGetValueTypesPatientInvalidParameterName() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(Patient.class, "invalid-parameter-name");
        assertTrue(valueTypes.isEmpty());
        assertEquals(valueTypes.size(), 0);
    }

    @Test
    public void testGetValueTypes1() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(Observation.class, "_lastUpdated");
        assertEquals(valueTypes.size(), 1);
        assertTrue(valueTypes.contains(Instant.class));
    }

    @Test
    public void testGetValueTypes2() throws Exception {
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(Patient.class, "_id");
        assertEquals(valueTypes.size(), 1);
        assertTrue(valueTypes.contains(com.ibm.watsonhealth.fhir.model.type.Id.class));
    }

    @Test
    public void testGetValueTypeObservationDate() throws Exception {

        /*
         * R4 expression" : "AllergyIntolerance.recordedDate | CarePlan.period | CareTeam.period |
         * ClinicalImpression.date | Composition.date | Consent.dateTime | DiagnosticReport.effective | Encounter.period
         * | EpisodeOfCare.period | FamilyMemberHistory.date | Flag.period | Immunization.occurrence | List.date |
         * Observation.effective | Procedure.performed | (RiskAssessment.occurrence as dateTime) |
         * SupplyRequest.authoredOn",
         */
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(Observation.class, "date");
        printValueTypes(valueTypes);
        assertEquals(valueTypes.size(), 1);
        assertTrue(valueTypes.contains(Element.class));
    }

    @Test
    public void testGetValueTypeObservationCustom() throws Exception {
        /*
         * In DSTU2, the original query is: <code> "code": "date", "base": "Observation", "type": "date", "xpath":
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
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(Observation.class, "value-range");
        printValueTypes(valueTypes);
        assertEquals(valueTypes.size(), 2);
        assertTrue(valueTypes.contains(ZonedDateTime.class));
        assertTrue(valueTypes.contains(Element.class));
    }

    @Test
    public void testGetValueTypes4() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));
        Set<Class<?>> valueTypes = ValueTypesUtil.getValueTypes(Observation.class, "date");
        assertEquals(valueTypes.size(), 1);
    }

    @Test
    public void testFieldObservation() {
        Field f = ValueTypesUtil.getField(Observation.class, "referenceRange");
        System.out.println(f);
        assertNotNull(f);

        f = ValueTypesUtil.getField(Observation.class, "modifierExtension");
        System.out.println(f);
        assertNotNull(f);
    }

}
