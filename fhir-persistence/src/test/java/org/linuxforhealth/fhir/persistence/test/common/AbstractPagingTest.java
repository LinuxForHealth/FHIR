/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.test.common;

import static org.linuxforhealth.fhir.model.test.TestUtil.isResourceInResponse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.persistence.MultiResourceResult;
import org.linuxforhealth.fhir.persistence.ResourceResult;
import org.linuxforhealth.fhir.persistence.context.FHIRHistoryContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import org.linuxforhealth.fhir.persistence.util.FHIRPersistenceTestSupport;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;

/**
 * This class contains a collection of search result sorting related tests that will be run against
 * each of the various persistence layer implementations that implement a subclass of this class.
 */
public abstract class AbstractPagingTest extends AbstractPersistenceTest {
    Basic resource1;
    Basic resource2;
    Basic resource3;

    @BeforeClass
    public void createResources() throws Exception {
        FHIRRequestContext.get().setTenantId("all");

        Basic resource = TestUtil.getMinimalResource(Basic.class);

        Basic.Builder resource1Builder = resource.toBuilder();
        Basic.Builder resource2Builder = resource.toBuilder();
        Basic.Builder resource3Builder = resource.toBuilder();

        // number
        resource1Builder.extension(extension("http://example.org/integer", Integer.of(1)));
        resource2Builder.extension(extension("http://example.org/integer", Integer.of(2)));
        resource3Builder.extension(extension("http://example.org/integer", Integer.of(3)));

        // save them in-order so that lastUpdated goes from 1 -> 3 as well
        resource1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource1Builder.meta(tag("pagingTest")).build()).getResource();
        resource2 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource2Builder.meta(tag("pagingTest")).build()).getResource();
        resource3 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource3Builder.meta(tag("pagingTest")).build()).getResource();

        // update resource3 two times so we have 3 different versions
        resource3 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource3.getId(), resource3).getResource();
        resource3 = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource3.getId(), resource3).getResource();
        
    }

    @AfterClass
    public void removeSavedResourcesAndResetTenant() throws Exception {
        Resource[] resources = {resource1, resource2, resource3};
        if (persistence.isDeleteSupported()) {
            // as this is AfterClass, we need to manually start/end the transaction
            startTrx();
            for (Resource resource : resources) {
                FHIRPersistenceTestSupport.delete(persistence, getDefaultPersistenceContext(), resource);
            }
            commitTrx();
        }
        FHIRRequestContext.get().setTenantId("default");
    }

    // This test assumes sorting is working, as tested in the AbstractSortTest
    @Test
    public void testSearchPaging() throws Exception {
        Map<String, List<String>> queryParameters;
        List<Resource> results;

        queryParameters = new HashMap<>();
        queryParameters.put("_sort", Collections.singletonList("integer"));
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("1"));
        results = runQueryTest(Basic.class, queryParameters, 1);
        assertEquals(results.size(), 1, "expected number of results");
        assertNotNull(isResourceInResponse(resource1, results));

        queryParameters = new HashMap<>();
        queryParameters.put("_sort", Collections.singletonList("integer"));
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("2"));
        results = runQueryTest(Basic.class, queryParameters, 1);
        assertEquals(results.size(), 1, "expected number of results");
        assertTrue(isResourceInResponse(resource2, results));

        queryParameters = new HashMap<>();
        queryParameters.put("_sort", Collections.singletonList("integer"));
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("3"));
        results = runQueryTest(Basic.class, queryParameters, 1);
        assertEquals(results.size(), 1, "expected number of results");
        assertTrue(isResourceInResponse(resource3, results));
    }

    // history results should be sorted with oldest versions last
    @Test
    public void testHistoryPaging() throws Exception {
        FHIRHistoryContext historyContext;
        FHIRPersistenceContext context;
        MultiResourceResult result;
        List<ResourceResult<? extends Resource>> results;

        historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        historyContext.setPageSize(1);
        historyContext.setPageNumber(1);
        context = this.getPersistenceContextForHistory(historyContext);

        result = persistence.history(context, resource3.getClass(), resource3.getId());
        assertTrue(result.isSuccess());
        results = result.getResourceResults();
        assertEquals(results.size(), 1, "expected number of results");
        assertEquals(results.get(0).getResource().getMeta().getVersionId().getValue(), "3", "expected version");

        historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        historyContext.setPageSize(1);
        historyContext.setPageNumber(2);
        context = this.getPersistenceContextForHistory(historyContext);

        result = persistence.history(context, resource3.getClass(), resource3.getId());
        assertTrue(result.isSuccess());
        results = result.getResourceResults();
        assertEquals(results.size(), 1, "expected number of results");
        assertEquals(results.get(0).getResource().getMeta().getVersionId().getValue(), "2", "expected version");


        historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        historyContext.setPageSize(1);
        historyContext.setPageNumber(3);
        context = this.getPersistenceContextForHistory(historyContext);

        result = persistence.history(context, resource3.getClass(), resource3.getId());
        assertTrue(result.isSuccess());
        results = result.getResourceResults();
        assertEquals(results.size(), 1, "expected number of results");
        assertEquals(results.get(0).getResource().getMeta().getVersionId().getValue(), "1", "expected version");

        historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        historyContext.setLenient(true);
        historyContext.setPageSize(1);
        historyContext.setPageNumber(0);
        context = this.getPersistenceContextForHistory(historyContext);

        result = persistence.history(context, resource3.getClass(), resource3.getId());
        assertTrue(result.isSuccess());
        results = result.getResourceResults();
        assertEquals(results.size(), 1, "expected number of results");
        assertEquals(results.get(0).getResource().getMeta().getVersionId().getValue(), "3", "expected version");
        OperationOutcome outcome = result.getOutcome();
        assertTrue(outcome != null);
        assertEquals(outcome.getIssue().size(), 1);
        assertEquals(outcome.getIssue().get(0).getSeverity(), IssueSeverity.WARNING);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.INVALID);

        historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        historyContext.setLenient(true);
        historyContext.setPageSize(1);
        historyContext.setPageNumber(4);
        context = this.getPersistenceContextForHistory(historyContext);

        result = persistence.history(context, resource3.getClass(), resource3.getId());
        assertTrue(result.isSuccess());
        results = result.getResourceResults();
        assertEquals(results.size(), 1, "expected number of results");
        assertEquals(results.get(0).getResource().getMeta().getVersionId().getValue(), "1", "expected version");
        outcome = result.getOutcome();
        assertTrue(outcome != null);
        assertEquals(outcome.getIssue().size(), 1);
        assertEquals(outcome.getIssue().get(0).getSeverity(), IssueSeverity.WARNING);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.INVALID);

        historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        historyContext.setLenient(false);
        historyContext.setPageSize(1);
        historyContext.setPageNumber(0);
        context = this.getPersistenceContextForHistory(historyContext);

        result = persistence.history(context, resource3.getClass(), resource3.getId());
        assertFalse(result.isSuccess());
        results = result.getResourceResults();
        assertEquals(results.size(), 0, "expected number of results");
        outcome = result.getOutcome();
        assertTrue(outcome != null);
        assertEquals(outcome.getIssue().size(), 1);
        assertEquals(outcome.getIssue().get(0).getSeverity(), IssueSeverity.ERROR);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.INVALID);

        historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        historyContext.setLenient(false);
        historyContext.setPageSize(1);
        historyContext.setPageNumber(4);
        context = this.getPersistenceContextForHistory(historyContext);

        result = persistence.history(context, resource3.getClass(), resource3.getId());
        assertFalse(result.isSuccess());
        results = result.getResourceResults();
        assertEquals(results.size(), 0, "expected number of results");
        outcome = result.getOutcome();
        assertTrue(outcome != null);
        assertEquals(outcome.getIssue().size(), 1);
        assertEquals(outcome.getIssue().get(0).getSeverity(), IssueSeverity.ERROR);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.INVALID);
    }

    public void testPageSizeEqualsZero() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_sort", Collections.singletonList("integer"));
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("1"));
        queryParameters.put("_count", Collections.singletonList("0"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(true);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertTrue(result.isSuccess());
        assertTrue(result.getResourceResults().isEmpty());
        assertTrue(result.getOutcome() == null);
        assertEquals(searchContext.getLastPageNumber(), 3);
    }

    @Test
    public void testInvalidPage0() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_sort", Collections.singletonList("integer"));
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("0"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(true);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertTrue(result.isSuccess());
        assertFalse(result.getResourceResults().isEmpty());
        OperationOutcome outcome = result.getOutcome();
        assertTrue(outcome != null);
        assertEquals(outcome.getIssue().size(), 1);
        assertEquals(outcome.getIssue().get(0).getSeverity(), IssueSeverity.WARNING);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.INVALID);
        assertEquals(searchContext.getLastPageNumber(), 3);
    }

    @Test
    public void testInvalidPage0Strict() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_sort", Collections.singletonList("integer"));
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("0"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(false);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertFalse(result.isSuccess());
        assertTrue(result.getResourceResults().isEmpty());
        OperationOutcome outcome = result.getOutcome();
        assertTrue(outcome != null);
        assertEquals(outcome.getIssue().size(), 1);
        assertEquals(outcome.getIssue().get(0).getSeverity(), IssueSeverity.ERROR);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.INVALID);
        assertEquals(searchContext.getLastPageNumber(), 3);
    }

    @Test
    public void testInvalidPage4() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_sort", Collections.singletonList("integer"));
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("4"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(true);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertTrue(result.isSuccess());
        assertFalse(result.getResourceResults().isEmpty());
        OperationOutcome outcome = result.getOutcome();
        assertTrue(outcome != null);
        assertEquals(outcome.getIssue().size(), 1);
        assertEquals(outcome.getIssue().get(0).getSeverity(), IssueSeverity.WARNING);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.INVALID);
        assertEquals(searchContext.getLastPageNumber(), 3);
    }

    @Test
    public void testInvalidPage4Strict() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_sort", Collections.singletonList("integer"));
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("4"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(false);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertFalse(result.isSuccess());
        assertTrue(result.getResourceResults().isEmpty());
        OperationOutcome outcome = result.getOutcome();
        assertTrue(outcome != null);
        assertEquals(outcome.getIssue().size(), 1);
        assertEquals(outcome.getIssue().get(0).getSeverity(), IssueSeverity.ERROR);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.INVALID);
        assertEquals(searchContext.getLastPageNumber(), 3);
    }

    @Test
    public void testInvalidPage4NoTotal() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_sort", Collections.singletonList("integer"));
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("4"));
        queryParameters.put("_total", Collections.singletonList("none"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(false);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        // Since _total=none, the search will not be able to determine that page 4 is too big,
        // but will return no resources and last page number will be left at MAX_VALUE
        assertTrue(result.isSuccess());
        assertTrue(result.getResourceResults().isEmpty());
        OperationOutcome outcome = result.getOutcome();
        assertFalse(outcome != null);
        assertEquals(searchContext.getLastPageNumber(), java.lang.Integer.MAX_VALUE);
    }
    
    /**
     * Test if the search result contains the expectedNextId and expectedPreviousId. 
     * @throws Exception
     */
    @Test
    public void testFirstAndLastId() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("2"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(true);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertTrue(result.isSuccess());
        assertTrue(result.getExpectedNextId() != null);
        assertTrue(result.getExpectedPreviousId() != null);
        assertFalse(result.getResourceResults().isEmpty());
        assertFalse(searchContext.getOutcomeIssues() != null);
    }
    
    
    /**
     * Test if the additional record at the end of previous page of search results 
     * matches with the _lastId request parameter with an invalid lastId value. The search result should contain 
     * 'Pages have shifted' warning.
     * @throws Exception
     */
    @Test
    public void testInvalidLastId() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("2"));
        queryParameters.put("_lastId", Collections.singletonList("1234"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(true);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertTrue(result.isSuccess());
        assertFalse(result.getResourceResults().isEmpty());
        List<Issue> issues = searchContext.getOutcomeIssues();
        assertEquals(issues.size(), 1);
        assertEquals(issues.get(0).getSeverity(), IssueSeverity.WARNING);
        assertEquals(issues.get(0).getCode(), IssueType.CONFLICT);
        assertEquals(issues.get(0).getDetails().getText().getValue(), "Pages have shifted; check pages for changed results.");
        assertTrue(result.getExpectedNextId() != null);
        assertTrue(result.getExpectedPreviousId() != null);
    }
    
    
    /**
     * Test if the additional record at the start of next page of search results 
     * matches with the _first request parameter with a valid firstId value.
     * The search result should contain 'Pages have shifted' warning.
     * @throws Exception
     */
    @Test
    public void testInvalidFirstId() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("2"));
        queryParameters.put("_firstId", Collections.singletonList("1234"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(true);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertTrue(result.isSuccess());
        assertFalse(result.getResourceResults().isEmpty());
        List<Issue> issues = searchContext.getOutcomeIssues();
        assertEquals(issues.size(), 1);
        assertEquals(issues.get(0).getSeverity(), IssueSeverity.WARNING);
        assertEquals(issues.get(0).getCode(), IssueType.CONFLICT);
        assertEquals(issues.get(0).getDetails().getText().getValue(), "Pages have shifted; check pages for changed results.");
        assertTrue(result.getExpectedNextId() != null);
        assertTrue(result.getExpectedPreviousId() != null);
    }
    
    /**
     * Test the expectedPreviousId which is the expected resource Id of the last result of the previous page of search results.  
     * The expectedPreviousId should be null for page 1 of search results.
     * @throws Exception
     */
    @Test
    public void testExpectedPreviousIdForPage1() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("2"));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(true);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertTrue(result.isSuccess());
        assertFalse(result.getExpectedPreviousId() != null);
    }
    
    /**
     * Test the expectedNextId which is the expected resource Id of the first result of the next page of search results.  
     * The expectedNextId should be null for the last of search results.
     * @throws Exception
     */
    @Test
    public void testExpectedNextIdForLastPage() throws Exception {
        Map<String, List<String>> queryParameters;
        queryParameters = new HashMap<>();
        queryParameters.put("_tag", Collections.singletonList("pagingTest"));
        queryParameters.put("_page", Collections.singletonList("3")); // page 3 is the last page of search results
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Basic.class, queryParameters);
        searchContext.setLenient(true);
        MultiResourceResult result = runQueryTest(searchContext, Basic.class, queryParameters, 1);
        assertTrue(result.isSuccess());
        assertFalse(result.getExpectedNextId() != null);
    }
    
    /**
     * Test if the system level search result contains the expectedNextId and expectedPreviousId. 
     * @throws Exception
     */
    @Test
    public void testSearchAllUsingTypeAndValidLastId() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_type", Collections.singletonList("Basic"));
        queryParms.put("_page", Collections.singletonList("2"));
        queryParms.put("_lastId", Collections.singletonList(resource2.getId()));
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(Resource.class, queryParms);
        searchContext.setLenient(true);
        MultiResourceResult result = runQueryTest(searchContext, Resource.class, queryParms, 1);
        assertTrue(result.isSuccess());
        assertTrue(result.getExpectedNextId() != null);
        assertTrue(result.getExpectedPreviousId() != null);
        assertFalse(result.getResourceResults().isEmpty());
        assertFalse(searchContext.getOutcomeIssues() != null);
    }
    

    private Meta tag(String tag) {
        return Meta.builder()
                   .tag(Coding.builder()
                              .code(Code.of(tag))
                              .build())
                   .build();
    }

    private Extension extension(String url, Element value) {
        return Extension.builder()
                        .url(url)
                        .value(value)
                        .build();
    }
}
