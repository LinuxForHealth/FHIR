/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Composition;
import com.ibm.watsonhealth.fhir.model.CompositionStatusList;
import com.ibm.watsonhealth.fhir.model.ImmunizationRecommendation;
import com.ibm.watsonhealth.fhir.model.ImmunizationRecommendationRecommendation;
import com.ibm.watsonhealth.fhir.model.Meta;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryChainedParmTest extends AbstractPersistenceTest {
	
	private static final String PROFILE_URI_VALUE = "http://fhir.org/guides/argonaut/StructureDefinition/argo-patient";
    private static final String TAG_CODE_VALUE = "blah";

    /**
	 * Creates an Observation and Patient using the contents of json data files.
	 * Then the Observation is chained to the Patient, by way of FHIR references.
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized" })
	public void testCreateObservation_chained() throws Exception {
		
		Patient patient = readResource(Patient.class, "Patient_SalMonella.json");
		Meta meta = f.createMeta()
		        .withTag(f.createCoding().withCode(f.createCode().withValue(TAG_CODE_VALUE)))
		        .withProfile(f.createUri().withValue(PROFILE_URI_VALUE));
		patient.setMeta(meta);
		persistence.create(getDefaultPersistenceContext(), patient);
	    assertNotNull(patient);
	    assertNotNull(patient.getId());
	    assertNotNull(patient.getId().getValue());
	    assertNotNull(patient.getMeta());
	    assertNotNull(patient.getMeta().getVersionId().getValue());
	    assertEquals("1", patient.getMeta().getVersionId().getValue());
	    
        Observation otherObservation = readResource(Observation.class, "observation-example-without-id.canonical.json");
        persistence.create(getDefaultPersistenceContext(), otherObservation);
        assertNotNull(otherObservation);
        assertNotNull(otherObservation.getId());
        assertNotNull(otherObservation.getId().getValue());
        assertNotNull(otherObservation.getMeta());
        assertNotNull(otherObservation.getMeta().getVersionId().getValue());
        assertEquals("1", otherObservation.getMeta().getVersionId().getValue());
        
        Observation observation = readResource(Observation.class, "observation-without-subject.json");
        persistence.create(getDefaultPersistenceContext(), observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertNotNull(observation.getMeta().getVersionId().getValue());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        
        // "Connect" the observation to a patient
        Reference patientRef = f.createReference().withReference(f.createString().withValue("Patient/" + patient.getId().getValue()));
        observation.setSubject(patientRef);
        // "Connect" the observation to the other observation
        Reference otherObservationRef = f.createReference().withReference(f.createString().withValue("Observation/" + otherObservation.getId().getValue()));
        observation.getRelated().add(f.createObservationRelated().withTarget(otherObservationRef));
        persistence.update(getDefaultPersistenceContext(), observation.getId().getValue(), observation);
        assertEquals("2", observation.getMeta().getVersionId().getValue());
	}
	
	/**
     * Creates a Composition with a reference to a ImmunizationRecommendation because search parameter of number on doseNumber.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateComposition_chained() throws Exception {
        ImmunizationRecommendationRecommendation imm_recrec = f.createImmunizationRecommendationRecommendation()
                .withDate(f.createDateTime().withValue("2017-09-04"))
                .withVaccineCode(f.createCodeableConcept().withText(f.createString().withValue("a vaccine")))
                .withDoseNumber(f.createPositiveInt().withValue(BigInteger.valueOf(10)))
                .withForecastStatus(f.createCodeableConcept().withText(f.createString().withValue("cloudy")));
        
        ImmunizationRecommendation imm_rec = f.createImmunizationRecommendation()
               .withPatient(f.createReference().withDisplay(f.createString().withValue("Pat Ient")))
               .withRecommendation(imm_recrec);
        persistence.create(getDefaultPersistenceContext(), imm_rec);
        assertNotNull(imm_rec);
        assertNotNull(imm_rec.getId());
        assertNotNull(imm_rec.getId().getValue());
        assertNotNull(imm_rec.getMeta());
        assertNotNull(imm_rec.getMeta().getVersionId().getValue());
        assertEquals("1", imm_rec.getMeta().getVersionId().getValue());
        
        // "Connect" the observation to the patient and encounter via a Reference
        Reference immunizationRef = f.createReference().withReference(f.createString().withValue("ImmunizationRecommendation/" + imm_rec.getId().getValue()));
        
        Composition composition = f.createComposition()
                .withDate(f.createDateTime().withValue("2018"))
                .withType(f.createCodeableConcept().withText(f.createString().withValue("test")))
                .withTitle(f.createString().withValue("TEST"))
                .withStatus(f.createCompositionStatus().withValue(CompositionStatusList.ENTERED_IN_ERROR))
                .withSubject(immunizationRef);
        persistence.create(getDefaultPersistenceContext(), composition);
        assertNotNull(composition);
        assertNotNull(composition.getId());
        assertNotNull(composition.getId().getValue());
        assertNotNull(composition.getMeta());
        assertNotNull(composition.getMeta().getVersionId().getValue());
        assertEquals("1", composition.getMeta().getVersionId().getValue()); 
    }
	
	/**
	 * Tests a valid chained parameter query that retrieves all Observations associated with a Patient with 
	 * a given or family name beginning with 'Monella'
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
	public void testObservationQuery_chained_positiveString() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "patient.name", "Monella");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		
	}
	
	/**
     * Tests a valid chained parameter query that retrieves all Observations associated with a Patient with 
     * a given or family name beginning with 'Sam' or 'Monella'
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_positiveStringOR() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient.name", "Sam,Monella");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        resources = runQueryTest(Observation.class, persistence, "patient.name", "Monella,Sam");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
    }
	
	/**
     * Tests a valid chained parameter query that retrieves all Observations associated with a Patient with family name = 'Monella'
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_positiveStringExact() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient.name:exact", "Monella");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
	
	/**
     * Tests a valid chained parameter query that retrieves all Observations associated with a Patient with tag = 'blah'
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_positiveToken() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient._tag", TAG_CODE_VALUE);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Compositions that reference an ImmunizationRecommendation with doseNumber = 10
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateComposition_chained" })
    public void testCompositionQuery_chained_positiveNumber() throws Exception {
        List<Resource> resources = runQueryTest(Composition.class, persistence, "subject:ImmunizationRecommendation.dose-number", "10");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Compositions that reference an ImmunizationRecommendation with doseNumber > 9
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateComposition_chained" })
    public void testCompositionQuery_chained_positive_comparison_gtNumber() throws Exception {
        List<Resource> resources = runQueryTest(Composition.class, persistence, "subject:ImmunizationRecommendation.dose-number", "gt9");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Compositions that reference an ImmunizationRecommendation with doseNumber < 11
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateComposition_chained" })
    public void testCompositionQuery_chained_positive_comparison_ltNumber() throws Exception {
        List<Resource> resources = runQueryTest(Composition.class, persistence, "subject:ImmunizationRecommendation.dose-number", "lt11");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference a Patient with name containing 'afeljagadf'
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_positiveDate() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient.birthdate", "1900");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference a Patient with name containing 'afeljagadf'
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_positiveDateEq() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient.birthdate", "1900-01-01");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
//    PatCompmt
    
    /**
     * Tests a chained parameter query for Compositions that reference an ImmunizationRecommendation with a date of '2017-09-04'
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateComposition_chained" })
    public void testCompositionQuery_chained_postiveDateEq() throws Exception {
        List<Resource> resources = runQueryTest(Composition.class, persistence, "subject:ImmunizationRecommendation.date", "eq2017-09-04");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Compositions that reference an ImmunizationRecommendation with a date between '2017-09-02' and '2017-09-16
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateComposition_chained" })
    public void testCompositionQuery_chained_positiveDateRange() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>();  
        queryParms.put("subject:ImmunizationRecommendation.date", Arrays.asList("gt2017-09-02","lt2017-09-14"));
        List<Resource> resources = runQueryTest(Composition.class, persistence, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference a Patient with name containing 'afeljagadf'
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_positiveReference() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "related-target:Observation.patient", "Patient/example");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference another Observation with value-quatity of 185 lbs
     * [parameter]=[prefix][number]|[system]|[code]
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_positiveQuantity() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "related-target:Observation.value-quantity", "185||[lb_av]");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference a Patient with a profile of 'http://fhir.org/guides/argonaut/StructureDefinition/argo-patient'
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_positiveURI() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient._profile", PROFILE_URI_VALUE);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference a Subject (Device, Location, Patient, Group) with an identifier value of 'salmonella'.
     * This should result in an FHIRSearchException because Observation.subject.identifier could reference identifiers on multiple resource types.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" }, expectedExceptions = FHIRSearchException.class)
    public void testObservationQuery_chained_invalidWildcardToken() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "subject.identifier", "salmonella");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        
    }
	
	/**
	 * Tests a chained parameter query for Observations that reference a Patient with name containing 'afeljagadf' which should retrieve no Observations
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
	public void testObservationQuery_chained_negativeString() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "patient.name", "afeljagadf");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
		
	}
	
	/**
     * Tests a chained parameter query for Observations that reference a Patient with name beginning with either 'Penn' or 'Acillin' which should retrieve no Observations
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_negativeStringOR() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient.name", "Penn,Acillin");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
	
	/**
     * Tests a chained parameter query for Observations associated with a Patient with family name = 'Mon' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_negativeStringExact() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient.name:exact", "Mon");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
	
	/**
	 * Tests a chained parameter query for Observations that reference a Patient with tag = 'afeljagadf' which should retrieve no Observations 
	 * @throws Exception
	 */
	@Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
	public void testObservationQuery_chained_negativeToken() throws Exception {
	    List<Resource> resources = runQueryTest(Observation.class, persistence, "patient._tag", "afeljagadf");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
		 
	}
	
	/**
     * Tests a chained parameter query for Observations that reference a Patient with name containing 'afeljagadf' which should retrieve no Observations
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_negativeNumber() throws Exception {
        List<Resource> resources = runQueryTest(Composition.class, persistence, "subject:ImmunizationRecommendation.dose-number", "525600");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference a Patient with name containing 'afeljagadf' which should retrieve no Observations
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_negativeDate() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient.name", "afeljagadf");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
    
    /**
     * Tests a chained parameter query for Compositions that reference an ImmunizationRecommendation with a date of '2017-09-04' which should retrieve no Compositions
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateComposition_chained" })
    public void testCompositionQuery_chained_negativeDateEq() throws Exception {
        List<Resource> resources = runQueryTest(Composition.class, persistence, "subject:ImmunizationRecommendation.date", "eq2017-09-04T16:20:00Z");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
    
    /**
     * Tests a chained parameter query for Compositions that reference an ImmunizationRecommendation with a date between '2099-09-02' and '3000-09-14'
     * which should return no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateComposition_chained" })
    public void testCompositionQuery_chained_negativeDateRange() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>();  
        queryParms.put("subject:ImmunizationRecommendation.date", Arrays.asList("ge2099-09-02","le3000-09-14"));
        List<Resource> resources = runQueryTest(Composition.class, persistence, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference a Patient with name containing 'afeljagadf' which should retrieve no Observations
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_negativeReference() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "related-target:Observation.patient", "Patient/XXX");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference a Patient with name containing 'afeljagadf' which should retrieve no Observations
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_negativeQuantity() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "related-target:Observation.value-quantity", "2000||lbs");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
    
    /**
     * Tests a chained parameter query for Observations that reference a Patient with name containing 'afeljagadf' which should retrieve no Observations
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateObservation_chained" })
    public void testObservationQuery_chained_negativeURI() throws Exception {
        List<Resource> resources = runQueryTest(Observation.class, persistence, "patient._profile", "afeljagadf");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        
    }
}
