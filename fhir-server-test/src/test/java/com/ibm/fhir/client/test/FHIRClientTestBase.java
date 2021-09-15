/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Properties;

import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.ibm.fhir.client.FHIRClient;
import com.ibm.fhir.client.FHIRClientFactory;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;

/**
 * Base class for fhir-client TestNG tests.
 */
public abstract class FHIRClientTestBase {
    protected Properties testProperties = null;
    protected Properties testPropertiesOAuth2 = null;
    protected FHIRClient client = null;
    protected FHIRClient clientOAuth2 = null;

    public FHIRClientTestBase() {
        // No Operation
    }

    /**
     * Do one-time setup to enable tests to run.
     */
    @BeforeClass
    public void setup() throws Exception {
        testProperties = TestUtil.readTestProperties("test.properties");
        testPropertiesOAuth2 = TestUtil.readTestProperties("test.oauth2.properties");
    }

    @BeforeMethod
    public void createClientAuthClient() throws Exception {
        client = FHIRClientFactory.getClient(testProperties);
    }

    @BeforeMethod
    public void createOauthClient() throws Exception {
        clientOAuth2 = FHIRClientFactory.getClient(testPropertiesOAuth2);
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
        assertNotNull(oo.getId());
        assertEquals("exception", oo.getId());

        // Make sure the OperationOutcomeIssue has a message containing 'msgPart'.
        assertNotNull(oo.getIssue());
        assertEquals(1, oo.getIssue().size());
        Issue ooi = oo.getIssue().get(0);
        assertNotNull(ooi);
        assertNotNull(ooi.getCode());
        assertNotNull(ooi.getCode().getValue());
        assertEquals(IssueType.EXCEPTION.getValue(), ooi.getCode().getValue());

        assertNotNull(ooi.getSeverity());
        assertNotNull(ooi.getSeverity().getValue());
        assertEquals(IssueSeverity.FATAL.getValue(), ooi.getSeverity().getValue());

        assertNotNull(ooi.getDiagnostics());
        String msg = ooi.getDiagnostics().getValue();
        assertNotNull(msg);
        assertTrue(msg.contains(msgPart));
    }


    protected void assertValidationOperationOutcome(OperationOutcome oo, String msgPart) {
        assertNotNull(oo);

        // Verify the id attribute.
        assertNotNull(oo.getId());
        assertNotNull(oo.getId());
        assertEquals("validationfail", oo.getId());

        // Make sure that we can find the 'msgPart' in one of the OperationOutcomeIssue objects.
        boolean foundIt = false;
        assertNotNull(oo.getIssue());
        assertFalse(oo.getIssue().isEmpty());
        for (Issue ooi : oo.getIssue()) {
            assertNotNull(ooi.getCode());
            assertNotNull(ooi.getCode().getValue());
            assertEquals(IssueType.INVALID.getValue(), ooi.getCode().getValue());

            assertNotNull(ooi.getSeverity());
            assertNotNull(ooi.getSeverity().getValue());
            assertEquals(IssueSeverity.ERROR.getValue(), ooi.getSeverity().getValue());

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
     *     <code>[base]/<resource-type>/<id>/_history/<version></code>
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
