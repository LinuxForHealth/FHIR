/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.ObservationComponent;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryObservationTest extends AbstractPersistenceTest {
	
	private static Patient savedPatient;
	
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreatePatient() throws Exception {
    	try {
   		Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

    	persistence.create(patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
        savedPatient = patient;
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Tests the FHIRPersistenceCloudantImpl create API for an Observation.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateObservation1() throws Exception {
        Observation observation = buildObservation(savedPatient.getId().getValue(), "Observation1.json");

        persistence.create(observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        assertNotNull(observation.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for an Observation.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateObservation2() throws Exception {
        Observation observation = buildObservation("example", "observation-example.canonical.json");

        persistence.create(observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        assertNotNull(observation.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for an Observation.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateObservation3() throws Exception {
        Observation observation = buildObservation("blood-pressure", "observation-example-bloodpressure.canonical.json");

        persistence.create(observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        assertNotNull(observation.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for an Observation.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateObservation4() throws Exception {
        Observation observation = buildObservation("blood-pressure", "obs-uslab-example8.canonical.json");

        persistence.create(observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        assertNotNull(observation.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for an Observation.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateObservation5() throws Exception {
        Observation observation = readResource(Observation.class, "Observation5.json");

        persistence.create(observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
        assertNotNull(observation.getMeta().getVersionId().getValue());
    }
    	
	/**
	 * Tests a query for an Observation with component-value-string = 'Systolic' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1" })
	public void testObservationQuery_componentValueString1() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "component-value-string", "Systolic");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<ObservationComponent> compList = ((Observation)resources.get(0)).getComponent();
		assertEquals(compList.get(0).getValueString().getValue(),"Systolic");
	}
	
	/**
	 * Tests a query for an Observation with component-value-string = 'Diastolic' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1" })
	public void testObservationQuery_componentValueString2() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "component-value-string", "Diastolic");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		List<ObservationComponent> compList = ((Observation)resources.get(0)).getComponent();
		assertEquals(compList.get(1).getValueString().getValue(),"Diastolic");
	}
	
	/**
	 * Tests a query for an Observation with value-string = 'Diastolic' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1" })
	public void testObservationQuery_valueString_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-string", "Diastolic");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for an Observation with encounter = 'Encounter/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_encounter() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "encounter", "Encounter/example");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getEncounter().getReference().getValue(),"Encounter/example");
	}
	
	/**
	 * Tests a query for an Observation with patient = 'Patient/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_Patient() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "patient", "Patient/example");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getSubject().getReference().getValue(),"Patient/example");
	}
	
	/**
	 * Tests a query for an Observation with subject = 'Patient/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_subject() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "subject", "Patient/example");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getSubject().getReference().getValue(),"Patient/example");
	}
	
	/**
	 * Tests a query for an Observation with performer = 'Practitioner/example' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation3" })
	public void testObservationQuery_performer() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "performer", "Practitioner/example");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getPerformer().get(0).getReference().getValue(),"Practitioner/example");
	}
	
	/**
	 * Tests a query for an Observation with specimen = 'Specimen/spec-uslab-example2' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation4" })
	public void testObservationQuery_specimen() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "specimen", "Specimen/spec-uslab-example2");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getSpecimen().getReference().getValue(),"Specimen/spec-uslab-example2");
	}
	
	/**
	 * Tests a query for an Observation with date = '2012-09-17' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation3" })
	public void testObservationQuery_date() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "date", "2012-09-17");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getEffectiveDateTime().getValue(),"2012-09-17");
	}
	
	/**
	 * Tests a query for an Observation with value-date = '2014-12-04T15:42:15-08:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation4" })
	public void testObservationQuery_valueDate() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-date", "2014-12-04T15:42:15-08:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValueDateTime().getValue(),"2014-12-04T15:42:15-08:00");
	}
	
	/**
	 * Tests a query for an Observation with value-date (valuePeriod - start) = '2014-11-04T15:42:15-08:00' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation4" })
	public void testObservationQuery_valueDate_valuePeriodStart() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-date", "2014-11-04T15:42:15-08:00");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValuePeriod().getStart().getValue(),"2014-11-04T15:42:15-08:00");
	}
	
	/**
	 * Tests a query for an Observation with value-quantity = '185|http://unitsofmeasure.org|[lb_av]' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_valueQuantity() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-quantity", "185|http://unitsofmeasure.org|[lb_av]");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValueQuantity().getValue().getValue().toString(),"185");
	}
	
	/**
	 * Tests a query for an Observation with value-quantity = 'le185|http://unitsofmeasure.org|[lb_av]' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_LEvalueQuantity() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-quantity", "le185|http://unitsofmeasure.org|[lb_av]");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValueQuantity().getValue().getValue().toString(),"185");
	}
	
	/**
	 * Tests a query for an Observation with value-quantity = 'lt186|http://unitsofmeasure.org|[lb_av]' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_LTvalueQuantity() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-quantity", "lt186|http://unitsofmeasure.org|[lb_av]");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValueQuantity().getValue().getValue().toString(),"185");
	}
	
	/**
	 * Tests a query for an Observation with value-quantity = 'gt186|http://unitsofmeasure.org|[lb_av]' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_GTvalueQuantity() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-quantity", "gt186|http://unitsofmeasure.org|[lb_av]");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for an Observation with value-quantity = 'ge185|http://unitsofmeasure.org|[lb_av]' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_GEvalueQuantity() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-quantity", "ge185|http://unitsofmeasure.org|[lb_av]");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValueQuantity().getValue().getValue().toString(),"185");
	}
	
	/**
	 * Tests a query for an Observation with value-quantity = 'eq185|http://unitsofmeasure.org|[lb_av]' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_EQvalueQuantity() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-quantity", "eq185|http://unitsofmeasure.org|[lb_av]");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValueQuantity().getValue().getValue().toString(),"185");
	}
	
	/**
	 * Tests a query for an Observation with value-quantity = 'ne186|http://unitsofmeasure.org|[lb_av]' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation2" })
	public void testObservationQuery_NEvalueQuantity() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "value-quantity", "ne186|http://unitsofmeasure.org|[lb_av]");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getValueQuantity().getValue().getValue().toString(),"185");
	}
	
	/**
	 * Tests a query for an Observation with category = 'vital-signs' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1" })
	public void testObservationQuery_categoryCode() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "category", "vital-signs");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getCategory().getCoding().get(0).getCode().getValue(),"vital-signs");
	}
	
	/**
	 * Tests a query for an Observation with category = 'http://hl7.org/fhir/observation-category|vital-signs' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1" })
	public void testObservationQuery_categorySystemCode() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "category", "http://hl7.org/fhir/observation-category|vital-signs");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getCategory().getCoding().get(0).getCode().getValue(),"vital-signs");
		assertEquals(((Observation)resources.get(0)).getCategory().getCoding().get(0).getSystem().getValue(),"http://hl7.org/fhir/observation-category");
	}
	
	/**
	 * Tests a query for an Observation with category = 'http://hl7.org/fhir/observation-category|vital-signs1' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1" })
	public void testObservationQuery_categorySystemCode_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "category", "http://hl7.org/fhir/observation-category|vital-signs1");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for an Observation with component-code = 'http://loinc.org|8453-3' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation5" })
	public void testObservationQuery_componentCode() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "component-code", "http://loinc.org|8453-3");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getCode().getCoding().get(0).getCode().getValue(),"8453-3");
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getCode().getCoding().get(0).getSystem().getValue(),"http://loinc.org");
	}
	
	/**
	 * Tests a query for an Observation with component-value-quantity = '93.7||mmHg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation5" })
	public void testObservationQuery_componentValueQuantity() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "component-value-quantity", "93.7||mmHg");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getValue().getValue()+"","93.7");
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getUnit().getValue(),"mmHg");
	}
	
	/**
	 * Tests a query for an Observation with component-value-quantity = 'le93.7||mmHg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation5" })
	public void testObservationQuery_componentValueQuantity_LE() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "component-value-quantity", "le93.7||mmHg");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getValue().getValue()+"","93.7");
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getUnit().getValue(),"mmHg");
	}
	
	/**
	 * Tests a query for an Observation with component-value-quantity = 'le93.6||mmHg' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation5" })
	public void testObservationQuery_componentValueQuantity_LE_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "component-value-quantity", "le93.6||mmHg");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for an Observation with component-value-quantity = 'ge93.7||mmHg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation5" })
	public void testObservationQuery_componentValueQuantity_GE() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "component-value-quantity", "ge93.7||mmHg");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getValue().getValue()+"","93.7");
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getUnit().getValue(),"mmHg");
	}
	
	/**
	 * Tests a query for an Observation with component-value-quantity = 'lt93.7||mmHg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation5" })
	public void testObservationQuery_componentValueQuantity_LT() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "component-value-quantity", "lt93.8||mmHg");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getValue().getValue().toString(),"93.7");
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getUnit().getValue(),"mmHg");
	}
	
	/**
	 * Tests a query for an Observation with component-value-quantity = 'gt93.7||mmHg' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation5" })
	public void testObservationQuery_componentValueQuantity_GT() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "component-value-quantity", "gt93||mmHg");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getValue().getValue().toString(),"93.7");
		assertEquals(((Observation)resources.get(0)).getComponent().get(1).getValueQuantity().getUnit().getValue(),"mmHg");
	}
	
	/**
	 * Tests a query for an Observation with status = 'final' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation5" })
	public void testObservationQuery_status() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "status", "final");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getStatus().getValue().toString().toLowerCase(), "final");
	}
	
	/**
	 * Tests a query for an Observation with status = 'draft' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation5" })
	public void testObservationQuery_status_noResults() throws Exception {
		List<Resource> resources = runQueryTest(Observation.class, persistence, "status", "draft");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/*
	 * Pagination Testcases
	 */
	
	/**
	 * Tests a query with a resource type but without any query parameters. This should yield correct results using pagination
	 * 
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation1", "testCreateObservation2", "testCreateObservation3", "testCreateObservation4" })
	public void testObservationPagination_001() throws Exception {
		
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for an Observation with date = '2012-09-17' which should yield correct results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation3" })
	public void testObservationPagination_002() throws Exception {
		
		String parmName = "date";
		String parmValue = "2012-09-17";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Observation)resources.get(0)).getEffectiveDateTime().getValue(),"2012-09-17");
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
	}
	
	/**
	 * Tests a query for an Observation with date = '2025-09-17' which should yield no results using pagination
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateObservation3" })
	public void testObservationPagination_003() throws Exception {
		
		String parmName = "date";
		String parmValue = "2025-09-17";
		Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
		
		queryParms.put(parmName, Collections.singletonList(parmValue));
		FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms);
		context.setPageNumber(1);
		List<Resource> resources = persistence.search(Observation.class, context);
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
		long count = context.getTotalCount();
		int pageSize = context.getPageSize();
		int lastPgNum = context.getLastPageNumber();
		assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
		assertTrue((count == 0) && (lastPgNum == 0));
	}
}
