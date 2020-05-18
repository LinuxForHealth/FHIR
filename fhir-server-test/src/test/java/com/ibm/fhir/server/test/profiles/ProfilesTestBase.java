/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.testng.annotations.BeforeClass;

import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/*
 * This class is not designed to run its own.  The class does the basic lift to check: 
 * <li> is the profile valid to run on the server? 
 */
public abstract class ProfilesTestBase extends FHIRServerTestBase {
    private static final String CLASSNAME = ProfilesTestBase.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public static final String EXPRESSION_PROFILES = "rest.resource.supportedProfile";

    public Boolean check = Boolean.TRUE;
    public Boolean skip = Boolean.FALSE;

    /*
     * Each Test asserts the required profiles, and subsequent BeforeClass checks if it's on the server.
     */
    public abstract List<String> getRequiredProfiles();

    /*
     * set the check value, if true, it'll check the tests.
     */
    public void setCheck(Boolean check) {
        this.check = check;
    }

    @BeforeClass
    public void checkProfileExistsOnServer() throws Exception {
        CapabilityStatement conf = retrieveConformanceStatement();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(conf);
        // All the possible required profiles
        Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, EXPRESSION_PROFILES);
        Collection<String> listOfProfiles = tmpResults.stream().map( x -> x.getValue().asStringValue().string()).collect(Collectors.toList());
        System.out.println(listOfProfiles);
        for(String requiredProfile : getRequiredProfiles()) {
            boolean v = listOfProfiles.contains(requiredProfile);
            if(!v && check) {
                logger.warning("Profile not found marking as skip [" + requiredProfile + "]");
                check = Boolean.FALSE;
            } else {
                assertTrue(v);
            }
        }

        if(!check) {
            logger.info("Skipping Tests");
        }
    }
}