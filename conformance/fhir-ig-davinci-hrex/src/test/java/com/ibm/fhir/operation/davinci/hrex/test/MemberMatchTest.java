/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.ig.davinci.hrex.test.ProfileTest;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome.Builder;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.davinci.hrex.MemberMatchOperation;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationAdapter;
import com.ibm.fhir.operation.davinci.hrex.configuration.ConfigurationFactory;
import com.ibm.fhir.operation.davinci.hrex.provider.MemberMatchFactory;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.DefaultMemberMatchStrategy;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult.ResponseType;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchStrategy;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

/**
 * Run the Unit Tests for MemberMatch
 */
public class MemberMatchTest {

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("src/test/resources");
    }

    public void createContext(String tenant) throws FHIRException {
        // Configure the request context for our search tests
        FHIRRequestContext.remove();
        FHIRRequestContext context = FHIRRequestContext.get();
        if (context == null) {
            context = new FHIRRequestContext();
        }
        FHIRRequestContext.set(context);

        //Facilitate the switching of tenant configurations based on method name
        context.setTenantId(tenant);
    }

    @Test
    public void testOperationDefinition() {
        MemberMatchOperation operation = new MemberMatchOperation();
        OperationDefinition definition = operation.getDefinition();
        assertNotNull(definition);
    }

    @Test
    public void testOperationInvoke() throws FHIROperationException {
        FHIROperationContext operationContext = FHIROperationContext.createResourceTypeOperationContext();
        operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "POST");
        Class<? extends Resource> resourceType = Patient.class;
        MemberMatchOperation operation = new MemberMatchOperation();
        Parameters output = operation.invoke(operationContext, resourceType, null, null, generateInput(), new LocalResourceHelpers());
        assertNotNull(output);
    }

    @Test
    public void testConfiguration_DisabledByDefault() throws FHIRException {
        createContext("default");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertFalse(adapter.enabled());
        assertEquals(adapter.getMemberStrategyKey(), "default");
        FHIRRequestContext.remove();
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void testConfiguration_NoExtendedProps() throws FHIRException {
        createContext("default");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        adapter.getExtendedStrategyPropertyGroup().getJsonObj();
    }

    @Test
    public void testConfiguration_EnabledByDefault() throws FHIRException {
        createContext("defaulta");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertTrue(adapter.enabled());
        assertEquals(adapter.getMemberStrategyKey(), "defaulta");
        FHIRRequestContext.remove();
    }

    @Test
    public void testConfiguration_Require() throws FHIRException {
        createContext("defaulta");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertTrue(adapter.enabled());
        assertTrue(adapter.requireMemberPatient());
        assertTrue(adapter.requireOldCoverage());
        FHIRRequestContext.remove();
    }

    @Test
    public void testConfiguration_Default() throws FHIRException {
        createContext("default");
        ConfigurationAdapter adapter = ConfigurationFactory.factory().getConfigurationAdapter();
        assertFalse(adapter.enabled());
        assertFalse(adapter.requireMemberPatient());
        assertFalse(adapter.requireOldCoverage());
        FHIRRequestContext.remove();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMemberMatchResult_NoResponseTypeSpecified() {
        MemberMatchResult.builder().build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMemberMatchResult_SingleNoValueSpecified() {
        MemberMatchResult.builder().responseType(ResponseType.SINGLE).build();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testMemberMatchResult_SingleEmptyValueSpecified() {
        MemberMatchResult.builder().responseType(ResponseType.SINGLE).value("").build();
    }

    @Test
    public void testMemberMatchFactory() {
        MemberMatchFactory factory = MemberMatchFactory.factory();
        assertNotNull(factory);
        MemberMatchStrategy strategy = factory.getStrategy(new ConfigurationAdapter() {

            @Override
            public boolean enabled() {
                return false;
            }

            @Override
            public String getMemberStrategyKey() {
                return "default";
            }

            @Override
            public PropertyGroup getExtendedStrategyPropertyGroup() {
                return null;
            }

            @Override
            public boolean requireOldCoverage() {
                return false;
            }

            @Override
            public boolean requireMemberPatient() {
                return false;
            }
        });
        assertNotNull(strategy);

    }

    @Test
    public void testMemberMatchFactory_BadKey() {
        MemberMatchFactory factory = MemberMatchFactory.factory();
        assertNotNull(factory);
        MemberMatchStrategy strategy = factory.getStrategy(new ConfigurationAdapter() {

            @Override
            public boolean enabled() {
                return false;
            }

            @Override
            public String getMemberStrategyKey() {
                return "defaultx";
            }

            @Override
            public PropertyGroup getExtendedStrategyPropertyGroup() {
                return null;
            }

            @Override
            public boolean requireOldCoverage() {
                return false;
            }

            @Override
            public boolean requireMemberPatient() {
                return false;
            }
        });
        assertNotNull(strategy);
        assertEquals("default", strategy.getMemberMatchIdentifier());
    }

    @Test
    public void testMemberMatchResult_All() {
        MemberMatchResult multiple = MemberMatchResult.builder().responseType(ResponseType.MULTIPLE).build();
        assertNotNull(multiple.getResponseType());
        assertEquals(multiple.getResponseType(), ResponseType.MULTIPLE);

        MemberMatchResult noMatch = MemberMatchResult.builder().responseType(ResponseType.NO_MATCH).build();
        assertNotNull(noMatch.getResponseType());
        assertEquals(noMatch.getResponseType(), ResponseType.NO_MATCH);

        MemberMatchResult single =
                MemberMatchResult.builder()
                    .responseType(ResponseType.SINGLE)
                    .value("12345")
                    .type("system1", "code1")
                    .type("system2", "code2")
                    .system("my-system-identifier")
                    .build();
        assertNotNull(single.getResponseType());
        assertEquals(single.getResponseType(), ResponseType.SINGLE);
        assertNotNull(single.getSystem());
        assertEquals(single.getSystem(), "my-system-identifier");
        assertNotNull(single.getValue());
        assertEquals(single.getValue(), "12345");
        assertEquals(single.getTypes().size(), 3);
        assertTrue(single.getTypes().containsKey("system1"));
        assertTrue(single.getTypes().containsKey("system2"));
        assertTrue(single.getTypes().containsKey("http://terminology.hl7.org/CodeSystem/v2-0203"));
    }

    @Test
    public void testMemberMatchStrategy_OutputSingle() {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        MemberMatchResult single =
                MemberMatchResult.builder()
                    .responseType(ResponseType.SINGLE)
                    .value("12345")
                    .type("system1", "code1")
                    .type("system2", "code2")
                    .system("my-system-identifier")
                    .build();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.setFHIROperationContext(ctx);
        Parameters output = strategy.output(single);
        assertNotNull(output);
        Parameters.Parameter param = output.getParameter().get(0);
        assertNotNull(param.getName());
        assertEquals(param.getName().getValue(), "MemberIdentifier");
    }

    @Test
    public void testMemberMatchStrategy_OutputSingle_NoSystem() {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        MemberMatchResult single =
                MemberMatchResult.builder()
                    .responseType(ResponseType.SINGLE)
                    .value("12345")
                    .type("system1", "code1")
                    .type("system2", "code2")
                    .build();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.setFHIROperationContext(ctx);
        Parameters output = strategy.output(single);
        assertNotNull(output);
        Parameters.Parameter param = output.getParameter().get(0);
        assertNotNull(param.getName());
        assertEquals(param.getName().getValue(), "MemberIdentifier");
    }

    @Test
    public void testMemberMatchStrategy_Output_MultipleMatch() {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        MemberMatchResult multipleMatch =
                MemberMatchResult.builder()
                    .responseType(ResponseType.MULTIPLE)
                    .build();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.setFHIROperationContext(ctx);
        Parameters output = strategy.output(multipleMatch);
        assertNotNull(output);
        assertNotNull(ctx.getProperty(FHIROperationContext.PROPNAME_RESPONSE));
        assertNotNull(ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE));
        Integer status = (Integer) ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE);
        assertEquals(status.intValue(), 422);

        Response response = (Response) ctx.getProperty(FHIROperationContext.PROPNAME_RESPONSE);
        assertNotNull(response);
    }

    @Test
    public void testMemberMatchStrategy_Output_NoMatch_MultipleMatch() {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        MemberMatchResult noMatch =
                MemberMatchResult.builder()
                    .responseType(ResponseType.NO_MATCH)
                    .build();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.setFHIROperationContext(ctx);
        Parameters output = strategy.output(noMatch);
        assertNotNull(output);
        assertNotNull(ctx.getProperty(FHIROperationContext.PROPNAME_RESPONSE));
        assertNotNull(ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE));
        Integer status = (Integer) ctx.getProperty(FHIROperationContext.PROPNAME_STATUS_TYPE);
        assertEquals(status.intValue(), 422);

        Response response = (Response) ctx.getProperty(FHIROperationContext.PROPNAME_RESPONSE);
        assertNotNull(response);
    }

    @Test(expectedExceptions = {FHIROperationException.class})
    public void testMemberMatchStrategy_Execute_Null() throws FHIROperationException {
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        strategy.execute(null, null, new LocalResourceHelpers());
    }

    @Test
    public void testMemberMatchStrategy_Execute() throws FHIROperationException {
        FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext();
        DefaultMemberMatchStrategy strategy = new DefaultMemberMatchStrategy();
        Parameters output = strategy.execute(ctx, generateInput(), new LocalResourceHelpers());

        assertNotNull(output);
        assertEquals(output.getParameter().get(0).getValue().as(com.ibm.fhir.model.type.Identifier.class).getValue().getValue(), "55678");
    }

    /**
     * generates the input
     * @return
     */
    private Parameters generateInput() {
        try (Reader r = new InputStreamReader(ProfileTest.class.getResourceAsStream("/JSON/Parameters-member-match-in.json"))) {
            return FHIRParser.parser(Format.JSON).parse(r);
        } catch (Exception e) {
            throw new IllegalArgumentException("Params issue");
        }
    }

    private static class LocalResourceHelpers implements FHIRResourceHelpers {

        @Override
        public FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist, boolean doValidation) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, boolean doValidation) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public FHIRRestOperationResponse doPatch(String type, String id, FHIRPatch patch, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public FHIRRestOperationResponse doDelete(String type, String id, String searchQueryString) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SingleResourceResult<? extends Resource> doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted, Resource contextResource,
            MultivaluedMap<String, String> queryParameters) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Resource doVRead(String type, String id, String versionId, MultivaluedMap<String, String> queryParameters) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Bundle doHistory(MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters, String requestUri,
            Resource contextResource) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Resource doInvoke(FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId, String operationName,
            Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Bundle doBundle(Bundle bundle, boolean skippableUpdates) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public FHIRPersistenceTransaction getTransaction() throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int doReindex(FHIROperationContext operationContext, Builder operationOutcomeResult, Instant tstamp, List<Long> indexIds,
            String resourceLogicalId) throws Exception {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public List<Long> doRetrieveIndex(FHIROperationContext operationContext, String resourceTypeName, int count, Instant notModifiedAfter,
            Long afterIndexId) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
