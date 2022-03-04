/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry.Response;
import com.ibm.fhir.model.resource.Bundle.Entry.Search;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.SearchEntryMode;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.util.FHIRRestHelper;

public class BundleValidationTest {

    FHIRPersistence persistence;
    FHIRRestHelper helper;

    @BeforeClass
    void setup() throws FHIRException {
        persistence = new MockPersistenceImpl();
        helper = new FHIRRestHelper(persistence, new SearchUtil());
    }

    /**
     * Test no bundle.
     */
    @Test
    public void testNoBundle() throws Exception {
        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Bundle parameter is missing or empty.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.REQUIRED);
        }
    }

    /**
     * Test invalid bundle type.
     */
    @Test
    public void testInvalidBundleType() throws Exception {
        Condition condition = Condition.builder()
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test"))
                .resource(condition)
                .request(bundleEntryRequest)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.COLLECTION)
                .entry(bundleEntry)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Bundle.type must be either 'batch' or 'transaction'.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.VALUE);
        }
    }

    /**
     * Test invalid bundle total (bundle constraint bdl-1).
     */
    @Test
    public void testInvalidBundleTotal() throws Exception {
        Condition condition = Condition.builder()
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test"))
                .resource(condition)
                .request(bundleEntryRequest)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .total(UnsignedInt.of(10))
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 1);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "Bundle.total must be empty.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
        }
    }

    /**
     * Test no bundle entry request (bundle constraint bdl-3).
     */
    @Test
    public void testNoBundleEntryRequest() throws Exception {
        Condition condition = Condition.builder()
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test"))
                .resource(condition)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertEquals(issues.get(1).getDetails().getText().getValue(), "Bundle.Entry is missing the 'request' field.");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(1).getCode(), IssueType.REQUIRED);
        }
    }

    /**
     * Test duplicate local identifier.
     */
    @Test
    public void testDuplicateLocalIdentifier() throws Exception {
        Condition condition = Condition.builder()
                .id("1")
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();
        Condition condition2 = Condition.builder()
                .id("2")
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test"))
                .resource(condition)
                .request(bundleEntryRequest)
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test"))
                .resource(condition2)
                .request(bundleEntryRequest2)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertEquals(issues.get(1).getDetails().getText().getValue(), "Duplicate local identifier encountered in bundled request entry: urn:test");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(1).getCode(), IssueType.DUPLICATE);
        }
    }

    /**
     * Test invalid bundle entry search (bundle constraint bdl-2).
     */
    @Test
    public void testInvalidBundleEntrySearch() throws Exception {
        Condition condition = Condition.builder()
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test"))
                .resource(condition)
                .request(bundleEntryRequest)
                .search(Search.builder().mode(SearchEntryMode.MATCH).build())
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertEquals(issues.get(1).getDetails().getText().getValue(), "Bundle.Entry.search must be empty.");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(1).getCode(), IssueType.INVALID);
        }
    }

    /**
     * Test invalid bundle entry response (bundle constraint bdl-4).
     */
    @Test
    public void testInvalidBundleEntryResponse() throws Exception {
        Condition condition = Condition.builder()
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test"))
                .resource(condition)
                .request(bundleEntryRequest)
                .response(Response.builder().status("test").build())
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertEquals(issues.get(1).getDetails().getText().getValue(), "Bundle.Entry.response must be empty.");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(1).getCode(), IssueType.INVALID);
        }
    }

    /**
     * Test version specific fullUrl (bundle constraint bdl-8).
     */
    @Test
    public void testVersionSpecificFullUrl() throws Exception {
        Condition condition = Condition.builder()
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test/_history/1"))
                .resource(condition)
                .request(bundleEntryRequest)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertEquals(issues.get(1).getDetails().getText().getValue(), "Bundle.Entry.fullUrl cannot be a version specific reference.");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(1).getCode(), IssueType.VALUE);
        }
    }

    /**
     * Test duplicate fullUrl (bundle constraint bdl-7.
     */
    @Test
    public void testDuplicateFullUrl() throws Exception {
        Condition condition = Condition.builder()
                .id("1")
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();
        Condition condition2 = Condition.builder()
                .id("2")
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("test"))
                .resource(condition)
                .request(bundleEntryRequest)
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("test"))
                .resource(condition2)
                .request(bundleEntryRequest2)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 2);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertEquals(issues.get(1).getDetails().getText().getValue(), "Duplicate Bundle.Entry.fullUrl encountered in bundled request entry: test");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(1).getCode(), IssueType.DUPLICATE);
        }
    }

    /**
     * Test multiple errors.
     */
    @Test
    public void testMultipleErrors() throws Exception {
        Condition condition = Condition.builder()
                .id("1")
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();
        Condition condition2 = Condition.builder()
                .id("2")
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test"))
                .resource(condition)
                .request(bundleEntryRequest)
                .search(Search.builder().mode(SearchEntryMode.MATCH).build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test/_history/1"))
                .resource(condition2)
                .request(bundleEntryRequest2)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry, bundleEntry2)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(issues.size(), 3);
            assertEquals(issues.get(0).getDetails().getText().getValue(), "One or more errors were encountered while validating a 'transaction' request bundle.");
            assertEquals(issues.get(0).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(0).getCode(), IssueType.INVALID);
            assertEquals(issues.get(1).getDetails().getText().getValue(), "Bundle.Entry.search must be empty.");
            assertEquals(issues.get(1).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(1).getCode(), IssueType.INVALID);
            assertEquals(issues.get(2).getDetails().getText().getValue(), "Bundle.Entry.fullUrl cannot be a version specific reference.");
            assertEquals(issues.get(2).getSeverity(), IssueSeverity.FATAL);
            assertEquals(issues.get(2).getCode(), IssueType.VALUE);
        }
    }

    /**
     * Test valid bundle.
     */
    @Test
    public void testValidBundle() throws Exception {
        Condition condition = Condition.builder()
                .meta(Meta.builder()
                    .versionId(Id.of("1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .build();

        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Condition"))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:test"))
                .resource(condition)
                .request(bundleEntryRequest)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .build();

        OperationOutcome expectedOutcome = OperationOutcome.builder()
                .issue(Issue.builder()
                    .severity(IssueSeverity.INFORMATION)
                    .code(IssueType.INFORMATIONAL)
                    .details(CodeableConcept.builder()
                        .text(string("All OK"))
                        .build())
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle responseBundle = helper.doBundle(requestBundle, false);
            assertEquals(responseBundle.getEntry().get(0).getResource().as(OperationOutcome.class), expectedOutcome);
        } catch (Exception e) {
            fail();
        }
    }

}