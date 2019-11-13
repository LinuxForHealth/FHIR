/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.RelatedPerson;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.persistence.test.common.AbstractPersistenceTest;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * _id and _lastUpdated are first class citizens in the FHIR Schema. These first
 * class citizens need to be tested under very specific conditions.
 * <br>
 * The following conditions are tested in the _id and _lastUpdated:
 * <code>
 * none
 * _id
 * _lastUpdated
 * _id and _lastUpdated
 * _lastUpdated and _id (intentionally in this order) 
 * _id status
 * _lastUpdated status
 * _id and _lastUpdated status
 * _lastUpdated and _id (intentionally in this order) status
 * status _id (intentionally in this order)
 * status _lastUpdated (intentionally in this order)
 * </code>
 * <br>
 * The specific search types are also tested:
 * <ul>
 * <li>Resource</li>
 * <li>Compartment</li>
 * <li>Global</li>
 * </ul>
 */
public class IdAndLastUpdatedSchemaTest extends AbstractPersistenceTest {

    Patient savedPatient;
    Device savedDevice;
    Encounter savedEncounter;
    Practitioner savedPractitioner;
    RelatedPerson savedRelatedPerson;
    Observation savedObservation;

    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
        Properties properties = TestUtil.readTestProperties("test.jdbc.properties");
        return new FHIRPersistenceJDBCImpl(properties);
    }

    @Override
    public void bootstrapDatabase() throws Exception {
        Properties properties = TestUtil.readTestProperties("test.jdbc.properties");

        DerbyInitializer derbyInit;
        String dbDriverName = properties.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(properties);
            derbyInit.bootstrapDb(false);
        }
    }

    private Reference buildReference(Resource resource) {
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getId().getValue());

        String resourceTypeName = FHIRUtil.getResourceTypeName(resource);
        return Reference.builder()
                .reference(string(resourceTypeName + "/" + resource.getId().getValue()))
                .build();
    }

    @Test
    public void testSearchId() throws Exception {
        Observation.Builder observationBuilder =
                ((Observation) TestUtil.readExampleResource("json/ibm/minimal/Observation-1.json")).toBuilder();

        Patient patient = TestUtil.readExampleResource("json/ibm/minimal/Patient-1.json");
        savedPatient = persistence.create(getDefaultPersistenceContext(), patient).getResource();
        observationBuilder.subject(buildReference(savedPatient));
        observationBuilder.performer(buildReference(savedPatient));

        Device device = TestUtil.readExampleResource("json/ibm/minimal/Device-1.json");
        savedDevice = persistence.create(getDefaultPersistenceContext(), device).getResource();
        observationBuilder.device(buildReference(savedDevice));

        Encounter encounter = TestUtil.readExampleResource("json/ibm/minimal/Encounter-1.json");
        savedEncounter = persistence.create(getDefaultPersistenceContext(), encounter).getResource();
        observationBuilder.encounter(buildReference(savedEncounter));

        Practitioner practitioner = TestUtil.readExampleResource("json/ibm/minimal/Practitioner-1.json");
        savedPractitioner = persistence.create(getDefaultPersistenceContext(), practitioner).getResource();
        observationBuilder.performer(buildReference(savedPractitioner));

        RelatedPerson relatedPerson = TestUtil.readExampleResource("json/ibm/minimal/RelatedPerson-1.json");
        savedRelatedPerson = persistence.create(getDefaultPersistenceContext(), relatedPerson).getResource();
        observationBuilder.performer(buildReference(savedRelatedPerson));

        savedObservation = persistence.create(getDefaultPersistenceContext(), observationBuilder.build()).getResource();
        assertNotNull(savedObservation);
        assertNotNull(savedObservation.getId());
        assertNotNull(savedObservation.getId().getValue());
        assertNotNull(savedObservation.getMeta());
        assertNotNull(savedObservation.getMeta().getVersionId().getValue());
        assertEquals("1", savedObservation.getMeta().getVersionId().getValue());

        List<Resource> results =
                runSearchTest(null, null,
                        Observation.class, "_id", savedObservation.getId().getValue(), null);
        assertTrue(results.size() > 0);
        
        results =
                runSearchTest(null, null,
                        Observation.class, "_lastUpdated", savedObservation.getMeta().getLastUpdated().getValue().toString(), null);
        assertTrue(results.size() > 0);

    }

    protected List<Resource> runSearchTest(String compartmentName, String compartmentLogicalId,
            Class<? extends Resource> resourceType, String parmName, String parmValue, Integer maxPageSize)
            throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        if (parmName != null && parmValue != null) {
            queryParms.put(parmName, Collections.singletonList(parmValue));
        }

        FHIRSearchContext searchContext =
                SearchUtil.parseQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParms, null);

        if (maxPageSize != null) {
            searchContext.setPageSize(maxPageSize);
        }
        FHIRPersistenceContext persistenceContext = getPersistenceContextForSearch(searchContext);
        MultiResourceResult<Resource> result = persistence.search(persistenceContext, resourceType);
        assertNotNull(result.getResource());
        return result.getResource();
    }

}
