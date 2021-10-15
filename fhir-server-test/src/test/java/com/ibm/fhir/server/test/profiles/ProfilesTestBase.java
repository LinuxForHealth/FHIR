/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.ig.us.core.tool.USCoreExamplesUtil;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/*
 * This class is not designed to run its own.  The class does the basic lift to check:
 * <li> is the profile valid to run on the server?
 * <li> is the resource.id in the response bundle?
 */
public abstract class ProfilesTestBase extends FHIRServerTestBase {
    private static final String CLASSNAME = ProfilesTestBase.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public static final String PREFER_HEADER_RETURN_REPRESENTATION = "return=representation";
    public static final String PREFER_HEADER_NAME = "Prefer";

    public static final String EXPRESSION_PROFILES = "rest.resource.supportedProfile";
    public static final String EXPRESSION_BUNDLE_IDS = "entry.resource.id";

    private Collection<String> listOfProfiles = null;

    private Boolean skip = Boolean.TRUE;

    /*
     * Each Test asserts the required profiles, and subsequent BeforeClass checks if it's on the server.
     */
    public abstract List<String> getRequiredProfiles();

    /*
     * set the check value, if true, it'll check the tests.
     */
    public abstract void setCheck(Boolean check);

    public void assertBaseBundleNotEmpty(Bundle bundle) {
        assertNotNull(bundle);
        assertFalse(bundle.getEntry().isEmpty());
    }

    /**
     * builds a valid us core object
     * @param cls
     * @param version
     * @param resource
     * @return
     * @throws Exception
     */
    public String buildAndAssertOnResourceForUsCore(String cls, String version, String resource) throws Exception {
        WebTarget target = getWebTarget();
        Resource goal = USCoreExamplesUtil.readLocalJSONResource(version, resource);

        Entity<? extends Resource> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path(cls).request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/MedicationRequest/12354 (first actual test, but simple)
        String id = getLocationLogicalId(response);
        response = target.path(cls + "/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        this.addToResourceRegistry(cls, id);
        return id;
    }

    @BeforeMethod
    protected void checkProfile() {
        if (skip) {
            throw new SkipException("Skipping tests profile " + getClass().getCanonicalName());
        }
    }

    /**
     * checks that the bundle contains resources with the given ids.
     *
     * @param bundle
     * @param ids
     * @throws FHIRPathException
     */
    public static void assertContainsIds(Bundle bundle, String... ids) throws FHIRPathException {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(bundle);
        Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, EXPRESSION_BUNDLE_IDS);
        Collection<String> listOfIds = tmpResults.stream().map(x -> x.toString()).collect(Collectors.toList());
        for (String id : ids) {
            assertTrue(listOfIds.contains(id));
        }
    }

    public static void assertDoesNotContainsIds(Bundle bundle, String... ids) throws FHIRPathException {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(bundle);
        Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, EXPRESSION_BUNDLE_IDS);
        Collection<String> listOfIds = tmpResults.stream().map(x -> x.toString()).collect(Collectors.toList());
        boolean found = false;
        for (String id : ids) {
            found = found || listOfIds.contains(id);
        }
        assertFalse(found);
    }

    public void assertSearchResponse(FHIRResponse response, int expectedStatusCode) throws Exception {
        assertNotNull(response);
        if (expectedStatusCode != response.getStatus()) {
            OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
            printOutResource(true, operationOutcome);
        }
        assertEquals(expectedStatusCode, response.getStatus());
    }

    public Bundle getEntityWithExtraWork(Response response, String method) throws Exception {
        Bundle responseBundle = response.readEntity(Bundle.class);
        commonWork(responseBundle,method);
        return responseBundle;
    }

    public void commonWork(Bundle responseBundle, String method) throws Exception{
        assertNotNull(responseBundle);
        checkForIssuesWithValidation(responseBundle, true, false, false);
    }

    public void grabProfilesFromServerOneTime() throws Exception {
        if (listOfProfiles == null) {
            CapabilityStatement conf = retrieveConformanceStatement();
            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(conf);
            // All the possible required profiles
            Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, EXPRESSION_PROFILES);
            listOfProfiles = tmpResults.stream().map(x -> x.getValue().asStringValue().string()).collect(Collectors.toList());
        }
    }

    @BeforeClass
    public void checkProfileExistsOnServer() throws Exception {
        grabProfilesFromServerOneTime();
        List<String> requiredProfiles = getRequiredProfiles();
        Map<String, Integer> checks = requiredProfiles.stream().collect(Collectors.toMap(x -> "" + x, x -> new Integer(0)));
        for (String requiredProfile : requiredProfiles) {
            boolean v = listOfProfiles.contains(requiredProfile);
            if (!v) {
                logger.warning("Profile not found marking as skip [" + requiredProfile + "]");
            } else {
                checks.put(requiredProfile, checks.get(requiredProfile).intValue() + 1);
            }
        }

        boolean skip = false;
        for (Entry<String, Integer> entry : checks.entrySet()) {
            skip = skip || entry.getValue() == 0;
        }
        setCheck(skip);
        this.skip = skip;
    }

    /**
     * V2 which hides check values.
     */
    public abstract static class ProfilesTestBaseV2 extends ProfilesTestBase {
        Boolean check = false;

        @Override
        public void setCheck(Boolean check) {
            this.check = check;
        }

        @BeforeClass
        public void runLoad() throws Exception {
            loadResources();
        }

        public abstract void loadResources() throws Exception;
    }
}