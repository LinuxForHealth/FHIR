/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.ibm.watsonhealth.fhir.client.FHIRClient;
import com.ibm.watsonhealth.fhir.client.FHIRClientFactory;
import com.ibm.watsonhealth.fhir.model.IssueSeverityList;
import com.ibm.watsonhealth.fhir.model.IssueTypeList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.test.FHIRModelTestBase;

/**
 * Base class for fhir-client TestNG tests.
 */
public abstract class FHIRClientTestBase extends FHIRModelTestBase {
    protected ObjectFactory objFactory = null;
    protected Properties testProperties = null;
    protected FHIRClient client = null;
    
    public FHIRClientTestBase() {
        objFactory = new ObjectFactory();
    }
    
    public ObjectFactory getObjectFactory() {
        return objFactory;
    }

    /**
     * Do one-time setup to enable tests to run.
     */
    @BeforeClass
    public void setUp() throws Exception {
        testProperties = readTestProperties("test.properties");
    }
    
    @BeforeMethod
    public void createClient() throws Exception {
        client = FHIRClientFactory.getClient(testProperties);
    }
    
    /**
     * Verify that the status code in the Response is equal to the expected status code.
     *
     * @param response the Response to verify
     * @param expectedStatusCode the expected status code value
     */
    protected void assertResponse(Response response, int expectedStatusCode) {
        assertNotNull(response);
        assertEquals(expectedStatusCode, response.getStatus());
    }
    
    /**
     * Validate the specified OperationOutcome object to make sure it contains
     * an exception.
     * @param msgPart a string which should be found in the exception message.
     */
    protected void assertExceptionOperationOutcome(OperationOutcome oo, String msgPart) {
        assertNotNull(oo);
        
        // Verify the id attribute.
        assertNotNull(oo.getId());
        assertNotNull(oo.getId().getValue());
        assertEquals("exception", oo.getId().getValue());
        
        // Make sure the OperationOutcomeIssue has a message containing 'msgPart'.
        assertNotNull(oo.getIssue());
        assertEquals(1, oo.getIssue().size());
        OperationOutcomeIssue ooi = oo.getIssue().get(0);
        assertNotNull(ooi);
        assertNotNull(ooi.getCode());
        assertNotNull(ooi.getCode().getValue());
        assertEquals(IssueTypeList.EXCEPTION, ooi.getCode().getValue());
        
        assertNotNull(ooi.getSeverity());
        assertNotNull(ooi.getSeverity().getValue());
        assertEquals(IssueSeverityList.FATAL, ooi.getSeverity().getValue());
        
        assertNotNull(ooi.getDiagnostics());
        String msg = ooi.getDiagnostics().getValue();
        assertNotNull(msg);
        assertTrue(msg.contains(msgPart));
    }
    

    protected void assertValidationOperationOutcome(OperationOutcome oo, String msgPart) {
        assertNotNull(oo);
        
        // Verify the id attribute.
        assertNotNull(oo.getId());
        assertNotNull(oo.getId().getValue());
        assertEquals("validationfail", oo.getId().getValue());
        
        // Make sure that we can find the 'msgPart' in one of the OperationOutcomeIssue objects.
        boolean foundIt = false;
        assertNotNull(oo.getIssue());
        assertFalse(oo.getIssue().isEmpty());
        for (OperationOutcomeIssue ooi : oo.getIssue()) {
            assertNotNull(ooi.getCode());
            assertNotNull(ooi.getCode().getValue());
            assertEquals(IssueTypeList.INVALID, ooi.getCode().getValue());
            
            assertNotNull(ooi.getSeverity());
            assertNotNull(ooi.getSeverity().getValue());
            assertEquals(IssueSeverityList.ERROR, ooi.getSeverity().getValue());
            
            assertNotNull(ooi.getDiagnostics());
            String msg = ooi.getDiagnostics().getValue();
            assertNotNull(msg);
            
            if (msg.contains(msgPart)) {
                foundIt = true;
            }
        }
        assertTrue("Could not find '" + msgPart + "' in OperationOutcomeIssue list!", foundIt);
    }
    
    /**
     * For the specified response, this function will extract the logical id value from the
     * response's Location header.
     * The format of a location header value should be:
     *     "[base]/<resource-type>/<id>/_history/<version>"
     * @param response the response object for a REST API invocation
     * @return the logical id value
     */
    protected String getLocationLogicalId(Response response) {
        String location = response.getLocation().toString();
        assertNotNull(location);
        assertFalse(location.isEmpty());
        // System.out.println("Location value: " + location);
        
        String[] tokens = location.split("/");
        assertNotNull(tokens);
        assertTrue(tokens.length >= 4);
    
        String logicalId = tokens[tokens.length-3];
        assertNotNull(logicalId);
        assertFalse(logicalId.isEmpty());
    
        return logicalId;
    }
}
